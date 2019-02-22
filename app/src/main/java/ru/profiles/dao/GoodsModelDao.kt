package ru.profiles.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ru.profiles.model.GoodsModel

@Dao
interface GoodsModelDao {

    @Query("SELECT * FROM goods")
    fun getPopularGoods(): LiveData<List<GoodsModel>>
}