package com.example.runapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.runapp.model.RunModelFinal
import com.example.runapp.network.RetrofitInstance
import com.example.runapp.other.AppUtilities
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepository {


    fun getAllList(
        userId: String,
        listRuns: MutableLiveData<List<RunModelFinal>>,
        recyclerView: RecyclerView,
        txtModifier : TextView
    ) {
        RetrofitInstance.getRetrofit().getAllRun(userId)
            .enqueue(object : Callback<List<RunModelFinal>> {
                override fun onResponse(
                    call: Call<List<RunModelFinal>>,
                    response: Response<List<RunModelFinal>>
                ) {
                    val lista = response.body()
                    if (lista!!.isNotEmpty()) {
                        recyclerView.visibility = View.VISIBLE
                        listRuns.postValue(lista.reversed())
                        txtModifier.apply {
                            text = "Ver todas as corridas"
                            isClickable = true
                        }

                    } else {
                       txtModifier.apply {
                           text = "Você não possui nenhuma corrida salva!"
                           isClickable = false
                       }
                    }


                }

                override fun onFailure(call: Call<List<RunModelFinal>>, t: Throwable) {
                    txtModifier.apply {
                        text = "Falha ao fazer requisição, tente novamente!"
                        isClickable = false
                    }
                }
            })
    }

    fun getLastRun(
        context: Context,
        txtDist: MaterialTextView,
        txtKmh: MaterialTextView,
        txtCal: MaterialTextView,
        txtTimeFinal: MaterialTextView,
        progressBarProfile: ProgressBar,
        layout: LinearLayout,
        layout2: LinearLayout,
        userId: String
    ) {
        RetrofitInstance.getRetrofit().getLastRun(userId).enqueue(object : Callback<RunModelFinal> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<RunModelFinal>, response: Response<RunModelFinal>) {
                response.body().let {
                    if (it?.userId?.isNotEmpty() == true && it?.userId?.isNotBlank() == true) {
                        progressBarProfile.visibility = View.GONE
                        layout.visibility = View.VISIBLE
                        layout2.visibility = View.VISIBLE
                        txtDist.text = "${AppUtilities.formatTo2DecimalHomes(it.totalDistance)} km"
                        txtKmh.text = "${AppUtilities.formatTo2DecimalHomes(it.avergedSpeed)} kmh"
                        txtTimeFinal.text = it.timRunInSeconds
                    } else {
                        progressBarProfile.visibility = View.GONE
                        layout.visibility = View.VISIBLE
                        layout2.visibility = View.VISIBLE
                        txtDist.visibility = View.GONE
                        txtKmh.visibility = View.GONE
                        txtCal.visibility = View.GONE
                        txtTimeFinal.text = "Você ainda não possui corridas salvas"
                    }
                }
            }

            override fun onFailure(call: Call<RunModelFinal>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Não foi possível recuperar a última corrida, tente novamente em alguns instantes",
                    Toast.LENGTH_SHORT
                ).show()
                progressBarProfile.visibility = View.GONE
                layout.visibility = View.VISIBLE
                layout2.visibility = View.VISIBLE
                txtDist.visibility = View.GONE
                txtKmh.visibility = View.GONE
                txtCal.visibility = View.GONE
                txtTimeFinal.text = "Falha ao recuperar corrida, tente novamente"
            }
        })
    }


}