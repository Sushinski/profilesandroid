package ru.profiles.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.profiles.dao.AuthModelDao
import ru.profiles.dao.UserModelDao
import ru.profiles.model.AuthModel
import ru.profiles.model.UserModel

@Database(entities = [UserModel::class, AuthModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userModelDao(): UserModelDao
    abstract fun authModelDao(): AuthModelDao

    companion object {

        @Volatile private var mInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return mInstance ?: synchronized(this) {
                mInstance ?: buildDatabase(context).also { mInstance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "Profiles.db").build()
        }
    }
}