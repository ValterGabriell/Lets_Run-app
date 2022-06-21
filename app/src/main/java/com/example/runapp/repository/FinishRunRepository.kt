package com.example.runapp.repository

import android.annotation.SuppressLint
import android.widget.EditText
import android.widget.ImageView
import com.example.runapp.other.AppUtilities
import java.util.*

class FinishRunRepository {
    @SuppressLint("SetTextI18n")
    fun checkIfIsDayOrNight(editTextDayNight: EditText) {
        val hora = Date().time
        if (hora in 18..23) {
            editTextDayNight.setText("Noite")
        } else{
            editTextDayNight.setText("Dia")
        }
    }

    fun setBackgroundImg(string: String, img: ImageView) {
        AppUtilities.changeBackground(string, img)
    }

}