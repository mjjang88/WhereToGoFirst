package com.mjjang.wheretogofirst.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OsrmRetrofitService {

    @GET("/trip/v1/driving/{location}?roundtrip=false&source=first&destination=last")
    suspend fun getRoute(
        @Path ("location") location: String
    ): Response<RetrofitManager.OsrmResult>
}