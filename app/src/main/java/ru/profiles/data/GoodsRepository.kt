package ru.profiles.data

import androidx.lifecycle.LiveData
import ru.profiles.dao.GoodsModelDao
import ru.profiles.model.GoodsModel

class GoodsRepository private constructor(val mGoodsDao: GoodsModelDao){

    companion object {
        @Volatile private var instance: GoodsRepository? = null

        fun getInstance(
            goodsDao: GoodsModelDao
        ) =
            instance ?: synchronized(this) {
                instance ?: GoodsRepository(goodsDao).also { instance = it }
            }
    }

    fun getPopularGoods(): LiveData<List<GoodsModel>>{
        return mGoodsDao.getPopularGoods()
    }
}