package com.pokedroid.common.crashreporter

import android.content.Context
import android.text.TextUtils
import androidx.activity.ComponentActivity
import java.io.File

object CrashReporter {
    lateinit var context: Context
    var config: CrashReporterConfiguration = CrashReporterConfiguration()

    fun initialize(context: Context, crashReporterConfiguration: CrashReporterConfiguration) {
        this.context = context
        config = crashReporterConfiguration
        setUpExceptionHandler()
    }


    private fun setUpExceptionHandler() {
        Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(CrashReporterExceptionHandler())
    }

    //LOG Exception APIs
    fun logException(exception: Exception) {
        CrashUtil.logException(exception)
    }

    val crashLogFilesPath: String
        get() {
            return if (TextUtils.isEmpty(config.crashReportStoragePath)) {
                val defaultPath =
                    (context.getExternalFilesDir(null)!!.absolutePath + File.separator + AppUtils.CRASH_REPORT_DIR)
                val file = File(defaultPath)
                file.mkdirs()
                defaultPath
            } else
                config.crashReportStoragePath

        }

    fun sendCrashReport(activity: ComponentActivity) {

    }

}