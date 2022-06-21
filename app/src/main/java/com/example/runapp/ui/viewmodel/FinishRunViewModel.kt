package com.example.runapp.ui.viewmodel

import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.example.runapp.repository.FinishRunRepository

class FinishRunViewModel(private val finishRunRepository: FinishRunRepository) : ViewModel() {

    fun checkIfIsDayOrNight(editText: EditText) {
        finishRunRepository.checkIfIsDayOrNight(editText)
    }

    fun setBackgroundImg(string: String, imageView: ImageView){
        finishRunRepository.setBackgroundImg(string, imageView)
    }



}