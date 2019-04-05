package ru.profiles.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "metro_station",
    indices = [Index("id"), Index("name"), Index("origId")]
)
data class MetroStationModel(
    @Expose
    @SerializedName("id")
    @ColumnInfo(name="origId")
    var origId: Long? = null,
    @Expose
    @ColumnInfo(name="name")
    @SerializedName("name")
    var name: String? = null
){

    companion object {
        const val UNDEFINED = "Не указана"
    }
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}