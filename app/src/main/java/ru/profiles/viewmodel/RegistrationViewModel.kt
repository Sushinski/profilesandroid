package ru.profiles.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.profiles.data.UserRepository
import ru.profiles.model.UserModel
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(private val mUserRep: UserRepository) : ViewModel() {

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var mLocalPicUri: Uri? = null

}