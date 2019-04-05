package ru.profiles.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.profiles.model.CityModel
import ru.profiles.model.MetroStationModel

@Entity(
    tableName = "location",
    indices = [Index("id")]
)
data class LocationModel(
    @Expose
    @Ignore
    @SerializedName("country")
    var country: CountryModel? = null,
    @Expose
    @Ignore
    @SerializedName("city")
    var city: CityModel? = null,
    @Expose
    @Ignore
    @SerializedName("metro_stations")
    var metroStations: List<MetroStationModel>? = null,
    @Expose
    @SerializedName("street")
    var street: String? = null,
    @Expose
    @SerializedName("building")
    var building: String? = null,
    @Expose
    @SerializedName("block")
    var block: String? = null,
    @Expose
    @SerializedName("office")
    var office: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var cityId: Long? = null
    var countryId: Long? = null
}