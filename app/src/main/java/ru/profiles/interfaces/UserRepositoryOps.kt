package ru.profiles.interfaces

import androidx.lifecycle.LiveData
import ru.profiles.model.UserModel

interface UserRepositoryOps {

    fun getLoggedUser() : LiveData<UserModel>

    fun getRegisteredUser() : LiveData<UserModel>

    suspend fun saveUser(user: UserModel)

    suspend fun deleteRegisteredUser()

    suspend fun deleteLoggedUser()
}