package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.profiles.model.ServiceModel

data class ServicesResponce(
    @SerializedName("count")
    @Expose
    var count: Long = 0,
    @SerializedName("results")
    @Expose
    var result: List<ServiceModel> = listOf()
)