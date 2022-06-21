package com.example.runapp.di.modules

import com.example.runapp.repository.FinishRunRepository
import com.example.runapp.repository.HomeRepository
import com.example.runapp.repository.ProfileRepository
import com.example.runapp.repository.ShowLastDataRepository
import com.example.runapp.ui.viewmodel.FinishRunViewModel
import com.example.runapp.ui.viewmodel.HomeViewModel
import com.example.runapp.ui.viewmodel.ProfileViewModel
import com.example.runapp.ui.viewmodel.ShowLastDataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
        single { FinishRunRepository() }
        single { ShowLastDataRepository() }
        single { HomeRepository() }
        single { ProfileRepository() }


        viewModel { FinishRunViewModel(get()) }
        viewModel { ShowLastDataViewModel(get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { ProfileViewModel(get()) }
}
