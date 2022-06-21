package com.example.runapp.ui.ui.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.runapp.databinding.FragmentFinishRunBinding
import com.example.runapp.other.AppUtilities
import com.example.runapp.ui.viewmodel.FinishRunViewModel
import org.koin.android.ext.android.inject

class FinishRunFragment : Fragment() {

    private lateinit var binding: FragmentFinishRunBinding
    private val viewModel by inject<FinishRunViewModel>()
    private val args by navArgs<FinishRunFragmentArgs>()
    private var local = ""
    private var emotion = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishRunBinding.inflate(inflater, container, false)
        viewModel.checkIfIsDayOrNight(binding.editTextDayNight)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chipPlace.setOnCheckedStateChangeListener { group, _ ->
            viewModel.setBackgroundImg(
                AppUtilities.filterChip(
                    group
                ),
                binding.imageView4
            )
            local = AppUtilities.filterChip(
                group
            )


        }

        binding.chipEmotions.setOnCheckedStateChangeListener { group, _ ->
            AppUtilities.filterChip(group)
            emotion = AppUtilities.filterChip(group)
        }


        binding.floatingActionButton.setOnClickListener {
            if (!binding.etNote.text.isNullOrEmpty()) {
                changeFragmentToLastDataFragment(
                    binding.editTextDayNight,
                    binding.etNote
                )
            }

        }


    }


    private fun changeFragmentToLastDataFragment(etDayNight: EditText, etNote: EditText) {

        val action = FinishRunFragmentDirections.actionFinishRunFragmentToShowLastDataFragment(
            mediaRun = args.kmh,
            timeRun = args.timerInsSeconds,
            distanciaTotal = args.distanceTotal,
            daynight = etDayNight.text.toString(),
            note = etNote.text.toString(),
            local = this.local,
            emotion = this.emotion
        )
        findNavController().navigate(action)

    }

}
