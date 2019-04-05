package ru.profiles.model

import androidx.room.Embedded
import androidx.room.Relation

class ServiceLocationRelation {

    @Embedded
    var serviceLocation: ServiceLocationModel? = null

    @Relation(parentColumn = "locationId", entityColumn = "id", entity = LocationModel::class)
    var serviceLocations: List<LocationWithRelations>? = null
}