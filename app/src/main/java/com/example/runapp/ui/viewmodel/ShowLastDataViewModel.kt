package com.example.runapp.ui.viewmodel

import android.content.Context
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import com.example.runapp.model.RunModel
import com.example.runapp.repository.ShowLastDataRepository

class ShowLastDataViewModel(private val repository: ShowLastDataRepository) : ViewModel() {

    fun changeBackgroundImg(string: String, imageView: ImageView) {
        repository.changeBackground(string, imageView)
    }

    fun getCurrentDate(): String {
        return repository.getDate()
    }

    fun saveIntoDatabase(
        runModel: RunModel,
        context: Context,
        btnFinalizar: Button,
        progressBar: ProgressBar
    ) {
        repository.saveIntoDatabase(runModel, context, btnFinalizar, progressBar)
    }

}