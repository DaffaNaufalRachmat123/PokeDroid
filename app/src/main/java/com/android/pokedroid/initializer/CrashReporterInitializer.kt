package com.android.pokedroid.initializer

import android.content.Context
import androidx.startup.Initializer
import com.pokedroid.common.crashreporter.CrashReporter
import com.pokedroid.common.crashreporter.CrashReporterConfiguration
import com.github.ajalt.timberkt.i

class CrashReporterInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        val config: CrashReporterConfiguration = CrashReporterConfiguration()
            .setMaxNumberOfCrashToBeReport(5)
            .setIncludeDeviceInformation(true)

        CrashReporter.initialize(context, config)
        i { "CrashReporterInitializer initialized" }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}