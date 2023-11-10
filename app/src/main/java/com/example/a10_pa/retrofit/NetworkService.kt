package com.example.a10_pa.retrofit

import com.example.a10_pa.model.PageListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("/v2/top-headlines")
    fun getList(
        @Query("apiKey") apiKey: String?,
        @Query("country") country: String?
    ): retrofit2.Call<PageListModel>
}