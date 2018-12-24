package ru.profiles.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.profiles.model.AuthModel

@Dao
interface AuthModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(auth: AuthModel)

    @Query("SELECT * FROM auth ORDER BY id LIMIT 1")
    fun getUserAuth(): LiveData<AuthModel>

    @Delete
    fun delete(auth: AuthModel)
}