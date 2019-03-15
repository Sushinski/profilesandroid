package ru.profiles.model.pojo

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.profiles.model.CityModel
import ru.profiles.model.MetroStationModel

data class Location(
    @Expose
    @SerializedName("city")
    val city: CityModel,
    @Expose
    @SerializedName("metro_stations")
    val metroStations: List<MetroStationModel>
){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}