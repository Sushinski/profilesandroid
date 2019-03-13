package ru.profiles.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import ru.profiles.model.ServiceModel


class ServiceDataSourceFactory(
    private val mServiceRepo: ServicesRepository,
    private val mParams: Map<String, String>
) : DataSource.Factory<Long, ServiceModel>() {

    private val sourceLiveData = MutableLiveData<ServicePageDataSource>()

    override fun create(): DataSource<Long, ServiceModel> {
        val source = ServicePageDataSource(mServiceRepo, mParams)
        sourceLiveData.postValue(source)
        return source
    }
}
