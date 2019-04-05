package ru.profiles.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "location_metro",
    indices = [
        Index("locationId"),
        Index("metroStationId")
    ],
    foreignKeys = [
        ForeignKey(entity = LocationModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("locationId"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = MetroStationModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("metroStationId"),
            onDelete = ForeignKey.CASCADE)
    ]
)
data class LocationMetroStation(
    var locationId: Long,
    var metroStationId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}