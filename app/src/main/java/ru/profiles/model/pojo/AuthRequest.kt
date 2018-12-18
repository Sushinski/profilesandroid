package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("identity")
    @Expose
    var mIdentity: String,
    @SerializedName("password")
    @Expose
    var mPassword: String
)