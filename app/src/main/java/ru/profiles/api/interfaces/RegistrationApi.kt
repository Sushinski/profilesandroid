package ru.profiles.api.interfaces

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.profiles.model.pojo.*

interface RegistrationApi  {

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST("/api/v1/user_register/")
    fun registerUser(
        @Body reg_request: RegistrationRequest
    ): Observable<RegistrationResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST("/api/v1/sms/send_code/")
    fun requestSms(
        @Body sms_request: SmsRequest
    )

}