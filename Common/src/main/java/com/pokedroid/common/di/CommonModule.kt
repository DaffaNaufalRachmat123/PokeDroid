package com.pokedroid.common.di

import android.annotation.SuppressLint
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


@SuppressLint("MissingPermission")
val commonModule = module {
    // provide FirebaseAnalytics
    // provide crashlytics
    single {
        FirebaseCrashlytics.getInstance()
    }

    single { NotificationManagerCompat.from(get()) }
}