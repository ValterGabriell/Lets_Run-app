package com.example.runapp.ui.viewmodel

import android.content.Context
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import com.example.runapp.repository.ProfileRepository
import com.google.android.material.textview.MaterialTextView

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

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