package com.example.runapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.runapp.databinding.ActivityListRunBinding
import com.example.runapp.ui.viewmodel.ListRunViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ListRunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListRunBinding
    private val viewModel by inject<ListRunViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var acct = GoogleSignIn.getLastSignedInAccount(this)

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getAllRuns(acct?.id!!)
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.listRuns.observe(this@ListRunActivity) {
                    Log.d("TAG: ", it.toString())
                }
            }

        }

    }
}