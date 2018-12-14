package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthRequest(identity: String, pswd: String) {

    @SerializedName("identity")
    @Expose
    var mIdentity: String = identity

    @SerializedName("password")
    @Expose
    var mPassword: String = pswd
}