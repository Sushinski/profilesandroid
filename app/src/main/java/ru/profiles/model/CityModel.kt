package ru.profiles.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "city",
    indices = [Index("id"), Index("name")]
)
data class CityModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    val mId: Long,
    @Expose
    @SerializedName("name")
    val name: String
)