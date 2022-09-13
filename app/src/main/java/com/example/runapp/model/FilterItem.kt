package com.example.runapp.model

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.R
import com.google.android.material.chip.Chip

data class FilterItem(
    val id: Int,
    val text: String
) {

    fun toChip(context: Context): Chip {
        val chip = LayoutInflater.from(context).inflate(R.layout.material_time_chip, null, false) as Chip

        /**
         * aplicando as configuracoes no chip
         */
        chip.apply {
            setChipStrokeColorResource(R.color.design_box_stroke_color)
            chipStrokeWidth = 2f
        }

        chip.text = text
        return chip
    }
}