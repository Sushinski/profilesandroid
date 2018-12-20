package ru.profiles.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class ErrorModel (

    @Expose
    @SerializedName("internalMessage")
    val mInternalMsg: String? = null,

    @Expose
    @SerializedName("userMessage")
    val mUserMessage: String? = null,

    @Expose
    @SerializedName("errorCode")
    val mErrorCode: String? = null,

    @Expose
    @SerializedName("fields")
    val mFields: FieldErrorObject? = null
){

    data class FieldErrorObject (

        @Expose
        @SerializedName("rating")
        val mRating: Array<String>? = null,

        @Expose
        @SerializedName("text")
        val mText: Array<String>? = null,

        @Expose
        @SerializedName("non_field_errors")
        val mNonFieldErrors: Array<String>? = null

    ) {
        // autogenerated
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FieldErrorObject

            if (!Arrays.equals(mRating, other.mRating)) return false
            if (!Arrays.equals(mText, other.mText)) return false
            if (!Arrays.equals(mNonFieldErrors, other.mNonFieldErrors)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = mRating?.let { Arrays.hashCode(it) } ?: 0
            result = 31 * result + (mText?.let { Arrays.hashCode(it) } ?: 0)
            result = 31 * result + (mNonFieldErrors?.let { Arrays.hashCode(it) } ?: 0)
            return result
        }
    }

}