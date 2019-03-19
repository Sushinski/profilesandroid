package ru.profiles.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "service_photo",
    indices = [
        Index("serviceId"),
        Index("photoId")
    ],
    foreignKeys = [
        ForeignKey(entity = ServiceModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("serviceId"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = PhotoModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("photoId"),
            onDelete = ForeignKey.CASCADE)
    ]
)
class ServicePhotoModel(
    val serviceId: Long,
    val photoId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}