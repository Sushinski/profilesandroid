package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PhotoData(
    @SerializedName("id")
    @Expose
    var mId: String,
    @SerializedName("file_name")
    @Expose
    var mFileName: String,
    @SerializedName("orig_file_name")
    @Expose
    var mOrigFileName: String,
    @SerializedName("file_size")
    @Expose
    var mFileSize: String,
    @SerializedName("file_type")
    @Expose
    var mFileType: String,
    @SerializedName("title")
    @Expose
    var mTitle: String,
    @SerializedName("alt")
    @Expose
    var mAlt: String,
    @SerializedName("display_name")
    @Expose
    var mDisplayName: String,
    @SerializedName("extra_data")
    @Expose
    var mExtraData: Any?
)