package ru.profiles.api.interfaces

import com.facebook.imagepipeline.request.ImageRequest
import io.reactivex.Observable
import retrofit2.http.*
import ru.profiles.model.pojo.*

interface RegistrationApi {

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST("/api/v2/user_register/")
    fun registerUser(
        @Body reg_request: RegistrationRequest
    ): Observable<RegistrationResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST("/api/v2/sms/send_code/")
    fun requestSms(
        @Body sms_request: SmsRequest
    )

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @GET("/api/v2/users/profile/")
    fun getUserInfo(
        @QueryMap fields: Map<String, String>
    ): Observable<UserInfoResponse>


    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @PUT("/api/v2/users/profile/")
    fun updateUserInfo(
        request: UserInfoRequest
    ): Observable<Unit>

}