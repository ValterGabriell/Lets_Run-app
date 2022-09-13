package com.example.runapp.ui.viewmodel

import android.content.Context
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.runapp.model.RunModelFinal
import com.example.runapp.repository.ProfileRepository
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textview.MaterialTextView

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    val listRuns = MutableLiveData<List<RunModelFinal>>()

    suspend fun getAllRuns(
        userId: String,
        recyclerOK: RecyclerView,
        txtModifier:TextView
    ) {
        profileRepository.getAllList(
            userId,
            listRuns,
            recyclerOK,
            txtModifier
        )
    }

    suspend fun getLastRun(
        context: Context,
        txtDist: MaterialTextView,
        txtKmh: MaterialTextView,
        txtCal: MaterialTextView,
        txtTimeFinal: MaterialTextView,
        progressBarProfile: ProgressBar,
        layout: LinearLayout,
        layout2: LinearLayout,
        userId: String
    ) {
        profileRepository.getLastRun(
            context,
            txtDist,
            txtKmh,
            txtCal,
            txtTimeFinal,
            progressBarProfile,
            layout,
            layout2,
            userId
        )
    }

}