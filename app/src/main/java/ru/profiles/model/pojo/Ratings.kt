package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Ratings(
    @Expose
    @SerializedName("common")
    val common: Int,
    @Expose
    @SerializedName("professional")
    val professional: Int
)