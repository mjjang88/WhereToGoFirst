package com.mjjang.wheretogofirst.network

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mjjang.wheretogofirst.data.OsrmWaypoint
import com.mjjang.wheretogofirst.data.Place
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {

    fun getKaKaoService(): KaKaoRetrofitService = kakaoRetrofit.create(KaKaoRetrofitService::class.java)

    private val kakaoRetrofit =
        Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

    data class KakaoSearchPlaceResult(
        @SerializedName("documents")
        @Expose
        val documents: List<Place>
    )


    fun getOsrmService(): OsrmRetrofitService = osrmRetrofit.create(OsrmRetrofitService::class.java)

    private val osrmRetrofit =
        Retrofit.Builder()
            .baseUrl("http://34.64.253.203:443")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

    data class OsrmResult(
        @SerializedName("waypoints")
        @Expose
        val waypoints: List<OsrmWaypoint>
    )
}