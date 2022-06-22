package com.example.runapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.runapp.model.RunModel
import com.example.runapp.repository.ListRunRepository

class ListRunViewModel(private val repository: ListRunRepository) :
    ViewModel() {

    val listRuns = MutableLiveData<List<RunModel>>()

    fun getAllRuns(userId: String) {
        repository.getAllList(userId, listRuns)
    }


}