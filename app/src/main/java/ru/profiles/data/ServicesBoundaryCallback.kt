package ru.profiles.data

import android.util.Log
import androidx.annotation.MainThread
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import ru.profiles.model.ServiceModel
import java.util.concurrent.Executors

class ServicesBoundaryCallback(private val mServicesRepo: ServicesRepository,
                               private val updateParams: Map<String, String>)
    : PagedList.BoundaryCallback<ServiceModel>() {


    private val mDisposables = CompositeDisposable()

    private val callbackJob = Job()

    private val callbackScope = CoroutineScope(Dispatchers.Main + callbackJob)

    private val mHelper = PagingRequestHelper(Executors.newSingleThreadExecutor())

    companion object {
        const val DATABASE_PAGE_SIZE = 10
    }

    @MainThread
    override fun onZeroItemsLoaded() {
        mHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            updateServices(1, updateParams) // start page
        }
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: ServiceModel) {
        mHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            val page  = (runBlocking { mServicesRepo.getItemNumber(itemAtEnd) } / DATABASE_PAGE_SIZE) + 1
            updateServices(page, updateParams)
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: ServiceModel) {
    }

    private fun updateServices(page: Long, params: Map<String, String>){
        mDisposables.add(
            mServicesRepo.updateServiceList(page, params).subscribe {
                    s, e -> s?.result?.let {
                    for (sm in it) {
                        callbackScope.launch { mServicesRepo.saveService(sm) }
                    }
                } ?: e?.let {
                Log.i("ProfilesInfo", "Error update services: ${it.cause}")
            }
            }
        )
    }
}