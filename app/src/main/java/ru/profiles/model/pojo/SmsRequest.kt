package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SmsRequest (
    @SerializedName("phone_number")
    @Expose
    var mPhoneNumber: String
)
