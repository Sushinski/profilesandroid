package ru.profiles.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.dao.UserModelDao
import ru.profiles.model.UserModel

class UserRepository private constructor(private val mUserDao: UserModelDao,
                                         private val mAuthApi: AuthApi ) {

    companion object {
        @Volatile private var instance: UserRepository? = null

        fun getInstance(userDao: UserModelDao,
                        authApi: AuthApi ) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userDao, authApi).also { instance = it }
            }
    }

    fun getLoggedUser() : LiveData<UserModel> {
        return mUserDao.getLoggedUser()
    }

    fun getRegisteredUser() : LiveData<UserModel>{
        return mUserDao.getRegisteredUser()
    }

    suspend fun saveUser(user: UserModel){
        withContext(IO) {
            mUserDao.save(user)
        }
    }

    suspend fun deleteRegisteredUser(){
        withContext(IO){
            mUserDao.clearUser(false)
        }
    }

    suspend fun deleteLoggedUser(){
        withContext(IO){
            mUserDao.clearUser(true)
        }
    }

}