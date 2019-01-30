package ru.profiles.api.interfaces

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap
import ru.profiles.model.pojo.SpecialistServiceModel

interface SearchApi {

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @GET("/api/v1/common/services/?")
    fun getSpecialistServices(@QueryMap params: Map<String, String>) : Observable<List<SpecialistServiceModel>>
}