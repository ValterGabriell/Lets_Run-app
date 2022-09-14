package com.example.runapp.repository

import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.runapp.model.RunModelFinal
import com.example.runapp.network.RetrofitInstance
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListRunRepository {


    fun getAllList(
        userId: String,
        listRuns: MutableLiveData<List<RunModelFinal>>,
        recyclerOk: RecyclerView,
        relativeLayoutErro: RelativeLayout,
        relativeLayoutEmpty: RelativeLayout,
        shimmerLayout: ShimmerFrameLayout
    ) {
        RetrofitInstance.getRetrofit().getAllRun(userId)
            .enqueue(object : Callback<List<RunModelFinal>> {
                override fun onResponse(
                    call: Call<List<RunModelFinal>>,
                    response: Response<List<RunModelFinal>>
                ) {
                    val lista = response.body()
                    if (lista!!.isNotEmpty()) {
                        recyclerOk.visibility = View.VISIBLE
                        shimmerLayout.apply {
                            visibility = View.GONE
                            this.stopShimmerAnimation()
                        }
                        listRuns.postValue(lista.reversed())
                    } else {
                        shimmerLayout.apply {
                            visibility = View.GONE
                            this.stopShimmerAnimation()
                        }
                        relativeLayoutEmpty.visibility = View.VISIBLE
                    }


                }

                override fun onFailure(call: Call<List<RunModelFinal>>, t: Throwable) {
                    shimmerLayout.apply {
                        visibility = View.GONE
                        this.stopShimmerAnimation()
                    }
                    relativeLayoutErro.visibility = View.VISIBLE
                }
            })
    }
}