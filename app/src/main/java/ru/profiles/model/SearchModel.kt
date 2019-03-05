package ru.profiles.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose


@Entity(
    tableName = "search",
    indices = [Index("id"), Index("searchString"), Index("searchTarget")]
)
data class SearchModel(
    var searchString: String? = null,
    var searchTarget: String? = null

){
    @PrimaryKey(autoGenerate = true)
    @Expose
    var id: Long = 0
}