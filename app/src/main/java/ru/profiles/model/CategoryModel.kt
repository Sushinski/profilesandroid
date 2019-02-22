package ru.profiles.model

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "category",
    foreignKeys = [
        ForeignKey(entity = CategoryModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("parentId"),
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("id"), Index("parentId")]
)
data class CategoryModel(
    @PrimaryKey
    @Expose
    @SerializedName("id")
    var id: Long = 0,
    @Expose
    @SerializedName("title")
    var title: String = "",
    @Expose
    @SerializedName("children")
    @Ignore
    var children: List<CategoryModel> = listOf()

){
    var parentId: Long = 0
}