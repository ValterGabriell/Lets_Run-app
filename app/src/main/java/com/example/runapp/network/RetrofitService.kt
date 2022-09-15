package com.example.runapp.network

import com.example.runapp.model.RunModelFinal
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    @POST("postRun")
    fun addNewRun(@Body runModel: RunModelFinal): Call<RunModelFinal>

    @GET("getAll/{userId}")
    fun getAllRun(@Path("userId") userId: String): Call<List<RunModelFinal>>

    @GET("orderByDate/{userId}")
    fun orderByDate(@Path("userId") userId: String): Call<List<RunModelFinal>>

    @GET("orderByKm/{userId}")
    fun orderByKm(@Path("userId") userId: String): Call<List<RunModelFinal>>

    @GET("getLastRun/{userId}")
    fun getLastRun(@Path("userId") userId: String): Call<RunModelFinal>

    @DELETE("delete/all")
    fun deleteAll() : Call<String>

    @DELETE("delete/{id}")
    fun deleteById(@Path("id") id: Int) : Call<String>
}