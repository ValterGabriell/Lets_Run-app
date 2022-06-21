package com.example.runapp.ui.ui.showlast

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.runapp.databinding.FragmentShowLastDataBinding
import com.example.runapp.model.RunModel
import com.example.runapp.other.AppUtilities
import com.example.runapp.ui.viewmodel.ShowLastDataViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.koin.android.ext.android.inject

class ShowLastDataFragment : Fragment() {
    private lateinit var binding: FragmentShowLastDataBinding
    private val args by navArgs<ShowLastDataFragmentArgs>()
    private val viewModel by inject<ShowLastDataViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowLastDataBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTexts()
        changeImgBackground()
        var acct = GoogleSignIn.getLastSignedInAccount(requireActivity())

        binding.btnFinalizar.setOnClickListener {
            binding.btnFinalizar.visibility =View.GONE
            binding.progressBar.visibility =View.VISIBLE
            val runModel = RunModel(
                0,
                acct?.id!!,
                acct.displayName!!,
                args.timeRun,
                args.emotion,
                args.mediaRun.toDouble(),
                args.distanciaTotal.toDouble(),
                args.note,
                args.daynight,
                args.local,
                viewModel.getCurrentDate()
            )

            viewModel.saveIntoDatabase(runModel, requireContext(), binding.btnFinalizar, binding.progressBar)


        }
    }


    private fun changeImgBackground() {
        val local = args.local
        viewModel.changeBackgroundImg(local, binding.imgBackgroundA)
    }

    @SuppressLint("SetTextI18n")
    private fun setTexts() {
        binding.txtTime.text = args.timeRun
        binding.txtDistanciaTotal.text =
            "${AppUtilities.formatTo2DecimalHomes(args.distanciaTotal.toDouble())} km"
        binding.txtVelocidadeMedia.text =
            "${AppUtilities.formatTo2DecimalHomes(args.mediaRun.toDouble())} km/h"
        binding.editTextDayNight2.setText(args.daynight)
        binding.txtNote.text = args.note
        binding.txtEmotion.text = args.emotion.replace("[", "").replace("]", "")
        binding.txtDate.text = viewModel.getCurrentDate()
    }

}