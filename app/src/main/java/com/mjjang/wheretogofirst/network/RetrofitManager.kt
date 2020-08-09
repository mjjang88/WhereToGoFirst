package com.mjjang.wheretogofirst.network

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mjjang.wheretogofirst.data.Place
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {

    fun getService(): RetrofitService = retrofit.create(RetrofitService::class.java)

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

    data class KakaoSearchPlaceResult(
        @SerializedName("documents")
        @Expose
        val documents: List<Place>
    )
}