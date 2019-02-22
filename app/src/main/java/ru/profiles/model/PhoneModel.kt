package ru.profiles.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "phone",
    indices = [Index("id"), Index("number")]
)
data class PhoneModel(
    @PrimaryKey
    @Expose
    @SerializedName("id")
    val id: Long,
    @Expose
    @SerializedName("number")
    val number: String,
    @Expose
    @SerializedName("note")
    val note: String,
    @Expose
    @SerializedName("order")
    val order: Int,
    @Expose
    @SerializedName("is_default")
    val isDefault: Boolean
)