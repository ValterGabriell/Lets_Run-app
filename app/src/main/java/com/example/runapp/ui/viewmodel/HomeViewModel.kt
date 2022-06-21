package com.example.runapp.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.AndroidViewModel
import com.example.runapp.db.DataModel
import com.example.runapp.repository.HomeRepository
import com.google.android.gms.maps.GoogleMap

class HomeViewModel(private val homeRepository: HomeRepository, application: Application) :
    AndroidViewModel(application) {

    fun changeStyleMap(style: Boolean, context: Context, map: GoogleMap?) {
        homeRepository.changeStyleMap(style, context, map)
    }

    suspend fun readMapData(key: String, dataStore: DataStore<Preferences>): Boolean? {
        return homeRepository.read(key, dataStore)
    }

    suspend fun saveImgIntoDatabase(dataModel: DataModel) {
        homeRepository.saveImgIntoDatabase(dataModel, getApplication())
    }


}