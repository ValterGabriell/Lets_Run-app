package com.example.runapp.ui.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.runapp.R
import com.example.runapp.databinding.FragmentHomeBinding
import com.example.runapp.other.AppUtilities
import com.example.runapp.other.Constantes
import com.example.runapp.service.Polyline
import com.example.runapp.service.TrackingService
import com.example.runapp.ui.ConfiguracoesActivity
import com.example.runapp.ui.animUI.Anim
import com.example.runapp.ui.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class HomeFragment : Fragment() {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var binding: FragmentHomeBinding
    private var map: GoogleMap? = null
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var currentTimeMillis = 0L
    private var distanceKm = 0.00
    private var speedOfRun = 0
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel by inject<HomeViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        dataStore = requireContext().createDataStore(name = "settings")
        fusedLocationProviderClient = FusedLocationProviderClient(requireContext())
        binding.mapView.getMapAsync {
            map = it
            CoroutineScope(Dispatchers.Main).launch {
                val styleMap = viewModel.readMapData("MapKey", dataStore)
                styleMap?.let { it -> changeStyleMap(it, requireContext(), map) }
            }

            addAllPolylines()
            getStartLocation()
        }

        binding.btnPause.visibility = View.GONE
        binding.btnFinish.visibility = View.GONE
        subscribeToObserve()

        binding.btnSong.setOnClickListener {
            startActivity(Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(requireActivity(), ConfiguracoesActivity::class.java))
        }


        binding.btnFinish.setOnActiveListener {
            CoroutineScope(Dispatchers.Main).launch {
                zoomToSeeWholeTrack()
                changeThisFragmentToFinishRunFragment()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    sendCommandToService(Constantes.ACTION_STOP_SERVICE)
                }


            }
        }

        binding.btnPause.setOnClickListener {
            sendCommandToService(Constantes.ACTION_PAUSE_SERVICE)
            binding.btnContinue.visibility = View.VISIBLE
            binding.btnPause.visibility = View.GONE
        }


        continueRun(binding.btnContinue, binding.btnPause)


        binding.btnEclude.setOnClickListener {
            binding.btnStartRun.text = "INICIAR"
            sendCommandToService(Constantes.ACTION_STOP_SERVICE)
            activity?.findViewById<Toolbar>(R.id.toolbarRun)?.visibility = View.VISIBLE
            binding.toolbarHome.visibility = View.GONE
            binding.linearLayout.visibility = View.GONE
            Anim.fadeInOrOut(binding.btnPause, 1f, 0f, 500, 0)
            Anim.fadeInOrOut(binding.btnFinish, 1f, 0f, 500, 0)
            Anim.fadeInOrOut(binding.btnStartRun, 0f, 1f, 500, View.VISIBLE)
            Anim.animateBtnSongAndSettings(binding.btnSong, "translationX", -5f, 1000)
            Anim.animateBtnSongAndSettings(binding.btnSettings, "translationX", 5f, 1000)
            Anim.hideOrShowBottomNavView(activity, View.VISIBLE)
        }

        binding.btnStartRun.setOnClickListener {
            activity?.findViewById<Toolbar>(R.id.toolbarRun)?.visibility = View.GONE
            it.isClickable = false
            AppUtilities.countDown(binding.btnStartRun)
            Anim.animateBtnSongAndSettings(binding.btnSong, "translationX", -500f, 1000)
            Anim.animateBtnSongAndSettings(binding.btnSettings, "translationX", 500f, 1000)
            Anim.hideOrShowBottomNavView(activity, View.GONE)
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                toogleRun()
                it.isClickable = true
                Anim.fadeInOrOut(binding.toolbarHome, 0f, 1f, 500, View.VISIBLE)
                Anim.fadeInOrOut(binding.linearLayout, 0f, 1f, 500, View.VISIBLE)
                Anim.fadeInOrOut(it, 1f, 0f, 500, 0)
                Anim.fadeInOrOut(binding.btnPause, 0f, 1f, 500, View.VISIBLE)
                Anim.fadeInOrOut(binding.btnFinish, 0f, 1f, 500, View.VISIBLE)
            }


        }
    }

    private fun zoomToSeeWholeTrack() {
        map?.moveCamera(
            CameraUpdateFactory.zoomOut()
        )


    }

    private fun changeStyleMap(
        style: Boolean,
        context: Context,
        map: GoogleMap?
    ) {
        viewModel.changeStyleMap(style, context, map)
    }

    private fun continueRun(view: View, view2: View) {
        view.setOnClickListener {
            sendCommandToService(Constantes.ACTION_START_OR_RESUME_SERVICE)
            view.visibility = View.GONE
            view2.visibility = View.VISIBLE
        }
    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Constantes.POLYLINE_COLOR)
                .width(Constantes.POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun subscribeToObserve() {
        TrackingService.pathPoints.observe(viewLifecycleOwner) {
            pathPoints = it
            addLastestPathpoints()
            moveCameraToUser()
        }

        TrackingService.distanceTotal.observe(viewLifecycleOwner) {
            distanceKm = it
            binding.txtDistanceMsSecond.text =
                "${AppUtilities.formatTo2DecimalHomes(distanceKm)} (km)"
        }


        TrackingService.speedInMetersPerSecond.observe(viewLifecycleOwner) {
            speedOfRun = it
            binding.txtSpeed.text = "$speedOfRun km/h"
        }

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner) {
            currentTimeMillis = it
            val formattedInSeconds =
                AppUtilities.getTimerInMillisAndChangeToSeconds(currentTimeMillis, true)
            binding.txtTimeInSeconds.text = formattedInSeconds
        }
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    Constantes.MAP_ZOOM
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getStartLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result != null) {
                    val latlng = LatLng(it.result.latitude, it.result.longitude)
                    map?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(latlng, Constantes.MAP_ZOOM)
                    )
                }
            }
        }

    }

    private fun addLastestPathpoints() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLong = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
            polylineOptions.apply {
                color(Constantes.POLYLINE_COLOR)
                width(Constantes.POLYLINE_WIDTH)
                add(preLastLatLng)
                add(lastLatLong)
            }
            map?.addPolyline(polylineOptions)
        }
    }

    private fun changeThisFragmentToFinishRunFragment() {
        map?.snapshot {
            val timeInSecond =
                AppUtilities.getTimerInMillisAndChangeToSeconds(currentTimeMillis, true)
            val distanceTotal = distanceKm
            val kmh = speedOfRun
            val action = HomeFragmentDirections.actionIdHomeToFinishRunFragment(
                timerInsSeconds = timeInSecond,
                distanceTotal = distanceTotal.toFloat(),
                kmh = kmh.toFloat()
            )
            findNavController().navigate(action)

        }


    }

    private fun toogleRun() {
        if (isTracking) {
            sendCommandToService(Constantes.ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(Constantes.ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }


    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }


}