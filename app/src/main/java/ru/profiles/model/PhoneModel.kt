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
    @Expose
    @SerializedName("id")
    val orig_id: Long? = null,
    @Expose
    @SerializedName("number")
    val number: String? = null,
    @Expose
    @SerializedName("note")
    val note: String? = null,
    @Expose
    @SerializedName("order")
    val order: Int? = null,
    @Expose
    @SerializedName("is_default")
    val isDefault: Boolean? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}