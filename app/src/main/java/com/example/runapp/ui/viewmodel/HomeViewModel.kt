package com.example.runapp.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.example.runapp.repository.HomeRepository
import com.google.android.gms.maps.GoogleMap

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel(){

    fun changeStyleMap(style: Boolean, context: Context, map: GoogleMap?) {
        homeRepository.changeStyleMap(style, context, map)
    }

    suspend fun readMapData(key: String, dataStore: DataStore<Preferences>): Boolean? {
        return homeRepository.read(key, dataStore)
    }


}