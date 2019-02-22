package ru.profiles.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "photo_data",
    indices = [Index("id")]
)
data class PhotoDataModel(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: Long = 0,
    @SerializedName("file_name")
    @Expose
    var file_name: String = "",
    @SerializedName("orig_file_name")
    @Expose
    var orig_file_name: String = "",
    @SerializedName("file_size")
    @Expose
    var file_size: String = "",
    @SerializedName("file_type")
    @Expose
    var file_type: String = "",
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("alt")
    @Expose
    var alt: String  = "",
    @SerializedName("display_name")
    @Expose
    var display_name: String = "",
    @SerializedName("extra_data")
    @Expose
    @Ignore
    var extra_data: Any? = null
)