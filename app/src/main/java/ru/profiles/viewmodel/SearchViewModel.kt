package ru.profiles.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.profiles.model.SearchModel
import ru.profiles.model.ServiceModel
import java.util.concurrent.Executors
import javax.inject.Inject
import ru.profiles.data.*


class SearchViewModel @Inject constructor(private val mUserRep: UserRepository,
                                          private val mServicesRepository: ServicesRepository,
                                          private val mGoodsRepository: GoodsRepository
) :  ViewModel() {

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val executor = Executors.newFixedThreadPool(5)

    private val mSearchString = MutableLiveData<String>()

    init{
        Log.i("ProfilesInfo", "$this constructor")
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPrefetchDistance(10)
            .setPageSize(10).build()

        /*mServiceLiveData = LivePagedListBuilder(ServiceDataSourceFactory(mServicesRepository, mapOf()), pagedListConfig)
            .setFetchExecutor(executor)
            .build()*/
        applySearch("%") // reset search on create
    }

    fun getServices(): LiveData<PagedList<ServiceModel>>{
        return mServicesRepository.getServicesList()
        //return mServiceLiveData
    }

    fun applySearch(searchString: String){
        if(mSearchString.value != searchString) {
            viewModelScope.launch {
                mServicesRepository.applySearch(searchString)
                mSearchString.value = searchString
            }
        }
    }

    fun getActualSearch(): LiveData<String>{
        return mSearchString
    }

    fun getSearchSuggestions(suggestString: String): List<String>{
        return mServicesRepository.getSearchSuggestions(suggestString)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i("ProfilesInfo", "$this onCleared")
    }
}
