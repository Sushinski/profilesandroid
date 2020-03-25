package ru.profiles.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.dao.UserModelDao
import ru.profiles.interfaces.UserRepositoryOps
import ru.profiles.model.UserModel

class UserRepository private constructor(private val mUserDao: UserModelDao,
                                         private val mAuthApi: AuthApi ) : UserRepositoryOps {

    companion object {
        @Volatile private var instance: UserRepository? = null

        fun getInstance(userDao: UserModelDao,
                        authApi: AuthApi ) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userDao, authApi).also { instance = it }
            }
    }

    override fun getLoggedUser() : LiveData<UserModel> {
        return mUserDao.getLoggedUser()
    }

    override fun getRegisteredUser() : LiveData<UserModel>{
        return mUserDao.getRegisteredUser()
    }

    override suspend fun saveUser(user: UserModel){
        withContext(IO) {
            mUserDao.save(user)
        }
    }

    override suspend fun deleteRegisteredUser(){
        withContext(IO){
            mUserDao.clearUser(false)
        }
    }

    override suspend fun deleteLoggedUser(){
        withContext(IO){
            mUserDao.clearUser(true)
        }
    }

}