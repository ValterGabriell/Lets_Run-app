package com.example.runapp.di.modules

import com.example.runapp.repository.*
import com.example.runapp.ui.viewmodel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { FinishRunRepository() }
    single { HomeRepository() }
    single { ProfileRepository() }
    single { ListRunRepository() }


    viewModel { FinishRunViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { ListRunViewModel(get()) }
}
