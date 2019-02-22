package ru.profiles.data

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.profiles.api.interfaces.ServicesApi
import ru.profiles.dao.ServicesModelDao
import ru.profiles.model.ServiceModel
import ru.profiles.model.pojo.ServicesResponce
import java.util.concurrent.TimeUnit

class ServicesRepository private constructor(val mServicesApi: ServicesApi, val mServicesModelDao: ServicesModelDao){

    companion object {
        @Volatile private var instance: ServicesRepository? = null

        fun getInstance(
            searchApi: ServicesApi,
            servicesDao: ServicesModelDao
        ) =
            instance ?: synchronized(this) {
                instance ?: ServicesRepository(searchApi, servicesDao).also { instance = it }
            }
    }

    fun updateServiceList(): Observable<ServicesResponce> {
        return  mServicesApi.getServices(HashMap())
            .subscribeOn(Schedulers.io())
            .timeout(10, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveServiceList(service: ServiceModel){
        mServicesModelDao.saveServiceModel(service)
    }

    fun getServicesList(): LiveData<List<ServiceModel>>{
        return mServicesModelDao.getServices()
    }
}