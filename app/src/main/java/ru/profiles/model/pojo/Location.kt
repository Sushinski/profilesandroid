package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(
    @Expose
    @SerializedName("city")
    val city: City,
    @Expose
    @SerializedName("metro_stations")
    val metroStations: List<MetroStation>
)