package com.example.runapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.example.runapp.model.RunModelFinal
import com.example.runapp.network.RetrofitInstance
import com.example.runapp.other.AppUtilities
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepository {

    fun getLastRun(
        context: Context,
        txtDist: MaterialTextView,
        txtKmh: MaterialTextView,
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
                    if (it?.equals(null) == false) {
                        progressBarProfile.visibility = View.GONE
                        layout.visibility = View.VISIBLE
                        layout2.visibility = View.VISIBLE
                        txtDist.text = "${AppUtilities.formatTo2DecimalHomes(it.totalDistance)} km"
                        txtKmh.text = "${AppUtilities.formatTo2DecimalHomes(it.avergedSpeed)} kmh"
                        txtTimeFinal.text = it.timRunInSeconds
                    }
                }
            }

            override fun onFailure(call: Call<RunModelFinal>, t: Throwable) {
                Toast.makeText(context, "Não foi possível recuperar a última corrida, tente novamente em alguns instantes", Toast.LENGTH_LONG).show()
                progressBarProfile.visibility = View.GONE
                layout.visibility = View.VISIBLE
                layout2.visibility = View.VISIBLE
                txtDist.text = "0 km"
                txtKmh.text = "0 kmh"
                txtTimeFinal.text = "00:00:00"
            }
        })
    }


}