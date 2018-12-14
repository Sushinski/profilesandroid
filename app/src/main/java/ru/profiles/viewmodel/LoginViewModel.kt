package ru.profiles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import ru.profiles.data.AuthRepository
import ru.profiles.data.UserRepository
import ru.profiles.model.pojo.AuthResponse

class LoginViewModel(val mUserRep: UserRepository, val mAuthRep: AuthRepository) : ViewModel() {

    fun loginUser(identity: String, pswd: String): Single<AuthResponse> {
        return mAuthRep.authUser(identity, pswd)
    }
}
