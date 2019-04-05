package ru.profiles.model

import androidx.room.Embedded
import androidx.room.Relation


class LocationWithRelations {

    @Embedded
    var location: LocationModel? = null

    @Relation(parentColumn = "id", entityColumn = "locationId", entity = LocationMetroStation::class)
    var metroStations: List<MetroStationRelation>? = null

    @Relation(parentColumn = "cityId", entityColumn = "id", entity = CityModel::class)
    var city: List<CityModel>? = null

    @Relation(parentColumn = "countryId", entityColumn = "id", entity = CountryModel::class)
    var country: List<CountryModel>? = null
}