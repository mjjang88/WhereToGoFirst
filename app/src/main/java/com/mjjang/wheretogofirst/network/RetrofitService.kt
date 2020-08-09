package com.mjjang.wheretogofirst.network

import com.mjjang.wheretogofirst.data.Place
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitService {

    @Headers("Authorization: KakaoAK 0d32d9da9b3c910fc0b6b91015428cb1")
    @GET("/v2/local/search/keyword.json")
    suspend fun getPlace(
        @Query("query") searchWord: String
    ): Response<RetrofitManager.KakaoSearchPlaceResult>

}