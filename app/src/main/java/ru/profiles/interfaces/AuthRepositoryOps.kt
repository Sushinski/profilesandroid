package ru.profiles.interfaces

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.profiles.model.AuthModel
import ru.profiles.model.pojo.AuthRequest
import ru.profiles.model.pojo.AuthResponse
import ru.profiles.model.pojo.TokenRefreshBody
import java.util.concurrent.TimeUnit

interface AuthRepositoryOps {

    suspend fun saveAuth(authModel: AuthModel)

    suspend fun updateAuth(authModel: AuthModel)


    suspend fun clearAuth()

    fun getAuth(): Observable<AuthModel?>

    fun refreshToken(refreshToken: String): Observable<AuthResponse>

    fun authUser(identity: String, pswd: String) : Single<AuthResponse>

}