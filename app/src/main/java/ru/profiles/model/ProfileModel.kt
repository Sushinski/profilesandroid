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
    var isSpecialist: Boolean = false,
    @Expose
    @SerializedName("is_subscribed")
    var isSubscribed: Boolean = false,
    @Expose
    @SerializedName("slug")
    var slug: String = "",
    @Expose
    @SerializedName("first_name")
    var firstName: String = "",
    @Expose
    @SerializedName("display_name")
    var displayName: String = "",
    @Expose
    @SerializedName("photo")
    @Ignore
    var photo: PhotoModel? = null, // todo foreign key
    @Expose
    @SerializedName("gender")
    var gender: Int = 0,
    @Expose
    @SerializedName("display_marital_status")
    var displayMaritalStatus: String = "",
    @Expose
    @SerializedName("display_birthdate")
    var displayBirthdate: String = "",
    @Expose
    @SerializedName("birth_date_view")
    var birthDateView: Int = 0,
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