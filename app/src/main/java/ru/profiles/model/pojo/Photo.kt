package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Photo(
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("file_name")
    val fileName: String,
    @Expose
    @SerializedName("orig_file_name")
    val origFileName: String,
    @Expose
    @SerializedName("file_size")
    val fileSize: Int,
    @Expose
    @SerializedName("file_type")
    val fileType: String
)