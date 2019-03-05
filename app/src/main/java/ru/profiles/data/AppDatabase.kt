package ru.profiles.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.profiles.dao.AuthModelDao
import ru.profiles.dao.GoodsModelDao
import ru.profiles.dao.ServicesModelDao
import ru.profiles.dao.UserModelDao
import ru.profiles.model.*

@Database(
    entities = [
        AreaModel::class,
        CityModel::class,
        CategoryModel::class,
        GoodsModel::class,
        MetroStationModel::class,
        OrganizationModel::class,
        PhoneModel::class,
        PhotoDataModel::class,
        PhotoModel::class,
        ProfileModel::class,
        RatingsModel::class,
        SpecializationModel::class,
        SpecializationPrefsModel::class,
        UserModel::class,
        AuthModel::class,
        ServiceModel::class,
        OrganizationArea::class,
        SearchModel::class
    ], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun userModelDao(): UserModelDao
    abstract fun authModelDao(): AuthModelDao
    abstract fun servicesModelDao(): ServicesModelDao
    abstract fun goodsModelDao(): GoodsModelDao

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