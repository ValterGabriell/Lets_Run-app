package com.example.runapp.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runapp.R
import com.example.runapp.databinding.ActivityListRunBinding
import com.example.runapp.model.FilterItem
import com.example.runapp.model.RunModelFinal
import com.example.runapp.ui.adapter.RunAdapter
import com.example.runapp.ui.viewmodel.ListRunViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ListRunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListRunBinding
    private val viewModel by inject<ListRunViewModel>()
    private lateinit var adapter: RunAdapter
    private var lista = ArrayList<RunModelFinal>()


    private var filters = arrayOf(
        FilterItem(1, "Data"),
        FilterItem(2, "KM"),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRunBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configToolbar()
        val acct = GoogleSignIn.getLastSignedInAccount(this)

        binding.shimmerLayout.apply {
            this.startShimmerAnimation()
        }

        binding.let {
            filters.forEach { filterItem ->
                it.chipFilter.addView(filterItem.toChip(this))
            }
        }


        binding.relativeErro.findViewById<Button>(R.id.btnBackErro).setOnClickListener {
            finish()
        }

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getAllRuns(
                acct?.id!!,
                binding.recyclerView,
                binding.relativeErro,
                binding.relativeEmpty,
                binding.shimmerLayout
            )
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.listRuns.observe(this@ListRunActivity) {
                    lista = it as ArrayList<RunModelFinal>
                    adapter = RunAdapter(lista)
                    binding.recyclerView.adapter = adapter
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@ListRunActivity)
                }
            }

        }
    }

    private fun configToolbar() {
        supportActionBar?.apply {
            title = "Lista de corridas"
            setDisplayHomeAsUpEnabled(true)
        }
    }

}