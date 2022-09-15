package com.example.runapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runapp.R
import com.example.runapp.databinding.ActivityRunBinding
import com.example.runapp.other.AppUtilities
import com.example.runapp.other.Constantes
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class RunActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityRunBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /**
         * Confiruração do bottom navigation view
         */
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        navHostFragment.findNavController().addOnDestinationChangedListener {_, destination, _ ->
            when (destination.id) {
                R.id.id_home, R.id.id_profile -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.finishRunFragment ->{
                    binding.toolbarRun.findViewById<TextView>(R.id.txtToolbar).apply {
                        text = "Finalizando corrida"
                    }
                    binding.toolbarRun.findViewById<ImageView>(R.id.imgTool).visibility = View.GONE
                }
                else -> binding.bottomNavigationView.visibility = View.GONE
            }
        }


        /**
         * Pedindo permissoes
         */
        requestPermissions()

    }


    private fun requestPermissions() {
        if (AppUtilities.hasPermissionsAccepts(this@RunActivity)) {
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            EasyPermissions.requestPermissions(
                this@RunActivity,
                "Você precisa aceitar a permissão de localização para utlizar este aplicativo",
                Constantes.REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this@RunActivity,
                "Você precisa aceitar a permissão de localização para utlizar este aplicativo",
                Constantes.REQUEST_CODE_LOCATION_PERMISSIONS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onBackPressed() {

    }


}