package ru.profiles.model

import android.util.Log
import androidx.room.*

@Entity(
    tableName = "auth",
    indices = [Index("id")]
)
data class AuthModel(
    @ColumnInfo(name="jwt_token")
    var mJwtToken: String,
    @ColumnInfo(name="refresh_token")
    var mRefreshToken: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var mId: Long = 0
}