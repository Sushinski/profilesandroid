package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegistrationRequest (
    @SerializedName("identity")
    @Expose
    var mIdentity: String,
    @SerializedName("password1")
    @Expose
    var mPasswd1: String,
    @SerializedName("password2")
    @Expose
    var mPasswd2: String,
    @SerializedName("sms_code")
    @Expose
    var mSmsCode: String?,
    @SerializedName("promocode")
    @Expose
    var mPromocode: String?
)
