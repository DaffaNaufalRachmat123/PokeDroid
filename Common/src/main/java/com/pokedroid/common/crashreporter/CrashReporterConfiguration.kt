package com.pokedroid.common.crashreporter

import java.io.Serializable

class CrashReporterConfiguration : Serializable {
    var crashReportStoragePath: String = ""

    var maxNoOfCrashToBeReport = 5
    var extraInformation: String = ""
    var includeDeviceInformation = true

    fun setCrashReportStoragePath(path: String): CrashReporterConfiguration {
        this.crashReportStoragePath = path
        return this
    }

    fun setExtraInformation(information: String): CrashReporterConfiguration {
        this.extraInformation = information
        return this
    }

    fun setMaxNumberOfCrashToBeReport(count: Int): CrashReporterConfiguration {
        this.maxNoOfCrashToBeReport = if (count in 1..15) count else this.maxNoOfCrashToBeReport
        return this
    }

    fun setIncludeDeviceInformation(allow: Boolean): CrashReporterConfiguration {
        this.includeDeviceInformation = allow
        return this
    }

}