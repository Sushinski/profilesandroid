package ru.profiles.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.profiles.data.GoodsRepository
import ru.profiles.data.ServicesRepository
import ru.profiles.data.UserRepository
import ru.profiles.model.GoodsModel
import ru.profiles.model.ServiceModel
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val mUserRep: UserRepository,
                                          private val mServicesRepository: ServicesRepository,
                                          private val mGoodsRepository: GoodsRepository
)
    :  ViewModel() {

    private val mDisposables = CompositeDisposable()

    init{
        Log.i("ProfilesInfo", "$this constructor")
    }

    fun getPopularServices(is_online: Boolean): LiveData<List<ServiceModel>>{
        // todo get services
        mDisposables.add(
            mServicesRepository.updateServiceList()
            .subscribe (
                { response ->
                    Log.i("ProfilesInfo", "List size = ${response.result.size}")
                },{
                    error->Log.i("ProfilesInfo", "Refresh service error: $error")
                }
        )
        )
        return mServicesRepository.getServicesList()
    }


    fun getPopularGoods(): LiveData<List<GoodsModel>>{
        return mGoodsRepository.getPopularGoods()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ProfilesInfo", "$this onCleared")
    }
}
