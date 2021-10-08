package com.android.pokedroid.di

import com.pokedroid.common.di.commonModule
import com.pokedroid.core.di.coreModule
import com.pokedroid.features.main.di.featureMain

val appComponent = listOf(
        coreModule,
        commonModule,
        featureMain
)