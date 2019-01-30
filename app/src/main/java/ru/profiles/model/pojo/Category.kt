package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Category(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("title")
    val title: String,
    @Expose
    @SerializedName("children")
    val children: List<Children>
) {
    data class Children(
        @Expose
        @SerializedName("id")
        val id: Int,
        @Expose
        @SerializedName("title")
        val title: String,
        @Expose
        @SerializedName("children")
        val children: List<Any>
    )
}