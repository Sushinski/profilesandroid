package ru.profiles.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class NotificationViewModel @Inject constructor(): ViewModel() {
    // TODO: Implement the ViewModel

    init{
        Log.i("ProfilesInfo", this.toString() + " constructor")
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("ProfilesInfo", this.toString() + " onCleared")
    }
}
