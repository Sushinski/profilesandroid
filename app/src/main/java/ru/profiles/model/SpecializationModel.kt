package ru.profiles.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "specialization",
    indices = [Index("id"), Index("name")]
)
data class SpecializationModel(
    @PrimaryKey
    @Expose
    @SerializedName("id")
    var id: Long = 0,
    @Expose
    @SerializedName("name")
    var name: String = ""
)