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
        return mUserDao.getLastLoggedUser()
    }

    suspend fun saveLoggedUser(user: UserModel){
        withContext(IO) {
            mUserDao.clearUsers()
            mUserDao.save(user)
        }
    }

}