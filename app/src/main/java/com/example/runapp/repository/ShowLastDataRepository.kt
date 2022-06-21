package com.example.runapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.example.runapp.db.Converter
import com.example.runapp.model.RunModel
import com.example.runapp.network.RetrofitInstance
import com.example.runapp.other.AppUtilities
import com.example.runapp.ui.RunActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ShowLastDataRepository {

    fun changeBackground(string: String, img: ImageView) {
        AppUtilities.changeBackground(string, img)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        return dateFormat.format(date)
    }

    fun saveIntoDatabase(
        runModel: RunModel,
        context: Context,
        btnFinalizar: Button,
        progressBar: ProgressBar
    ) {
        RetrofitInstance.getRetrofit().addNewRun(runModel).enqueue(object : Callback<RunModel> {
            override fun onResponse(call: Call<RunModel>, response: Response<RunModel>) {
                context.startActivity(Intent(context, RunActivity::class.java))
                btnFinalizar.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Corrida salva no banco de dados", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<RunModel>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Falha ao salvar no database ${t.cause} and ${t.message} and ${t.stackTrace}",
                    Toast.LENGTH_LONG
                ).show()
                Log.d(
                    "tag",
                    "Falha ao salvar no database ${t.cause} and ${t.message} and ${t.stackTrace}"
                )
            }
        })
    }


}