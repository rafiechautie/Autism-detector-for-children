package com.capstoneproject.audiproject.data.remote.retrofit

import com.capstoneproject.audiproject.data.remote.response.MapsTherapyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {

    @GET("http://34.101.192.188:8080/")
    @Headers("Authorization: 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")
    fun getDataLocation(

    ): Call<MapsTherapyResponse>

}