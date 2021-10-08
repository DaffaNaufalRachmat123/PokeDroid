/*
package com.pokedroid.common.crashreporter

import android.content.Context
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.ajalt.timberkt.i
import com.pokedroid.common.extension.readFileToString
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.core.component.KoinComponent

class CrashReporterWorker(application: Context, params: WorkerParameters) :
    CoroutineWorker(application, params), KoinComponent {

    //private val syncApi by inject<SyncApiService>()

    override suspend fun doWork(): Result = coroutineScope {
        val crashCount = CrashUtil.crashLogsCount
        if (CrashUtil.isHaveCrashData) {
            i { "crashCount : $crashCount" }

            for (crashFile in CrashUtil.exceptionFileList!!) {
                val builder = MultipartBody.Builder()
                builder.addFormDataPart("filename", crashFile.name)
                builder.addFormDataPart("stacktrace", crashFile.readFileToString())
                val requestBody = builder.build()

                val urlBotDev =
                    "https://api.telegram.org/bot951057918:AAFnsQETCoDdXjwAbxI17YzGWcJvyRDwltE/sendDocument"
                val caption = "Crash found on ${Build.BRAND} ${Build.MODEL} ${Build.MANUFACTURER}"
                val builder2 = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("chat_id", "233175343")
                    .addFormDataPart("caption", caption)
                    .addFormDataPart(
                        "document",
                        crashFile.name,
                        crashFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                val requestBody2 = builder2.build()

                */
/*val crashUploadedAwait = async {
                    syncApi.sendCrashToDev(urlBotDev, requestBody2)
                    syncApi.sendStackTrace(requestBody)
                }
                crashUploadedAwait.await()*//*

            }

            CrashUtil.clearAllCrashLogs()
            Result.success()
        } else {
            Result.success()
        }
    }
}*/
