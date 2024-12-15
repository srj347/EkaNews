package com.example.core.network.api

import com.example.core.network.model.NewsResponse
import com.example.core.network.util.NetworkConstants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String = "us",
        @Query("apiKey")
        apiKey:String = API_KEY
    ): NewsResponse

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        searchQuery:String,
        @Query("apiKey")
        apiKey:String = API_KEY
    ): NewsResponse

}