package ru.profiles.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "ratings",
    indices = [Index("id")]
)
data class RatingsModel(

    @SerializedName("id")
    @Expose
    var orig_id: Long? = null,
    @Expose
    @SerializedName("common")
    val common: Int? = null,
    @Expose
    @SerializedName("professional")
    val professional: Int? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}