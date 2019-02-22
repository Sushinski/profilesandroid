package ru.profiles.model

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.profiles.model.CityModel

@Entity(
    foreignKeys = [
        ForeignKey(entity = CityModel::class, parentColumns = arrayOf("id"),
            childColumns = arrayOf("city_id"),
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("id"), Index("name"), Index("city_id")]
)
data class MetroStationModel(
    @PrimaryKey
    @Expose
    @SerializedName("id")
    val id: Long,
    val city_id: Long,
    @Expose
    @SerializedName("name")
    val name: String
)