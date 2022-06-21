package com.example.runapp.ui.animUI

import android.animation.ObjectAnimator
import android.app.Activity
import android.view.View
import com.example.runapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

object Anim {

    fun fadeInOrOut(
        view: View,
        valueInitial: Float,
        valueFinal: Float,
        duration: Long,
        visivility: Int
    ) {
        view.apply {
            alpha = valueInitial
            visibility = visivility
            animate().alpha(valueFinal).setDuration(duration).setListener(null)
        }
    }

    fun hideOrShowBottomNavView(activity: Activity?, visivility: Int) {
        val navbar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (visivility == View.GONE) {
            ObjectAnimator.ofFloat(navbar, "translationY", 500f).apply {
                duration = 500
                start()
            }
        } else {
            ObjectAnimator.ofFloat(navbar, "translationY", 0f).apply {
                duration = 500
                start()
            }
        }

    }

    fun animateBtnSongAndSettings(
        view: View,
        propertyName: String,
        value: Float,
        seconds: Long
    ) {
        ObjectAnimator.ofFloat(view, propertyName, value).apply {
            duration = seconds
            start()
        }
    }
}