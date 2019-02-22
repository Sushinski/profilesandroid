package ru.profiles.model

import androidx.room.ColumnInfo
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
    @Expose
    @SerializedName("common")
    val common: Int,
    @Expose
    @SerializedName("professional")
    val professional: Int
){
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    var id: Long = 0
}