package ru.profiles.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.profiles.model.UserModel

@Dao
interface UserModelDao {

    @Insert(onConflict = REPLACE)
    fun save(user: UserModel)

    @Query("SELECT * FROM users WHERE is_logged = 1 LIMIT 1")
    fun getLoggedUser() : LiveData<UserModel>

    @Query("SELECT * FROM users WHERE is_logged = 0 LIMIT 1")
    fun getRegisteredUser() : LiveData<UserModel>

    @Update
    fun update(user: UserModel)

    @Query("DELETE FROM users WHERE is_logged = :is_logged")
    fun clearUser(is_logged: Boolean)
}