package ru.profiles.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.profiles.dao.AuthModelDao
import ru.profiles.model.AuthModel
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.model.pojo.AuthRequest
import ru.profiles.model.pojo.AuthResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepository(val mAuthDao: AuthModelDao, val mAuthApi: AuthApi) {

    init{
        // todo inject
    }

    fun getUserAuth(user_id: Int): LiveData<AuthModel> = mAuthDao.getUserAuth(user_id)


    fun authUser(identity: String, pswd: String) : Single<AuthResponse> {
        // todo check existing auth
        return mAuthApi.authorizeUser(AuthRequest(identity, pswd))
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).firstOrError()
    }

}