package ru.profiles.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel;
import javax.inject.Inject

class CalendarViewModel @Inject constructor() : ViewModel() {

    init{
        Log.i("ProfilesInfo", this.toString() + " constructor")
    }


    override fun onCleared() {
        super.onCleared()
        Log.i("ProfilesInfo", this.toString() + " onCleared")
    }
}
