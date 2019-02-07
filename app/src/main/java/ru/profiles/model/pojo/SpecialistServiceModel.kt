package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SpecialistServiceModel(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("service_type")
    val serviceType: Int,
    @Expose
    @SerializedName("title")
    val title: String,
    @Expose
    @SerializedName("description")
    val description: String,
    @Expose
    @SerializedName("members_count")
    val membersCount: Int,
    @Expose
    @SerializedName("locations")
    val locations: List<Location>,
    @Expose
    @SerializedName("is_published")
    val isPublished: Boolean,
    @Expose
    @SerializedName("created")
    val created: String,
    @Expose
    @SerializedName("profile")
    val profile: Profile,
    @Expose
    @SerializedName("duration")
    val duration: Int,
    @Expose
    @SerializedName("address")
    val address: String,
    @Expose
    @SerializedName("cost")
    val cost: String,
    @Expose
    @SerializedName("cost_to")
    val costTo: String,
    @Expose
    @SerializedName("cost_type")
    val costType: Int,
    @Expose
    @SerializedName("unit")
    val unit: String,
    @Expose
    @SerializedName("files")
    val files: List<Any>,
    @Expose
    @SerializedName("regular_schedule")
    val regularSchedule: List<Any>,
    @Expose
    @SerializedName("irregular_schedule")
    val irregularSchedule: List<Any>,
    @Expose
    @SerializedName("ratings")
    val ratings: Ratings,
    @Expose
    @SerializedName("categories")
    val categories: List<Category>
) {
    data class Ratings(
        @Expose
        @SerializedName("rating")
        val rating: String,
        @Expose
        @SerializedName("reviews")
        val reviews: Int
    )
}