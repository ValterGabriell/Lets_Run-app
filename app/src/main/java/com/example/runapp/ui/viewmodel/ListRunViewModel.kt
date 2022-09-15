package com.example.runapp.ui.viewmodel

import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.runapp.model.RunModelFinal
import com.example.runapp.repository.ListRunRepository
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.chip.ChipGroup

class ListRunViewModel(private val repository: ListRunRepository) :
    ViewModel() {

    val listRuns = MutableLiveData<List<RunModelFinal>>()

    suspend fun getAllRuns(
        userId: String,
        recyclerOK: RecyclerView,
        relativeLayoutErro: RelativeLayout,
        relativeLayoutEmpty: RelativeLayout,
        shimmerLayout: ShimmerFrameLayout
    ) {
        repository.getAllList(
            userId,
            listRuns,
            recyclerOK,
            relativeLayoutErro,
            relativeLayoutEmpty,
            shimmerLayout
        )
    }

    suspend fun deleteRun(
       runId:Int
    ) {
        repository.deleteRun(runId)
    }

    suspend fun deleteAllRuns() {
        repository.deleteAllRuns()
    }
}