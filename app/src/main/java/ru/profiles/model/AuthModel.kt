package ru.profiles.model

import androidx.room.*

@Entity(
    tableName = "auth",
    indices = [Index("id")]
)
data class AuthModel(
    @ColumnInfo(name="jwt_token") val mJwtToken: String,
    @ColumnInfo(name="refresh_token") val mRefreshToken: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var mId: Long = 0
}