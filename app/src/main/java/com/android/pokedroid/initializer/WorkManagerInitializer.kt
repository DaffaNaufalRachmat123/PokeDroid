package com.android.pokedroid.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.android.pokedroid.BuildConfig
import com.github.ajalt.timberkt.i

private val WorkInfo.description get() = "WorkInfo { id: $id, state: $state, progress: $progress, outputData: $outputData }"

@Suppress("unused")
class WorkManagerInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        WorkManager.initialize(
            context,
            Configuration.Builder()
                .setMinimumLoggingLevel(Log.INFO)
                .build()
        )

        // logs work information in debug mode
        if (BuildConfig.DEBUG) {
            var times = 0
            WorkManager
                .getInstance(context)
                .getWorkInfosByTagLiveData("SyncCollectionWorker")
                .observeForever { workInfos ->
                    val s = workInfos
                        .map(WorkInfo::description)
                        .joinToString(
                            separator = "\n",
                            prefix = "\n",
                            postfix = "\n",
                        )

                    i { "workInfomations [${times++}]: $s" }
                }
        }


        i { "WorkManager initialized" }

    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(TimberInitializer::class.java)
}