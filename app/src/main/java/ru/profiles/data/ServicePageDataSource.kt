package ru.profiles.data

import android.util.Log
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import ru.profiles.model.ServiceModel


class ServicePageDataSource(private val mServicesRepo: ServicesRepository,
                            private val mParams: Map<String, String>)
    : PageKeyedDataSource<Long, ServiceModel>() {

    private val mDisposables = CompositeDisposable()


    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Long>,
        callback: PageKeyedDataSource.LoadInitialCallback<Long, ServiceModel>
    ) {

        mDisposables.add(mServicesRepo.updateServiceList(1, mParams).subscribe {
            s, e -> s?.result?.let {
                callback.onResult(it, null, 2)

            } ?: e?.let {
                Log.i("ProfilesInfo", "Error update services: ${it.cause}")

            }
        })
    }


    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, ServiceModel>
    ) {
    }


    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, ServiceModel>
    ) {

        Log.i("ProfilesInfo", "Loading Range " + params.key + " Count " + params.requestedLoadSize)

        mDisposables.add(mServicesRepo.updateServiceList(params.key, mParams).subscribe {
                s, e -> s?.result?.let {
            callback.onResult(it, params.key + 1)
        } ?: e?.let {
            Log.i("ProfilesInfo", "Error update services: ${it.cause}")

        }
        })
    }

}