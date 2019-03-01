package ru.profiles.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "profile",
    indices = [Index("id")]
)
data class ProfileModel(
    @PrimaryKey
    @Expose
    @SerializedName("id")
    var id: Long = 0,
    @Expose
    @SerializedName("is_specialist")
    var isSpecialist: Boolean? = null,
    @Expose
    @SerializedName("is_subscribed")
    var isSubscribed: Boolean? = null,
    @Expose
    @SerializedName("slug")
    var slug: String? = null,
    @Expose
    @SerializedName("first_name")
    var firstName: String? = null,
    @Expose
    @SerializedName("display_name")
    var displayName: String? = null,
    @Expose
    @SerializedName("photo")
    @Ignore
    var photo: PhotoModel? = null, // todo foreign key
    @Expose
    @SerializedName("gender")
    var gender: Int? = null,
    @Expose
    @SerializedName("display_marital_status")
    var displayMaritalStatus: String? = null,
    @Expose
    @SerializedName("display_birthdate")
    var displayBirthdate: String? = null,
    @Expose
    @SerializedName("birth_date_view")
    var birthDateView: Int? = null,
    @Expose
    @SerializedName("city")
    @Ignore
    var city: Any? = null, // todo foreign key
    @Expose
    @SerializedName("organization")
    @Ignore
    var organization: OrganizationModel? = null,
    @Expose
    @SerializedName("ratings")
    @Ignore
    var ratings: RatingsModel? = null, // todo foreign key
    @Expose
    @SerializedName("specializations")
    @Ignore
    var specializations: List<SpecializationPrefsModel>? = null // todo onetomany
){
    var organizationId: Long = organization?.id ?: 0
    var ratingsId: Long = ratings?.id ?: 0
    var photoId: Long = photo?.mId ?: 0

}