package ru.profiles.data

import ru.profiles.api.interfaces.AuthApi
import ru.profiles.dao.UserModelDao
import ru.profiles.model.UserModel

class UserRepository(val mUserDao: UserModelDao, val mWebApi: AuthApi ) {

    fun getLastLoggedUser() : UserModel?{
        return mUserDao.getLastLoggedUser()
    }

    fun saveUser(user: UserModel) = mUserDao.save(user)

}