package ru.profiles.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(
        tableName = "organization_area",
        indices = [ Index("organizationId"),
            Index("areaId"),
            Index(value = ["organizationId", "areaId"], unique = true) ],
        foreignKeys = [
        ForeignKey(entity = OrganizationModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("organizationId"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = AreaModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("areaId"),
            onDelete = ForeignKey.CASCADE)
        ]
)
data class OrganizationArea(
    var organizationId: Long,
    var areaId: Long
) {

    @PrimaryKey(autoGenerate = true)
    @Expose
    var id: Long = 0
}