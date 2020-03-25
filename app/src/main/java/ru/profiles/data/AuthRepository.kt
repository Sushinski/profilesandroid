package ru.profiles.data

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import ru.profiles.dao.AuthModelDao
import ru.profiles.model.AuthModel
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.interfaces.AuthRepositoryOps
import ru.profiles.model.pojo.AuthRequest
import ru.profiles.model.pojo.AuthResponse
import ru.profiles.model.pojo.TokenRefreshBody
import java.util.concurrent.TimeUnit


class AuthRepository private constructor(private val mAuthDao: AuthModelDao,
                                         private val mAuthApi: AuthApi) : AuthRepositoryOps{



    companion object{
        // For Singleton instantiation
        @Volatile private var instance: AuthRepository? = null

        fun getInstance( authDao: AuthModelDao,
                        authApi: AuthApi) =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(authDao, authApi).also { instance = it }
            }
    }

    override suspend fun saveAuth(authModel: AuthModel){
        withContext(Dispatchers.IO) {
            mAuthDao.saveAuth(authModel)
        }
    }

    override  suspend fun updateAuth(authModel: AuthModel){
        withContext(Dispatchers.IO) {
            mAuthDao.updateAuth(authModel)
        }
    }

    override suspend fun clearAuth(){
        withContext(Dispatchers.IO){
            mAuthDao.deleteAuth()
        }
    }

    override fun getAuth(): Observable<AuthModel?> {
        return mAuthDao.getUserAuth()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).share()
    }

    override fun refreshToken(refreshToken: String): Observable<AuthResponse>{
        return mAuthApi.updateAuth(TokenRefreshBody(refreshToken))
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).share()
    }

    override fun authUser(identity: String, pswd: String) : Single<AuthResponse> {
        return mAuthApi.authorizeUser(AuthRequest(identity, pswd))
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).firstOrError()
    }

}