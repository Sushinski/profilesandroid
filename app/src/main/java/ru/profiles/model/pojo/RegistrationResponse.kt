package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegistrationResponse (
    @SerializedName("token")
    @Expose
    var mToken: String = "",

    @SerializedName("refresh_token")
    @Expose
    var mRefreshToken: String = ""
)