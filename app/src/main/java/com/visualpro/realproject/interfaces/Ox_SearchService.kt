package com.visualpro.realproject.interfaces

import com.visualpro.realproject.repositories.network.submodel.SearchItemResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface Ox_SearchService {
//
    @GET(value = "autocomplete/english/")
     suspend fun search(@Query(value = "q",encoded = true) q:String, @Query (value = "contentType", encoded = true) content:String ): SearchItemResponse
}