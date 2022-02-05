package com.visualpro.realproject.network

import com.visualpro.realproject.Model.TranslateItems
import retrofit2.http.GET
import retrofit2.http.Query

interface Gg_TranslateService {
    @GET("single")
    suspend fun translate(
        @Query(value = "client", encoded = true) clientGtx: String,
        @Query(value = "sl", encoded = true) from: String,
        @Query(value = "tl", encoded = true) to: String,
        @Query(value = "dt", encoded = true) dt:String,
        @Query(value = "dt", encoded = true) dt1:String,
        @Query(value = "dj", encoded = true) dj:Int,
        @Query(value = "q", encoded = true) query:String
    ) :TranslateItems

}