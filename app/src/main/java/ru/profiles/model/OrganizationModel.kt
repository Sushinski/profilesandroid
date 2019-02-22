package ru.profiles.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "organization",
    indices = [Index("id"), Index("title")]
)
data class OrganizationModel(
    @PrimaryKey
    @Expose
    @SerializedName("id")
    var id: Long = 0,
    @Expose
    @SerializedName("title")
    var title: String = "",
    @Expose
    @SerializedName("web_site")
    var webSite: String = "",
    @Expose
    @SerializedName("avatar")
    @Ignore
    var avatar: Any? = null,
    @Expose
    @SerializedName("is_owner")
    var isOwner: Boolean = false,
    @Expose
    @SerializedName("organization_type")
    var organizationType: String = "",
    @Expose
    @SerializedName("is_invited")
    var isInvited: Boolean = false,
    @Expose
    @SerializedName("is_member")
    var isMember: Boolean = false,
    @Expose
    @SerializedName("member_id")
    var memberId: Int = 0,
    @Expose
    @SerializedName("is_subscribed")
    var isSubscribed: Boolean = false,
    @Expose
    @SerializedName("ratings")
    @Ignore
    var ratings: RatingsModel? = null,
    @Expose
    @SerializedName("phone")
    @Ignore
    var phone: PhoneModel? = null,
    @Expose
    @SerializedName("areas")
    @Ignore
    var areas: List<AreaModel>? = null,
    @Expose
    @SerializedName("is_accepted")
    var isAccepted: Boolean = false,
    @Expose
    @SerializedName("slug")
    var slug: String = ""
)