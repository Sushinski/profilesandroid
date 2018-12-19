package ru.profiles.viewmodel

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.profiles.data.AuthRepository
import ru.profiles.data.UserRepository
import ru.profiles.livedata.SingleLiveEvent
import ru.profiles.model.ErrorModel
import ru.profiles.model.UserModel


class LoginViewModel(private val mUserRep: UserRepository,
                     private val mAuthRep: AuthRepository,
                     private val mGson: Gson
) : ViewModel() {

    private val mErrorStatus = SingleLiveEvent<ErrorModel>()

    private val mDisposables = CompositeDisposable()

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun loginUser(identity: String, pswd: String) {
        mDisposables.add(mAuthRep.authUser(identity, pswd).subscribe (
            { auth -> if (auth.mRefreshToken.isNotEmpty() && auth.mToken.isNotEmpty()) {
                val u = String(Base64.decode(auth.mToken.split('.')[1], Base64.DEFAULT))
                Log.i("ProfilesInfo:" , u)
                val userModel = mGson.fromJson(u, UserModel::class.java)
                    viewModelScope.launch {
                    mUserRep.loginUser(userModel)
                }
            }},
            {
                    error-> Log.e("ProfilesError:", "Wrong login data: " + error.toString())
                val errObj = mGson.fromJson(error.message, ErrorModel::class.java)
                if(errObj != null) mErrorStatus.value = errObj
            }
        ))
    }

    fun getErrorStatus(): LiveData<ErrorModel>{
        return mErrorStatus
    }


    fun getLoggedUser(): LiveData<UserModel>{
        return mUserRep.getLastLoggedUser()
    }



    override fun onCleared() {
        if(!mDisposables.isDisposed) mDisposables.dispose()
        viewModelJob.cancel()
        super.onCleared()
    }
}
