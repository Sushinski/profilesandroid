package ru.profiles.model

import androidx.room.Embedded
import androidx.room.Relation
import ru.profiles.model.PhotoModel
import ru.profiles.model.ProfileModel

class ProfileWithRelations {
    @Embedded
    var profile: ProfileModel? = null

    @Relation(parentColumn = "photoId", entityColumn = "id", entity = PhotoModel::class)
    var photoModels: List<PhotoModel>? = null
}