package com.example.runapp.other

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.children
import com.example.runapp.R
import com.example.runapp.service.Polyline
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.squareup.picasso.Picasso
import pub.devrel.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit


object AppUtilities {
    fun hasPermissionsAccepts(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun formatTo2DecimalHomes(value: Double): String {
        return String.format("%.2f", value)
    }


    fun countDown(button: Button) {
        object : CountDownTimer(3000, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                button.text = "Prepare-se: " + millisUntilFinished / 1000 + "s"
            }

            override fun onFinish() {}
        }.start()
    }

    fun calculatePolylineLength(polyline: Polyline): Double {
        var distance = 0.00
        for (i in 0..polyline.size - 2) {
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    fun filterChip(chipGroup: ChipGroup): String {
        val name = chipGroup.children
            .filter { (it as Chip).isChecked }
            .map { (it as Chip).text.toString() }.toList()

        return name.toString().replace("[", "").replace("]", "")
    }


    fun changeBackground(string: String, img: ImageView) {
        when (string) {
            "[Cidade]" -> {
                Picasso.get().load(R.drawable.img_cidade).into(img)
            }
            "[Campo]" -> {
                Picasso.get().load(R.drawable.img_campo).into(img)
            }
        }
    }

    fun getTimerInMillisAndChangeToSeconds(ms: Long, includeMs: Boolean = false): String {
        var milliseconds = ms

        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        if (!includeMs) {
            return "${if (hours < 10) "0" else ""}$hours:" +
                    "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds"
        }

        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds:" +
                "${if (milliseconds < 10) "0" else ""}$milliseconds"
    }



}