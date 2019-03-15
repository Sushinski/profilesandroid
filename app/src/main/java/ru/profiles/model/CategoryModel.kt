package ru.profiles.model

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "category",
    foreignKeys = [
        ForeignKey(entity = CategoryModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("parent_id"),
            onDelete = ForeignKey.RESTRICT)
    ],
    indices = [Index("id"), Index("parent_id"), Index("title", unique = true)]
)
data class CategoryModel(
    @Expose
    @SerializedName("id")
    var orig_id: Long? = null,
    @Expose
    @SerializedName("title")
    var title: String? = null,
    @Expose
    @SerializedName("children")
    @Ignore
    var children: List<CategoryModel>? = null

){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var parent_id: Long? = null
}