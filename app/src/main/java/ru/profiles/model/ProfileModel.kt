package ru.profiles.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "profile",
    indices = [Index("id"), Index("photoId")],
    foreignKeys = [
            androidx.room.ForeignKey(
                entity = ru.profiles.model.PhotoModel::class,
                parentColumns = kotlin.arrayOf("id"),
                childColumns = kotlin.arrayOf("photoId"),
                onDelete = androidx.room.ForeignKey.SET_NULL
            )
    ]
)
data class ProfileModel(
    @Expose
    @SerializedName("id")
    var orig_id: Long? = null,
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
    var photo: PhotoModel? = null,
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
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var organizationId: Long? = null
    var ratingsId: Long? = null
    var photoId: Long? = null

}