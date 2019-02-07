package ru.profiles.viewmodel

import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
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
import ru.profiles.model.ErrorModel
import ru.profiles.model.UserModel
import java.security.KeyStore
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(private val mUserRep: UserRepository,
                                                private val mRegRep: RegistrationRepository,
                                                private val mResRep: ResourcesRepository,
                                                private val mGson: Gson
) : ViewModel() {

    private val mErrorStatus = SingleLiveEvent<ErrorModel>()

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

    fun getRegisteredUser(): LiveData<UserModel> {
        return mUserRep.getRegisteredUser().toSingleEvent()
    }

    fun clearRegisteredUser(){
        runBlocking { mUserRep.deleteRegisteredUser() }
    }

    fun saveImage(imageUri: Uri){
        val file = FileUtils.getFile(this, imageUri)
       val requestFile = RequestBody.create(
           MediaType.parse(getContentResolver().getType(imageUri)),
           file
       )
        mDisposables.add(
            mResRep.saveImageFile()
        )
    }

    fun regUser(identity: String, pswd: String){
        mDisposables.add(
            mRegRep.registerUser(identity, pswd).subscribe (
                {
                    auth->if (auth.mRefreshToken.isNotEmpty() && auth.mToken.isNotEmpty()) {
                        //val u = String(Base64.decode(auth.mToken.split('.')[1], Base64.DEFAULT))
                        //val userModel = mGson.fromJson(u, UserModel::class.java)
                        val u  = UserModel()
                        if(android.util.Patterns.EMAIL_ADDRESS.matcher(identity).matches())
                            u.mEmail = identity
                        else if(android.util.Patterns.PHONE.matcher(identity).matches())
                            u.mPhone = identity
                        viewModelScope.launch {
                            mUserRep.deleteRegisteredUser()
                            mUserRep.saveUser(u)
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
                            catch (e: JsonSyntaxException){ ErrorModel(mUserMessage = "Login data error")
                            }
                        is TimeoutException -> ErrorModel(mUserMessage = "Can`t connect to server, try again later.")
                        else -> ErrorModel(mUserMessage = "Login error")
                    }
                }
            )
        )
    }

    fun getRegistrationError(): LiveData<ErrorModel>{
        return mErrorStatus
    }
}