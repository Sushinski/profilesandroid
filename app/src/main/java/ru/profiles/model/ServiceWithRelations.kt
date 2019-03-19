package ru.profiles.model

import androidx.room.Embedded
import androidx.room.Relation

class ServiceWithRelations {
    @Embedded
    var service: ServiceModel? = null

    @Relation(parentColumn = "profile_id", entityColumn = "id", entity = ProfileModel::class)
    var profileModels: List<ProfileWithRelations>? = null

    @Relation(parentColumn = "id", entityColumn = "serviceId", entity = ServicePhotoModel::class)
    var photoModels: List<ServicePhotoRelation>? = null

}