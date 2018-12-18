package ru.profiles.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
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