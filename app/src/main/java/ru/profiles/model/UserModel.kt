package ru.profiles.model

import androidx.room.*

@Entity(
    tableName = "users",
    indices = [Index("id")]
)
data class UserModel(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "surname") val surname: String,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "email") val email: String,
    val imageUrl: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var mId: Int = 0

    override fun toString() = name
}