package com.example.runapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runapp.R
import com.example.runapp.databinding.ActivityListRunBinding
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRunBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configToolbar()
        val acct = GoogleSignIn.getLastSignedInAccount(this)

        binding.shimmerLayout.apply {
            this.startShimmerAnimation()
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

                    removeItemFromList()

                }
            }

        }
    }

    private fun removeItemFromList() {
        adapter.setOnClick = { runId, btnDelete, position ->
                btnDelete.setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.deleteRun(runId)
                            CoroutineScope(Dispatchers.Main).launch {
                                adapter.notifyItemRemoved(position)
                            }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btn_deleteAll ->{
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteAllRuns()
                    CoroutineScope(Dispatchers.Main).launch {
                        adapter.notifyDataSetChanged()
                        startActivity(Intent(this@ListRunActivity, RunActivity::class.java))
                        Toast.makeText(this@ListRunActivity, "Todas as corridas foram deletadas", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

    }

}