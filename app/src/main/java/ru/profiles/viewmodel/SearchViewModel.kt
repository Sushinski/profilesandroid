package ru.profiles.viewmodel

import androidx.lifecycle.ViewModel
import ru.profiles.data.UserRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val mUserRep: UserRepository):  ViewModel() {


}
