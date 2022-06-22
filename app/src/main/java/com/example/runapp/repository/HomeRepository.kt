package com.example.runapp.repository

import android.content.Context
import android.graphics.Bitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import com.example.runapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import kotlinx.coroutines.flow.first
import java.io.ByteArrayOutputStream
import java.util.*

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

    fun convertBitmapToString(bitmap: Bitmap): String? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val bmp = outputStream.toByteArray()
        return Base64.getEncoder().encodeToString(bmp)
    }
}