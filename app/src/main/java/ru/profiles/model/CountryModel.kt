package ru.profiles.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "country",
    indices = [Index("id")]
)
data class CountryModel(
    @Expose
    @SerializedName("name")
    val name: String? = null
){
    companion object {
        const val UNDEFINED = "Не указан"
    }
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}