package ru.profiles.viewmodel

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.profiles.data.AuthRepository
import ru.profiles.data.UserRepository
import ru.profiles.livedata.SingleLiveEvent
import ru.profiles.model.ErrorModel
import ru.profiles.model.UserModel
import java.util.concurrent.TimeoutException


class LoginViewModel(private val mUserRep: UserRepository,
                     private val mAuthRep: AuthRepository,
                     private val mGson: Gson
) : ViewModel() {

    private val mErrorStatus = SingleLiveEvent<ErrorModel>()

    private val mLoggedUser = SingleLiveEvent<UserModel>()

    private val mDisposables = CompositeDisposable()

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun loginUser(identity: String, pswd: String) {
        mDisposables.add(mAuthRep.authUser(identity, pswd).subscribe (
            { auth -> if (auth.mRefreshToken.isNotEmpty() && auth.mToken.isNotEmpty()) {
                val u = String(Base64.decode(auth.mToken.split('.')[1], Base64.DEFAULT))
                val userModel = mGson.fromJson(u, UserModel::class.java)
                viewModelScope.launch {
                    mUserRep.saveLoggedUser(userModel)
                    mLoggedUser.value = userModel
                }
            }},
            {
                error->
                mErrorStatus.value = when(error) {
                    is HttpException ->
                            try{mGson.fromJson(error.response().errorBody()?.string(), ErrorModel::class.java)}// todo localize messages
                            catch (e: JsonSyntaxException){ ErrorModel(mUserMessage = "Login data error")}
                    is TimeoutException -> ErrorModel(mUserMessage = "Can`t connect to server, try again later.")
                    else -> ErrorModel(mUserMessage = "Login error")
                }
            }
        ))
    }

    fun getErrorStatus(): LiveData<ErrorModel>{
        return mErrorStatus
    }

    fun getLoggedUser(): LiveData<UserModel>{
        // todo transform from repo livedata
        return mLoggedUser
    }

    override fun onCleared() {
        if(!mDisposables.isDisposed) mDisposables.dispose()
        viewModelJob.cancel()
        super.onCleared()
    }
}
