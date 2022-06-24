package com.example.runapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.runapp.model.RunModelFinal
import com.example.runapp.network.RetrofitInstance
import com.example.runapp.other.AppUtilities
import com.example.runapp.ui.RunActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FinishRunRepository {
    @SuppressLint("SetTextI18n")
    fun checkIfIsDayOrNight(editTextDayNight: EditText) {
        val hora = Date().hours
        if (hora in 18..23) {
            editTextDayNight.setText("Noite")
        } else {
            editTextDayNight.setText("Dia")
        }
    }

    fun setBackgroundImg(string: String, img: ImageView) {
        AppUtilities.changeBackground(string, img)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        return dateFormat.format(date)
    }

    fun saveIntoDatabase(
        runModel: RunModelFinal,
        context: Context,
        btnFinalizar: FloatingActionButton,
        progressBar: ProgressBar
    ) {
        RetrofitInstance.getRetrofit().addNewRun(runModel)
            .enqueue(object : Callback<RunModelFinal> {
                override fun onResponse(
                    call: Call<RunModelFinal>,
                    response: Response<RunModelFinal>
                ) {
                    context.startActivity(Intent(context, RunActivity::class.java))
                    btnFinalizar.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Corrida salva no banco de dados", Toast.LENGTH_LONG)
                        .show()
                    Log.d("TAG", runModel.toString())
                }

                override fun onFailure(call: Call<RunModelFinal>, t: Throwable) {
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