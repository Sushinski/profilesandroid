package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Profile(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("is_specialist")
    val isSpecialist: Boolean,
    @Expose
    @SerializedName("is_subscribed")
    val isSubscribed: Boolean,
    @Expose
    @SerializedName("slug")
    val slug: String,
    @Expose
    @SerializedName("first_name")
    val firstName: String,
    @Expose
    @SerializedName("display_name")
    val displayName: String,
    @Expose
    @SerializedName("photo")
    val photo: Photo,
    @Expose
    @SerializedName("gender")
    val gender: Int,
    @Expose
    @SerializedName("display_marital_status")
    val displayMaritalStatus: String,
    @Expose
    @SerializedName("display_birthdate")
    val displayBirthdate: String,
    @Expose
    @SerializedName("birth_date_view")
    val birthDateView: Int,
    @Expose
    @SerializedName("city")
    val city: Any?,
    @Expose
    @SerializedName("organization")
    val organization: String,
    @Expose
    @SerializedName("ratings")
    val ratings: SpecialistServiceModel.Ratings,
    @Expose
    @SerializedName("specializations")
    val specializations: List<Specialization>
)