package com.example.runapp.network

import com.example.runapp.model.RunModel
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    @POST("postRun")
    fun addNewRun(@Body runModel: RunModel): Call<RunModel>

    @GET("getAll")
    fun getAllRun(): Call<List<RunModel>>

    @GET("orderByDate")
    fun orderByDate(): Call<List<RunModel>>

    @GET("orderByKm")
    fun orderByKm(): Call<List<RunModel>>

    @GET("getLastRun")
    fun getLastRun(): Call<RunModel>

    @DELETE("delete/all")
    fun deleteAll()

    @DELETE("delete/{id}")
    fun deleteById(@Path("id") id: Int)
}