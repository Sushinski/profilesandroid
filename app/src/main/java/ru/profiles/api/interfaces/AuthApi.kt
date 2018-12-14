package ru.profiles.api.interfaces

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.profiles.model.pojo.AuthRequest
import ru.profiles.model.pojo.AuthResponse

interface AuthApi {

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST("/api/v1/login/")
    fun authorizeUser(
        @Body auth_request: AuthRequest
    ): Observable<AuthResponse>


    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST("/api/v1/delegate/")
    fun updateAuth(
    ): Observable<String>
}