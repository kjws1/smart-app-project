package com.example.a10_pa

import android.app.Application
import com.example.a10_pa.retrofit.NetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    companion object {
        val API_KEY = "fd688c74f69a4df78e1118e4d2aac8f1"
        val BASE_URL = "https://newsapi.org/"

        //add....................................
        var networkService: NetworkService
        val retrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        init {
            networkService = retrofit.create(NetworkService::class.java)
        }
    }
}
