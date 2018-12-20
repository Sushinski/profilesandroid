package ru.profiles.model

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "users",
    indices = [Index("id")]
)
data class UserModel(

    @SerializedName("user_id")
    @Expose
    var mUserId: Int = 0,

    @ColumnInfo(name = "name")
    @SerializedName("username")
    @Expose
    var mName: String = "",

    @ColumnInfo(name = "surname")
    var mSurname: String? = null,

    @ColumnInfo(name = "gender")
    var mGender: String? = null,

    @ColumnInfo(name = "phone")
    var mPhone: String? = null,

    @SerializedName("exp")
    @Expose
    var mExp: String = "",

    @ColumnInfo(name = "email")
    @SerializedName("email")
    @Expose
    var mEmail: String = "",

    @SerializedName("refresh_token_id")
    @Expose
    var mRefreshTokenId: Int = 0,

    @SerializedName("has_email")
    @Expose
    var mHasEmail: Boolean? = null,

    @SerializedName("is_email_verified")
    @Expose
    var mEmailVerified: Boolean? = null,

    @SerializedName("email_for_verification")
    @Expose
    var mEmailForVerification: String? = null,

    val imageUrl: String? = null
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    var mId: Int = 0

    override fun toString() = mName + " " + (mSurname ?: "")
}