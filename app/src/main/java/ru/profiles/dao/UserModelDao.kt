package ru.profiles.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import ru.profiles.model.UserModel

@Dao
interface UserModelDao {
    @Insert(onConflict = REPLACE)
    fun save(user: UserModel)

    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
    fun getLastLoggedUser() : LiveData<UserModel>

    @Query("DELETE FROM users")
    fun clearUsers()
}