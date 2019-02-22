package ru.profiles.model

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.profiles.model.pojo.Location

@Entity(
    tableName = "services",
    foreignKeys = [
        ForeignKey(
            entity = OrganizationModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("organization_id"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = ProfileModel::class,
            parentColumns = ["id"],
            childColumns = ["profile_id"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = RatingsModel::class,
            parentColumns = ["id"],
            childColumns = ["ratings_id"],
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [
        Index("id"),
        Index("organization_id"),
        Index("profile_id"),
        Index("ratings_id")
    ]
)
data class ServiceModel(
    @PrimaryKey
    @Expose
    @SerializedName("id")
    var id: Long = 0,
    @Expose
    @Ignore
    @SerializedName("organization")
    var organization: OrganizationModel? = null,
    @Expose
    @SerializedName("service_type")
    var serviceType: Int = 0,
    @Expose
    @SerializedName("title")
    var title: String = "",
    @Expose
    @SerializedName("description")
    var description: String = "",
    @Expose
    @SerializedName("members_count")
    var membersCount: Int = 0,
    @Expose
    @SerializedName("locations")
    @Ignore
    var locations: List<Location> = listOf(),// todonetomany
    @Expose
    @SerializedName("is_published")
    var isPublished: Boolean = false,
    @Expose
    @SerializedName("created")
    var created: String = "",
    @Expose
    @SerializedName("profile")
    @Ignore
    var profile: ProfileModel? = null,
    @Expose
    @SerializedName("duration")
    var duration: Int = 0,
    @Expose
    @SerializedName("address")
    var address: String = "",
    @Expose
    @SerializedName("cost")
    var cost: String = "",
    @Expose
    @SerializedName("cost_to")
    var costTo: String = "",
    @Expose
    @SerializedName("cost_type")
    var costType: Int = 0,
    @Expose
    @SerializedName("unit")
    var unit: String = "",
    @Expose
    @SerializedName("files")
    @Ignore
    var files: List<Any> = listOf(), // todo onetomany
    @Expose
    @SerializedName("regular_schedule")
    @Ignore
    var regularSchedule: List<Any> = listOf(), // todo onetomany
    @Expose
    @SerializedName("irregular_schedule")
    @Ignore
    var irregularSchedule: List<Any> = listOf(), // todo onetomany
    @Expose
    @SerializedName("ratings")
    @Ignore
    var ratings: RatingsModel? = null,
    @Expose
    @SerializedName("categories")
    @Ignore
    var categories: List<CategoryModel> = listOf() // todo onetomany
){
    var organization_id: Long = organization?.id ?: 0
    var profile_id: Long = profile?.id ?: 0
    var ratings_id: Long = ratings?.id ?: 0
}