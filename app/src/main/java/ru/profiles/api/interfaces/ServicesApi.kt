package ru.profiles.api.interfaces

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.profiles.model.pojo.ServicesResponse

interface ServicesApi {

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @GET("/api/v1/common/services/")
    fun getServices(
        @Query("page") page: Long,
        @QueryMap params: Map<String, String>,
        @Query("page_size") pageSize: Int = 20) : Observable<ServicesResponse>

}