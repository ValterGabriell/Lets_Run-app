package com.example.runapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.runapp.model.RunModel
import com.example.runapp.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListRunRepository {

    fun getAllList(userId: String, listRuns: MutableLiveData<List<RunModel>>) {
        RetrofitInstance.getRetrofit().getAllRun(userId).enqueue(object : Callback<List<RunModel>> {
            override fun onResponse(
                call: Call<List<RunModel>>,
                response: Response<List<RunModel>>
            ) {
                Log.d("TAG", response.code().toString())
                Log.d("TAG", response.message())
                Log.d("TAG", response.body().toString())
                Log.d("TAG", userId)
            }

            override fun onFailure(call: Call<List<RunModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

}