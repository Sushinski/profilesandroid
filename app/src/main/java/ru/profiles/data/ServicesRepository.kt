package ru.profiles.data

import androidx.lifecycle.LiveData
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



class ServicesRepository private constructor(private val mServicesApi: ServicesApi,
                                             private val mServicesModelDao: ServicesModelDao){

    private var mCurrentLiveData: LiveData<PagedList<ServiceModel>>

    private val mPagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(30)
        .setPrefetchDistance(10)
        .setPageSize(10).build()

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

    init {
        mCurrentLiveData = mServicesModelDao.searchServices("%").toLiveData(
            mPagedListConfig,
            null,
            ServicesBoundaryCallback(this, mapOf())
        )
    }

    fun updateServiceList(pageNum: Long, params: Map<String, String>): Single<ServicesResponse?> {
        return  mServicesApi.getServices(pageNum, params)
            .subscribeOn(Schedulers.io())
            .timeout(30, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).firstOrError()
    }

    suspend fun saveServicesList(list: List<ServiceModel>){
        withContext(IO) {
            mServicesModelDao.saveServicesList(list)
        }
    }

    fun getActualSearch(): LiveData<SearchModel> {
        return mServicesModelDao.getActualSearch("service")
    }

    suspend fun getItemNumber(itemAtEnd: ServiceModel): Long{
        return withContext(IO) {
            mServicesModelDao.getItemsCountForId(itemAtEnd.id)
        }
    }

    fun getServicesList(): LiveData<PagedList<ServiceModel>>{
        return mCurrentLiveData
    }

    suspend fun applySearch(searchString: String){
        mCurrentLiveData = mServicesModelDao.searchServices(searchString).toLiveData(
            mPagedListConfig,
            null,
            ServicesBoundaryCallback(this,
                if(searchString != "%") mapOf("search" to searchString) else mapOf()
            )
        )
        withContext(IO){
            val m = SearchModel(searchString, "service")
            mServicesModelDao.insert(m)
        }
    }

    fun getSearchSuggestions(searchStr: String): List<String>{
        return mServicesModelDao.getSearchSuggestions("%$searchStr%")
    }

}