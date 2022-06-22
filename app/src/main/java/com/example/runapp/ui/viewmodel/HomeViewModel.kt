package com.example.runapp.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.example.runapp.repository.HomeRepository
import com.google.android.gms.maps.GoogleMap
import java.util.*

class HomeViewModel(private val homeRepository: HomeRepository):ViewModel() {

    fun changeStyleMap(style: Boolean, context: Context, map: GoogleMap?) {
        homeRepository.changeStyleMap(style, context, map)
    }

    suspend fun readMapData(key: String, dataStore: DataStore<Preferences>): Boolean? {
        return homeRepository.read(key, dataStore)
    }

    fun encodeBitmapToString(bitmap: Bitmap):String?{
        return homeRepository.convertBitmapToString(bitmap)
    }

}