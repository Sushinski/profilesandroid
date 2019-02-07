package ru.profiles.api.interfaces

import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*
import ru.profiles.model.pojo.FileUploadResponse

interface ResourcesApi {

    @Multipart
    @Headers("Content-Type: multipart/form-data")
    @POST("/upload/")
    fun uploadFile(@Header("Authorization") authorization: String,
                   @Part file: MultipartBody.Part): Observable<FileUploadResponse>
}