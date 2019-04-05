package ru.profiles.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "service_location",
    indices = [
        Index("serviceId"),
        Index("locationId")
    ],
    foreignKeys = [
        ForeignKey(entity = ServiceModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("serviceId"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = LocationModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("locationId"),
            onDelete = ForeignKey.CASCADE)
    ]
)
data class ServiceLocationModel(
    val serviceId: Long,
    val locationId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}