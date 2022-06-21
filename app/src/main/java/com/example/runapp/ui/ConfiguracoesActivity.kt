package com.example.runapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.example.runapp.R
import com.example.runapp.databinding.ActivityConfiguracoesBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ConfiguracoesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracoesBinding
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracoesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStore = createDataStore(name = "settings")
        changeMap()
        setMapOnSettings()

        binding.toolbarSettings.findViewById<TextView>(R.id.btnTool).setOnClickListener {
            startActivity(Intent(this, RunActivity::class.java))
        }

    }

    private fun setMapOnSettings() {
        CoroutineScope(Dispatchers.Main).launch {
            if (read("MapKey") == true) {
                binding.switchMap.setChecked(true);
                Picasso.get().load(R.drawable.mapdark).into(binding.imageButton)
            } else {
                binding.switchMap.setChecked(false);
                Picasso.get().load(R.drawable.mapanormal).into(binding.imageButton)
            }
        }
    }

    private suspend fun save(key: String, value: Boolean) {
        val dataStoreKey = preferencesKey<Boolean>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): Boolean? {
        val dataStoreKey = preferencesKey<Boolean>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }


    private fun changeMap() {
        binding.switchMap.setOnCheckedChangeListener { _, bool ->
            if (bool) {
                Picasso.get().load(R.drawable.mapdark).into(binding.imageButton)
                lifecycleScope.launch {
                    save(
                        key = "MapKey",
                        value = bool
                    )
                }

            } else {
                Picasso.get().load(R.drawable.mapanormal).into(binding.imageButton)
                lifecycleScope.launch {
                    save(
                        key = "MapKey",
                        value = bool
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, RunActivity::class.java))
    }
}