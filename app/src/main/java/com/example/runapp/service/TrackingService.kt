package com.example.runapp.service


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.runapp.R
import com.example.runapp.other.AppUtilities
import com.example.runapp.other.Constantes
import com.example.runapp.other.Constantes.CHANNEL_ID
import com.example.runapp.other.Constantes.NOTIFICATION_ID
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>


class TrackingService : LifecycleService() {

    private var serviceKilled = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var isFirstStartRun = true
    private var timeWhenRunStarted = 0L
    private var timeRun = 0L
    private var lapTime = 0L
    private var lastSecondPassed = 0L
    private var isTimerEnable = false
    private val timeRunInSecond = MutableLiveData<Long>()

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
        val distanceTotal = MutableLiveData<Double>()
        val speedInMetersPerSecond = MutableLiveData<Int>()
        val timeRunInMillis = MutableLiveData<Long>()
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        isTracking.observe(this) {
            updateLocationTracker(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Constantes.ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstStartRun) {
                        startForegroundService()
                        isFirstStartRun = false
                    } else {
                        startTimer()
                    }
                }

                Constantes.ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }

                Constantes.ACTION_STOP_SERVICE -> {
                    killService()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun killService() {
        postInitialValues()
        serviceKilled = true
        isFirstStartRun = true
        pauseService()
        stopForeground(true)
        stopSelf()
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnable = false
    }

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)
    }

    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeWhenRunStarted = System.currentTimeMillis()
        isTimerEnable = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeWhenRunStarted
                timeRunInMillis.postValue(timeRun + lapTime)
                if (timeRunInMillis.value!! >= lastSecondPassed + 1000L) {
                    timeRunInSecond.postValue(timeRunInSecond.value!! + 1)
                    lastSecondPassed += 1000L
                }
                delay(Constantes.TIMER_UPDATE)
            }
        }
        timeRun += lapTime

        timeRunInMillis.observe(this) {
            var timeFormatted = AppUtilities.getTimerInMillisAndChangeToSeconds(it, false)
            createNotification(timeFormatted, this)
        }

    }


    private fun addEmptyPolyline() =
        pathPoints.value?.apply {
            add(mutableListOf())
            pathPoints.postValue(this)
        } ?: pathPoints.postValue(mutableListOf(mutableListOf()))


    @SuppressLint("MissingPermission")
    private fun updateLocationTracker(isTracking: Boolean) {
        if (isTracking) {
            if (AppUtilities.hasPermissionsAccepts(this)) {
                val request = LocationRequest().apply {
                    interval = Constantes.LOCATION_UPDATE_INTERVAL
                    fastestInterval = Constantes.FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let {
                    for (locations in it) {
                        addPathPoints(locations)
                    }
                }
            }
        }
    }

    private fun createNotification(timeRun: String, context: Context) {
        val builder = NotificationCompat.Builder(
            context,
            Constantes.CHANNEL_ID
        ).setSmallIcon(R.drawable.ic_baseline_av_timer_24)
            .setContentTitle(timeRun)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(context)) {
            notify(Constantes.NOTIFICATION_ID, builder.build())
        }
    }

    private fun addPathPoints(locations: Location?) {
        locations?.let {
            val position = LatLng(locations.latitude, locations.longitude)
            pathPoints.value?.apply {
                last().add(position)
                pathPoints.postValue(this)
                distanceTotal.postValue(
                    calculateDiferenceBetweenStartLocationAndCurrentLocation(
                        this
                    )
                )

                calculateAvergedSpeed(this)
            }
        }
    }

    private fun calculateDiferenceBetweenStartLocationAndCurrentLocation(pathPoints: Polylines): Double {
        var distanceInMeters = 0.00
        for (polylines in pathPoints) {
            distanceInMeters += AppUtilities.calculatePolylineLength(polylines)
        }
        return (distanceInMeters / 1000)
    }

    private fun calculateAvergedSpeed(pathPoints: Polylines) {
        var distanceInMeters = 0.00
        for (polylines in pathPoints) {
            distanceInMeters += AppUtilities.calculatePolylineLength(polylines)
        }

        timeRunInSecond.observe(this) {
            val velocidade = (distanceInMeters / it)
            speedInMetersPerSecond.postValue((velocidade * 3.6).toInt())
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }


    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSecond.postValue(0L)
        timeRunInMillis.postValue(0L)
        distanceTotal.postValue(0.00)
        speedInMetersPerSecond.postValue(0)
    }

}