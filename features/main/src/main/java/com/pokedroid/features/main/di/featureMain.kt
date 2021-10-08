package com.pokedroid.features.main.di

import com.pokedroid.core.di.provideApiService
import com.pokedroid.features.main.MainViewModel
import com.pokedroid.features.main.api.MainApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureMain = module {
    single { provideApiService<MainApi>(get()) }
    viewModel { MainViewModel(get() , get()) }
}