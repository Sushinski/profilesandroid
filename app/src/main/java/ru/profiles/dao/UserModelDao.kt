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

    @Update
    fun update(user: UserModel)

    @Query("DELETE FROM users")
    fun clearUsers()
}