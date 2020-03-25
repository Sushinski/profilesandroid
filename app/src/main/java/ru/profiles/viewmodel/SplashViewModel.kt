package ru.profiles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.profiles.extensions.toSingleEvent
import ru.profiles.interfaces.UserRepositoryOps
import ru.profiles.model.UserModel
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val mUserRep: UserRepositoryOps): ViewModel() {

    fun getLoggedUser(): LiveData<UserModel> {
        return mUserRep.getLoggedUser().toSingleEvent()
    }
}