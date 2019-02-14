package ru.profiles.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.profiles.data.AuthRepository
import ru.profiles.model.AuthModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivityViewModel  @Inject constructor(private val mAuthRep: AuthRepository) : ViewModel() {

    private var mAuthLookupDisposable: Disposable? = null

    private var mRefreshDisposable: Disposable? = null

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init{
        Log.i("ProfilesInfo", "${this.javaClass} created")
        mAuthLookupDisposable = mAuthRep.getAuth()
            .subscribe (
            {
                authModel ->authModel?.let { scheduleRefreshToken(authModel, 5 * 60 ) } // 5 minutes
            },
            {
                error-> Log.i("ProfilesInfo", "Error looking tokens: $error")
            }
        )
    }

    fun scheduleRefreshToken(authModel: AuthModel, delay: Long){
        mRefreshDisposable = Observable.timer(delay, TimeUnit.SECONDS)
            .flatMap { o-> mAuthRep.refreshToken(authModel.mRefreshToken)}
            .retry()
            .subscribe (
            { authResponse ->
                viewModelScope.launch {
                    Log.i("ProfilesInfo", "Saves refreshed tokens")
                    authModel.mJwtToken = authResponse.mJwtToken
                    authModel.mRefreshToken = authResponse.mRefreshToken
                    mAuthRep.updateAuth(authModel)
                }
            },
            {
                    error-> Log.i("ProfilesInfo", "Error refreshing tokens: $error")
            }
        )
    }

    override fun onCleared() {
        mRefreshDisposable?.let{ if(!it.isDisposed) it.dispose() }
        mAuthLookupDisposable?.let{ if(!it.isDisposed) it.dispose() }
        super.onCleared()
    }
}