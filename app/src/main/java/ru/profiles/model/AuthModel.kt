package ru.profiles.model

import androidx.room.*

@Entity(
    tableName = "auth",
    indices = [Index("id"), Index("user_id")],
    foreignKeys = [ForeignKey(
        entity = UserModel::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE,
        deferred = true
    )]
)
data class AuthModel(
    @ColumnInfo(name="jwt_token") val mJwtToken: String,
    @ColumnInfo(name="refresh_token") val mRefreshToken: String,
    @ColumnInfo(name = "user_id") val mUserId: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var mId: Long = 0
}