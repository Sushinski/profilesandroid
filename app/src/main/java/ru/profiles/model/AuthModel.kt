package ru.profiles.model

import android.util.Log
import androidx.room.*

@Entity(
    tableName = "auth",
    indices = [Index("id")]
)
data class AuthModel(
    val _jwtToken: String,
    @ColumnInfo(name="refresh_token")
    var mRefreshToken: String
) {

    @ColumnInfo(name="jwt_token")
    var mJwtToken: String = _jwtToken
        set(value){
            field = "Bearer $value"
            Log.i("ProfilesInfo", "updated jwt $field")
        }


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var mId: Long = 0
}