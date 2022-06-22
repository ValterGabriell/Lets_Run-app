package com.example.runapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.runapp.model.RunModelFinal
import com.example.runapp.repository.ListRunRepository

class ListRunViewModel(private val repository: ListRunRepository) :
    ViewModel() {

    val listRuns = MutableLiveData<List<RunModelFinal>>()

   suspend fun getAllRuns(userId: String) {
        repository.getAllList(userId, listRuns)
    }


}