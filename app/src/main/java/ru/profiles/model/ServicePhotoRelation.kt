package ru.profiles.model

import androidx.room.Embedded
import androidx.room.Relation

class ServicePhotoRelation {

    @Embedded
    var servicePhotos: ServicePhotoModel? = null

    @Relation(parentColumn = "photoId", entityColumn = "id", entity = PhotoModel::class)
    var photos: List<PhotoModel>? = null
}