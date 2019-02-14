package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token")
    @Expose
    var mJwtToken: String = "",

    @SerializedName("refresh_token")
    @Expose
    var mRefreshToken: String = ""
)