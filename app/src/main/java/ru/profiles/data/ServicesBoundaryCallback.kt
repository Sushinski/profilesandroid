package ru.profiles.data

import androidx.annotation.MainThread
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import ru.profiles.model.ServiceModel
import java.util.concurrent.Executors

class ServicesBoundaryCallback(private val mServicesRepo: ServicesRepository)
    : PagedList.BoundaryCallback<ServiceModel>() {

    private var pagetoLoad = 2 // next pager from 2

    private val mDisposables = CompositeDisposable()

    private val mHelper = PagingRequestHelper(Executors.newSingleThreadExecutor())

    companion object {
        const val DATABASE_PAGE_SIZE = 10
    }

    @MainThread
    override fun onZeroItemsLoaded() {
        mHelper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            updateServices(1) // start page
        }
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: ServiceModel) {
        mHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            updateServices(pagetoLoad)
            pagetoLoad += 1
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: ServiceModel) {
    }

    private fun updateServices(page: Int){
        mDisposables.add(
            mServicesRepo.updateServiceList(page).subscribe { s, _ ->
                s?.result?.let {
                    for (sm in it) {
                        GlobalScope.launch(Dispatchers.Main) { mServicesRepo.saveService(sm) }
                    }
                }
            }
        )
    }
}