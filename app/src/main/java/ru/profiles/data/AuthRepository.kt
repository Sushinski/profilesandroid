package ru.profiles.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.Response
import retrofit2.adapter.rxjava2.Result
import ru.profiles.dao.AuthModelDao
import ru.profiles.model.AuthModel
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.model.pojo.AuthRequest
import ru.profiles.model.pojo.AuthResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject

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

    fun getUserAuth(user_id: Int): LiveData<AuthModel> {
        return mAuthDao.getUserAuth(user_id)
    }

    fun authUser(identity: String, pswd: String) : Single<AuthResponse> {
        // todo check existing auth
        return mAuthApi.authorizeUser(AuthRequest(identity, pswd))
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).firstOrError()
    }

}