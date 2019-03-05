package ru.profiles.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.profiles.data.GoodsRepository
import ru.profiles.data.ServicesBoundaryCallback
import ru.profiles.data.ServicesRepository
import ru.profiles.data.UserRepository
import ru.profiles.model.GoodsModel
import ru.profiles.model.SearchModel
import ru.profiles.model.ServiceModel
import javax.inject.Inject


class SearchViewModel @Inject constructor(private val mUserRep: UserRepository,
                                          private val mServicesRepository: ServicesRepository,
                                          private val mGoodsRepository: GoodsRepository
) :  ViewModel() {

    private val mDisposables = CompositeDisposable()

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init{
        Log.i("ProfilesInfo", "$this constructor")
    }

    fun getServices(params: Map<String, String>): LiveData<PagedList<ServiceModel>>{
        return mServicesRepository.getServicesList(params)
    }

    fun applySearch(searchString: String){
        viewModelScope.launch {
            mServicesRepository.applySearch(searchString)
        }
    }

    fun getActualSearch(): LiveData<SearchModel>{
        return mServicesRepository.getActualSearch()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i("ProfilesInfo", "$this onCleared")
    }
}
