package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfoRequest(
    @SerializedName("id")
    @Expose
    var mId: Long,
    @SerializedName("user")
    @Expose
    var mUser: Long,
    @SerializedName("first_name")
    @Expose
    var mFirstName: String,
    @SerializedName("last_name")
    @Expose
    var mLastName: String,
    @SerializedName("gender")
    @Expose
    var mGender: Int
)