package ru.profiles.data

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import ru.profiles.dao.AuthModelDao
import ru.profiles.model.AuthModel
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.model.pojo.AuthRequest
import ru.profiles.model.pojo.AuthResponse
import ru.profiles.model.pojo.TokenRefreshBody
import java.util.concurrent.TimeUnit


class AuthRepository private constructor(private val mAuthDao: AuthModelDao,
                                         private val mAuthApi: AuthApi) {



    companion object{
        // For Singleton instantiation
        @Volatile private var instance: AuthRepository? = null

        fun getInstance( authDao: AuthModelDao,
                        authApi: AuthApi) =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(authDao, authApi).also { instance = it }
            }
    }

    suspend fun saveAuth(authModel: AuthModel?){
        withContext(Dispatchers.IO) {
            mAuthDao.saveAuth(authModel)
        }
    }

    fun getAuth(): Single<AuthModel?> {
        return mAuthDao.getUserAuth()
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).firstOrError()
    }

    fun refreshToken(refreshToken: String): Single<AuthResponse>{
        return mAuthApi.updateAuth(TokenRefreshBody(refreshToken))
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).firstOrError()
    }

    fun authUser(identity: String, pswd: String) : Single<AuthResponse> {
        return mAuthApi.authorizeUser(AuthRequest(identity, pswd))
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).firstOrError()
    }

}