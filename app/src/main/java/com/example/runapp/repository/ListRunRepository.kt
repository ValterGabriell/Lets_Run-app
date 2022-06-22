package com.example.runapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.runapp.model.RunModelFinal
import com.example.runapp.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListRunRepository {

    fun getAllList(userId: String, listRuns: MutableLiveData<List<RunModelFinal>>) {
        RetrofitInstance.getRetrofit().getAllRun(userId)
            .enqueue(object : Callback<List<RunModelFinal>> {
                override fun onResponse(
                    call: Call<List<RunModelFinal>>,
                    response: Response<List<RunModelFinal>>
                ) {
                    listRuns.postValue(response.body())
                }

                override fun onFailure(call: Call<List<RunModelFinal>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

}