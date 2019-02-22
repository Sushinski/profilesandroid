package ru.profiles.model

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.profiles.model.SpecializationModel


@Entity(
    tableName = "specialization_prefs",
    foreignKeys = [
        ForeignKey(
            entity = SpecializationModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("spec_id"),
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("id"), Index( "spec_id")]
)
data class SpecializationPrefsModel(
    @Expose
    @SerializedName("specialization")
    @Ignore
    var specialization: SpecializationModel? = null,
    @Expose
    @SerializedName("is_general")
    var isGeneral: Boolean = false,
    @Expose
    @SerializedName("is_looking_for_job")
    var isLookingForJob: Boolean = false
){
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    var id: Long = 0

    var spec_id: Long = specialization?.id ?: 0
}