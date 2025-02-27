package ru.profiles.data

import android.util.Log
import androidx.annotation.MainThread
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import ru.profiles.model.ServiceModel
import ru.profiles.model.ServiceWithRelations
import java.util.concurrent.Executors

class ServicesBoundaryCallback(private val mServicesRepo: ServicesRepository,
                               private val updateParams: Map<String, String>)
    : PagedList.BoundaryCallback<ServiceWithRelations>() {


    private val mDisposables = CompositeDisposable()

    private val callbackJob = Job()

    private val callbackScope = CoroutineScope(Dispatchers.Main + callbackJob)

    private val mHelper = PagingRequestHelper(Executors.newFixedThreadPool(3))


    companion object {
        const val DATABASE_PAGE_SIZE = 20
    }

    @MainThread
    override fun onZeroItemsLoaded() {
        mHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            updateServices(1, updateParams, it) // start page
        }
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: ServiceWithRelations) {
        val page = (runBlocking { mServicesRepo.getItemNumber(itemAtEnd) } / DATABASE_PAGE_SIZE) + 1
        mHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            updateServices(page, updateParams, it)
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: ServiceWithRelations) {
    }

    private fun updateServices(page: Long,
                               params: Map<String, String>,
                               callback: PagingRequestHelper.Request.Callback){
        mDisposables.add(
            mServicesRepo.updateServiceList(page, params).subscribe {
                    s, e -> s?.result?.let {
                    callbackScope.launch {
                        mServicesRepo.saveServicesList(it)
                        callback.recordSuccess()
                    }
                } ?: e?.let {
                    Log.i("ProfilesInfo", "Error update services: ${it.cause}")
                    callback.recordFailure(e)
                }
            }
        )
    }
}