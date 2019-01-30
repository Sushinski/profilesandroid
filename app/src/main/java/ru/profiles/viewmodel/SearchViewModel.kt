package ru.profiles.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.profiles.data.UserRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val mUserRep: UserRepository):  ViewModel() {

    init{
        Log.i("ProfilesInfo", this.toString() + " constructor")
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("ProfilesInfo", this.toString() + " onCleared")
    }
}
