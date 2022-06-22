package com.example.runapp.network

import com.example.runapp.model.RunModel
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    @POST("postRun")
    fun addNewRun(@Body runModel: RunModel): Call<RunModel>

    @GET("getAll/{userId}")
    fun getAllRun(@Path("userId") userId: String): Call<List<RunModel>>

    @GET("orderByDate/{userId}")
    fun orderByDate(@Path("userId") userId: String): Call<List<RunModel>>

    @GET("orderByKm/{userId}")
    fun orderByKm(@Path("userId") userId: String): Call<List<RunModel>>

    @GET("getLastRun/{userId}")
    fun getLastRun(@Path("userId") userId: String): Call<RunModel>

    @DELETE("delete/all")
    fun deleteAll()

    @DELETE("delete/{id}")
    fun deleteById(@Path("id") id: Int)
}