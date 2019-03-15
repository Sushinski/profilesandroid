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
    @Expose
    @SerializedName("id")
    var orig_id: Long? = null,
    @Expose
    @SerializedName("title")
    var title: String? = null,
    @Expose
    @SerializedName("web_site")
    var webSite: String? = null,
    @Expose
    @SerializedName("avatar")
    @Ignore
    var avatar: Any? = null,
    @Expose
    @SerializedName("is_owner")
    var isOwner: Boolean? = null,
    @Expose
    @SerializedName("organization_type")
    var organizationType: String? = null,
    @Expose
    @SerializedName("is_invited")
    var isInvited: Boolean? = null,
    @Expose
    @SerializedName("is_member")
    var isMember: Boolean? = null,
    @Expose
    @SerializedName("member_id")
    var memberId: Int? = null,
    @Expose
    @SerializedName("is_subscribed")
    var isSubscribed: Boolean? = null,
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
    var isAccepted: Boolean? = null,
    @Expose
    @SerializedName("slug")
    var slug: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var ratings_id: Long = ratings?.id ?: 0
    var phone_id: Long = phone?.id ?: 0
}