package ru.profiles.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import ru.profiles.api.interfaces.ServicesApi
import ru.profiles.dao.ServicesModelDao
import ru.profiles.model.SearchModel
import ru.profiles.model.ServiceModel
import ru.profiles.model.pojo.ServicesResponse
import java.util.concurrent.TimeUnit

class ServicesRepository private constructor(val mServicesApi: ServicesApi,
                                             val mServicesModelDao: ServicesModelDao){

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

    fun updateServiceList(pageNum: Int, params: Map<String, String>): Single<ServicesResponse?> {
        return  mServicesApi.getServices(pageNum, params)
            .subscribeOn(Schedulers.io())
            .timeout(10, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).firstOrError()
    }

    suspend fun saveService(service: ServiceModel){
        withContext(IO) {
            mServicesModelDao.saveServiceModel(service)
        }
    }

    fun getActualSearch(): LiveData<SearchModel> {
        return mServicesModelDao.getActualSearch("service")
    }

    fun getServicesList(params: Map<String, String>): LiveData<PagedList<ServiceModel>>{
        return mServicesModelDao.searchServices( params["search"]?.let{"%$it%"} ?: "%").toLiveData(
            ServicesBoundaryCallback.DATABASE_PAGE_SIZE,
            null,
            ServicesBoundaryCallback(this, params)
        )
    }

    suspend fun applySearch(searchString: String){
        withContext(IO){
            val m = SearchModel(searchString, "service")
            mServicesModelDao.insert(m)
        }
    }

    fun getSearchSuggestions(searchStr: String): List<String>{
        return mServicesModelDao.getSearchSuggestions("%$searchStr%")
    }

}