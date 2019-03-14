package ru.profiles.model

import androidx.room.*
import androidx.room.ColumnInfo.NOCASE
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
        Index("title"),
        Index("organization_id"),
        Index("profile_id"),
        Index("ratings_id")
    ]
)
data class ServiceModel(
    @Expose
    @SerializedName("id")
    var orig_id: Long = 0,
    @Expose
    @Ignore
    @SerializedName("organization")
    var organization: OrganizationModel? = null,
    @Expose
    @SerializedName("service_type")
    var serviceType: Int? = null,
    @Expose
    @SerializedName("title")
    @ColumnInfo(collate = NOCASE)
    var title: String? = null,
    @Expose
    @SerializedName("description")
    var description: String? = null,
    @Expose
    @SerializedName("members_count")
    var membersCount: Int? = null,
    @Expose
    @SerializedName("locations")
    @Ignore
    var locations: List<Location>? = null,// todonetomany
    @Expose
    @SerializedName("is_published")
    var isPublished: Boolean? = null,
    @Expose
    @SerializedName("created")
    var created: String? = null,
    @Expose
    @SerializedName("profile")
    @Ignore
    var profile: ProfileModel? = null,
    @Expose
    @SerializedName("duration")
    var duration: Int? = null,
    @Expose
    @SerializedName("address")
    var address: String? = null,
    @Expose
    @SerializedName("cost")
    var cost: String? = null,
    @Expose
    @SerializedName("cost_to")
    var costTo: String? = null,
    @Expose
    @SerializedName("cost_type")
    var costType: Int? = null,
    @Expose
    @SerializedName("unit")
    var unit: String? = null,
    @Expose
    @SerializedName("files")
    @Ignore
    var files: List<Any>? = null,
    @Expose
    @SerializedName("regular_schedule")
    @Ignore
    var regularSchedule: List<Any>? = null,
    @Expose
    @SerializedName("irregular_schedule")
    @Ignore
    var irregularSchedule: List<Any>? = null,
    @Expose
    @SerializedName("ratings")
    @Ignore
    var ratings: RatingsModel? = null,
    @Expose
    @SerializedName("categories")
    @Ignore
    var categories: List<CategoryModel>? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var organization_id: Long? = null
    var profile_id: Long? = null
    var ratings_id: Long? = null
    var isOnline: Boolean = true
    // todo catgories
}