package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Specialization(
    @Expose
    @SerializedName("specialization")
    val specialization: Specialization,
    @Expose
    @SerializedName("is_general")
    val isGeneral: Boolean,
    @Expose
    @SerializedName("is_looking_for_job")
    val isLookingForJob: Boolean
) {
    data class Specialization(
        @Expose
        @SerializedName("id")
        val id: Int,
        @Expose
        @SerializedName("name")
        val name: String
    )
}