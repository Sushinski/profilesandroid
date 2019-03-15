package ru.profiles.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "photo",
    indices = [Index("id"), Index("mId")]
)
data class PhotoModel(
    @Expose
    @SerializedName("id")
    var id: String = "",
    @Expose
    @SerializedName("file_name")
    var fileName: String = "",
    @Expose
    @SerializedName("orig_file_name")
    var origFileName: String = "",
    @Expose
    @SerializedName("file_size")
    var fileSize: Int = 0,
    @Expose
    @SerializedName("file_type")
    var fileType: String = ""
){

    @PrimaryKey(autoGenerate = true)
    var mId: Long = 0
}