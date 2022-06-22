package com.example.runapp.ui.ui.finish

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.runapp.databinding.FragmentFinishRunBinding
import com.example.runapp.model.RunModelFinal
import com.example.runapp.other.AppUtilities
import com.example.runapp.ui.viewmodel.FinishRunViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        changeBackgroundImg()
        setTexts()


        CoroutineScope(Dispatchers.Main).launch {
            viewModel.checkIfIsDayOrNight(binding.editTextDayNight2)
        }

        CoroutineScope(Dispatchers.Main).launch {
            binding.txtDate.text = viewModel.getCurrentDate()
        }


        binding.chipPlace.setOnCheckedStateChangeListener { group, _ ->
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
                saveIntoDatabase(acct)
            }
        }


    }

    private fun changeBackgroundImg() {

    }


    private fun saveIntoDatabase(acct: GoogleSignInAccount?) {
        binding.floatingActionButton.visibility = View.GONE
        binding.progressBarSave.visibility = View.VISIBLE
        val runModel = RunModelFinal(
            0,
            acct?.id!!,
            acct.displayName!!,
            args.timerInsSeconds,
            emotion,
            args.kmh.toDouble(),
            args.distanceTotal.toDouble(),
            binding.etNote.text.toString(),
            binding.etNote.text.toString(),
            local,
            viewModel.getCurrentDate(),
            "ESSA"
        )

        viewModel.saveIntoDatabase(
            runModel,
            requireContext(),
            binding.floatingActionButton,
            binding.progressBarSave
        )


    }

    @SuppressLint("SetTextI18n")
    private fun setTexts() {
        binding.txtTime.text = args.timerInsSeconds
        binding.txtDistanciaTotal.text =
            "${AppUtilities.formatTo2DecimalHomes(args.distanceTotal.toDouble())} km"
        binding.txtVelocidadeMedia.text =
            "${AppUtilities.formatTo2DecimalHomes(args.kmh.toDouble())} km/h"
    }


}
