package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServiceModel(
    @SerializedName("id")
    @Expose
    var mId: Long
)