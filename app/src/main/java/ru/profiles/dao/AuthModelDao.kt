package ru.profiles.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.profiles.model.AuthModel

@Dao
interface AuthModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(auth: AuthModel)

    @Query("SELECT * FROM auth WHERE user_id = :user_id")
    fun getUserAuth(user_id: Int): LiveData<AuthModel>

    @Delete
    fun delete(auth: AuthModel)
}