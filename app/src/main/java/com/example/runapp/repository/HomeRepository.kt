package com.example.runapp.repository

import android.content.Context
import android.graphics.Bitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import com.example.runapp.R
import com.example.runapp.db.Converter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.coroutines.flow.first

class HomeRepository {
    fun changeStyleMap(style: Boolean, context: Context, map: GoogleMap?) {
        when (style) {
            true -> {
                map?.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        context, R.raw.style_dark
                    )
                )
            }
            false -> {
                map?.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        context, R.raw.style_standard
                    )
                )
            }
        }
    }

    suspend fun read(key: String, dataStore: DataStore<Preferences>): Boolean? {
        val dataStoreKey = preferencesKey<Boolean>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun saveImgIntoDatabase(img: Bitmap?) {
        val converter = Converter()
        val imgToByteArray = converter.toByteArray(img!!)
    }

}