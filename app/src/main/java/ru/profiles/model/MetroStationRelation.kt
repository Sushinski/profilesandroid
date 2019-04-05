package ru.profiles.model

import androidx.room.Embedded
import androidx.room.Relation

class MetroStationRelation {

    @Embedded
    var locationMetro: LocationMetroStation? = null

    @Relation(parentColumn = "metroStationId", entityColumn = "id", entity = MetroStationModel::class)
    var stations: List<MetroStationModel>? = null
}