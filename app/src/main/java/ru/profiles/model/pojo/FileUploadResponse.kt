package ru.profiles.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FileUploadResponse (
    @Expose
    @SerializedName("id")
    var mId: String = "",

    @Expose
    @SerializedName("file_name")
    var mFileName: String? = null,

    @Expose
    @SerializedName("file_type")
    var mFileType: String? = null,

    @Expose
    @SerializedName("orig_file_name")
    var mOrigFileName: String? = null,

    @Expose
    @SerializedName("file_size")
    var mFileSize: Long = 0,

    @Expose
    @SerializedName("extra_data")
    var mOriginalSize: OriginalSize? = null

){
    data class OriginalSize(
        @Expose
        @SerializedName("width")
        var mWidth: Int = 0,
        @Expose
        @SerializedName("height")
        var mHeight: Int = 0
    )
}