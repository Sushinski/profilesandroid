package ru.profiles.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.profiles.data.UserRepository
import ru.profiles.extensions.toSingleEvent
import ru.profiles.model.UserModel
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val mUserRep: UserRepository):  ViewModel() {

    fun getLoggedUser(): LiveData<UserModel>{
        return mUserRep.getLoggedUser().toSingleEvent()
    }
}
