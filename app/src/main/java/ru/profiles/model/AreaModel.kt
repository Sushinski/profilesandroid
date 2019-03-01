package ru.profiles.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "area",
    indices = [Index("id")]
)
data class AreaModel(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    val id: Long = 0,
    @Expose
    @SerializedName("name")
    val name: String? = null
)