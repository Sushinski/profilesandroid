package ru.profiles.model

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
    @SerializedName("id")
    @Expose
    var origId: Long? = null,
    @Expose
    @SerializedName("name")
    var name: String? = null
){
    companion object {
        const val UNDEFINED = "Не указан"
    }
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}