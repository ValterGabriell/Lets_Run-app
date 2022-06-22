package com.example.runapp.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import com.example.runapp.model.RunModel
import com.example.runapp.repository.FinishRunRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FinishRunViewModel(private val finishRunRepository: FinishRunRepository) : ViewModel() {

    suspend fun checkIfIsDayOrNight(editText: EditText) {
        finishRunRepository.checkIfIsDayOrNight(editText)
    }

    suspend fun getCurrentDate(): String {
        return finishRunRepository.getDate()
    }

    fun saveIntoDatabase(
        runModel: RunModel,
        context: Context,
        btnFinalizar: FloatingActionButton,
        progressBar: ProgressBar
    ) {
        finishRunRepository.saveIntoDatabase(runModel, context, btnFinalizar, progressBar)
    }

    suspend fun convertStringToBitmap(str:String):Bitmap?{
        return finishRunRepository.convertStringToBitmap(str)
    }




}