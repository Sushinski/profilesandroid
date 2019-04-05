package ru.profiles.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import ru.profiles.data.*
import ru.profiles.model.CategoryModel
import ru.profiles.model.ServiceWithRelations


class SearchViewModel @Inject constructor(private val mUserRep: UserRepository,
                                          private val mServicesRepository: ServicesRepository,
                                          private val mGoodsRepository: GoodsRepository
) :  ViewModel() {

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val mSearchString = MutableLiveData<String>()

    init{
        Log.i("ProfilesInfo", "$this constructor")
        applySearch("%") // reset search on create
    }

    fun getServices(): LiveData<PagedList<ServiceWithRelations>>{
        return mServicesRepository.getServicesList()
    }

    fun searchCategories(searchString: String): LiveData<Array<CategoryModel>>{
        return mServicesRepository.searchCategories(searchString)
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
