package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.profiles.model.PhotoModel

data class PhotoFile (
    @SerializedName("file")
    @Expose
    val mFile: PhotoModel
)