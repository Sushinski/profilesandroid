package ru.profiles.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import ru.profiles.data.ServicesRepository
import ru.profiles.di.ViewModelFactory
import ru.profiles.model.ServiceModel
import ru.profiles.model.ServiceWithRelations
import javax.inject.Inject

class ServiceCardViewModel @Inject constructor(private val mServicesRepository: ServicesRepository) : ViewModel() {

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    init{
        Log.i("ProfilesInfo", "$this constructor")
    }


}