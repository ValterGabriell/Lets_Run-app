package com.example.runapp.network

import com.example.runapp.other.Constantes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        fun getRetrofit(): RetrofitService = Retrofit.Builder()
            .baseUrl(Constantes.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitService::class.java)
    }

}