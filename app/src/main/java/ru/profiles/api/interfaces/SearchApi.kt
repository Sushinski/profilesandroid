package ru.profiles.api.interfaces

import retrofit2.http.GET
import retrofit2.http.Headers

interface SearchApi {

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @GET("/api/v1/common/services/?")
    fun getServices()
}