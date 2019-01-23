package ru.profiles.data

import android.util.Log
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.profiles.api.interfaces.RegistrationApi
import ru.profiles.model.pojo.RegistrationRequest
import ru.profiles.model.pojo.RegistrationResponse
import java.util.concurrent.TimeUnit

class RegistrationRepository private constructor(private val mRegApi: RegistrationApi) {

    companion object{
        // For Singleton instantiation
        @Volatile private var instance: RegistrationRepository? = null

        fun getInstance(regApi: RegistrationApi) =
            instance ?: synchronized(this) {
                instance ?: RegistrationRepository(regApi).also { instance = it }
            }
    }

    fun registerUser(identity: String, pswd: String) : Single<RegistrationResponse> {
        // todo check existing auth
        val r = RegistrationRequest(identity, pswd, pswd, null,null)
        Log.i("ProfilesInfo", "reg request: "  + Gson().toJson(r,   RegistrationRequest::class.java))
        return mRegApi.registerUser(r)
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).firstOrError()
    }

}