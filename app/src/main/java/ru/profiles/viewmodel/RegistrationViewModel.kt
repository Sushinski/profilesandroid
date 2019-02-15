package ru.profiles.viewmodel

import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.isActive
import okhttp3.RequestBody
import retrofit2.HttpException
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.dao.AuthModelDao
import ru.profiles.data.AuthRepository
import ru.profiles.data.RegistrationRepository
import ru.profiles.data.ResourcesRepository
import ru.profiles.data.UserRepository
import ru.profiles.extensions.isEmailValid
import ru.profiles.extensions.toSingleEvent
import ru.profiles.livedata.SingleLiveEvent
import ru.profiles.model.AuthModel
import ru.profiles.model.ErrorModel
import ru.profiles.model.UserModel
import java.security.KeyStore
import java.util.concurrent.TimeoutException
import javax.inject.Inject


class RegistrationViewModel @Inject constructor(private val mUserRep: UserRepository,
                                                private val mRegRep: RegistrationRepository,
                                                private val mResRep: ResourcesRepository,
                                                private val mAuthRep: AuthRepository,
                                                private val mGson: Gson
) : ViewModel() {

    private val mErrorStatus = SingleLiveEvent<ErrorModel>()

    private val mPicUploadStatus = SingleLiveEvent<Boolean>()

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val mDisposables = CompositeDisposable()

    var mLocalPicUri: Uri? = null
    set(value){
        mUriLiveData.value = value
        field = value
    }

    private val mUriLiveData: MutableLiveData<Uri?> = MutableLiveData()



    fun getLocalPicUri(): LiveData<Uri?>{
        return mUriLiveData
    }

    fun getRegisteredUser(): LiveData<UserModel> { // todo replace with login data
        return mUserRep.getRegisteredUser().toSingleEvent()
    }

    fun clearRegisteredUser(){
        runBlocking { mUserRep.deleteRegisteredUser() }
    }

    fun regUser(identity: String, pswd: String, name: String, surname: String, gender: Int){
        mDisposables.add(
            mRegRep.registerUser(identity, pswd, name, surname, gender)
                .subscribe (
                {
                    auth->if (auth.mRefreshToken.isNotEmpty() && auth.mToken.isNotEmpty()) {
                        val a = String(Base64.decode(auth.mToken.split('.')[1], Base64.DEFAULT))
                        val u = mGson.fromJson(a, UserModel::class.java)
                        val am = AuthModel(auth.mToken, auth.mRefreshToken)
                        runBlocking { mUserRep.deleteRegisteredUser() }
                        viewModelScope.launch {
                            mAuthRep.clearAuth()
                            mUserRep.saveUser(u)
                            mAuthRep.saveAuth(am)
                        }
                    }
                },
                {
                    error->
                    mErrorStatus.value = when(error) {
                        is HttpException ->
                            try{
                                val e = error.response().errorBody()?.string()
                                Log.e("ProfilesError", e)
                                 mGson.fromJson(e, ErrorModel::class.java)
                            }// todo localize messages
                            catch (e: JsonSyntaxException){ ErrorModel(mUserMessage = "Login data error") }
                        is TimeoutException -> ErrorModel(mUserMessage = "Can`t connect to server, try again later.")
                        else -> ErrorModel(mUserMessage = "Login error")
                    }
                }
            )
        )
    }

    fun getPicUploadStatus(): LiveData<Boolean>{
        return mPicUploadStatus
    }

    fun saveUserImage(imageBody: RequestBody) {
        mDisposables.add(mResRep.saveImageFile(imageBody).subscribe(
            {
                response -> mPicUploadStatus.value = true
            },
            {
                error->
                when(error) {
                    is HttpException -> try {
                        Log.i("ProfilesInfo", error.response().errorBody()?.string())
                    }// todo localize messages
                    catch (e: JsonSyntaxException) {
                        ErrorModel(mUserMessage = "Registration data error")
                    }
                }
                mPicUploadStatus.value = false
            }
        ))
    }

    fun getRegistrationError(): LiveData<ErrorModel>{
        return mErrorStatus
    }

    override fun onCleared() {
        Log.i("ProfilesInfo", "${this.javaClass} cleared")
        if(!mDisposables.isDisposed) mDisposables.dispose()
        viewModelJob.cancel()
        super.onCleared()
    }
}