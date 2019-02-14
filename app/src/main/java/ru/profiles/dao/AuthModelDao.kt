package ru.profiles.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.*
import io.reactivex.Observable
import ru.profiles.model.AuthModel

@Dao
interface AuthModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAuth(auth: AuthModel)

    @Query("SELECT * FROM auth ORDER BY id LIMIT 1")
    fun getUserAuth(): Observable<AuthModel?>

    @Update
    fun updateAuth(auth: AuthModel)

    @Delete
    fun delete(auth: AuthModel)
}