package ru.profiles.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class ErrorModel {

    @Expose
    @SerializedName("internalMessage")
    val mInternalMsg: String? = null

    @Expose
    @SerializedName("userMessage")
    val mUserMessage: String? = null

    @Expose
    @SerializedName("errorCode")
    val mErrorCode: String? = null

    @Expose
    @SerializedName("fields")
    val mFields: FieldErrorObject? = null

    class FieldErrorObject {

        @Expose
        @SerializedName("rating")
        val mRating: String? = null

        @Expose
        @SerializedName("text")
        val mText: String? = null

        @Expose
        @SerializedName("non_field_errors")
        val mNonFieldErrors: String? = null

    }

}