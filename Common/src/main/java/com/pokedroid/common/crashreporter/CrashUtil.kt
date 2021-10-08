package com.pokedroid.common.crashreporter

import android.content.Context
import android.preference.PreferenceManager
import com.pokedroid.common.extension.orZero
import timber.log.Timber
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class CrashUtil private constructor() {

    companion object {
        private val TAG = CrashUtil::class.java.simpleName
        private val crashLogTime: String
            get() {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault())
                return dateFormat.format(Date())
            }
        internal val dateFormat get() = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

        fun saveCrashReport(throwable: Throwable) {
            val filename = crashLogTime + AppUtils.CRASH_SUFFIX + AppUtils.FILE_EXTENSION
            writeToFile(CrashReporter.crashLogFilesPath, filename, getStackTrace(throwable))
        }

        fun logException(exception: Exception) {
            Thread(Runnable {
                val filename = crashLogTime + AppUtils.EXCEPTION_SUFFIX + AppUtils.FILE_EXTENSION
                writeToFile(
                    CrashReporter.crashLogFilesPath,
                    filename,
                    getStackTrace(exception)
                )
            }).start()
        }

        private fun writeToFile(crashReportPath: String, filename: String, crashLog: String) {
            val bufferedWriter: BufferedWriter
            try {
                val fullFileName = "$crashReportPath/$filename"
                if (!File(fullFileName).exists())
                    File(fullFileName).createNewFile()

                bufferedWriter = BufferedWriter(FileWriter(fullFileName))
                bufferedWriter.write(crashLog)
                bufferedWriter.flush()
                bufferedWriter.close()
                Timber.d("Crash report saved in : $fullFileName")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun getCrashContent(crashLog: String): String {
            val sb = StringBuilder()

            sb.append("APP NAME : ").append(AppUtils.getApplicationName(CrashReporter.context))
                .append("\n\n")
            if (CrashReporter.config.includeDeviceInformation) {
                sb.append("======= DEVICE INFORMATION =======").append("\n")
                sb.append(AppUtils.getDeviceDetails(CrashReporter.context)).append("\n\n")
            }

            sb.append("======= EXTRA INFORMATION =======").append("\n")
            sb.append(
                """Is sustained performance mode supported: ${CrashReporter.context.isSustainedPerformanceModeSupported}
Is in power save mode                  : ${CrashReporter.context.isInPowerSaveMode}
Is in interactive state                : ${CrashReporter.context.isInInteractiveState}
Is ignoring battery optimizations      : ${CrashReporter.context.isIgnoringBatteryOptimization}
Thermal status                         : ${CrashReporter.context.getThermalStatus}
Location power save mode               : ${CrashReporter.context.locationPowerSaveMode}
Is device idle                         : ${CrashReporter.context.isDeviceIdle}
Battery percentage                     : ${CrashReporter.context.getBatteryPercentage}%
Is battery charging                    : ${CrashReporter.context.isBatteryCharging.asYesOrNo()}
""" +
                        "First installed                        : ${
                            dateFormat.format(
                                Date(
                                    CrashReporter.context.getFirstInstallTime
                                )
                            )
                        }\n" +
                        "Last updated                           : ${
                            dateFormat.format(
                                Date(
                                    CrashReporter.context.lastUpdateTime
                                )
                            )
                        }\n"
            )

            sb.append("\n======= CRASH INFORMATION =======").append("\n\n")
            sb.append(crashLog).append("\n")
            sb.append("=============== END ===============").append("\n\n")

            return sb.toString()

        }

        private fun collectSharedPrefs(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context).all.iterator().asSequence().map {
                val key = it.key
                val value = it.value
                "$key = $value"
            }.toList().mapWithoutNewLine()

        private fun getStackTrace(e: Throwable): String {
            val result: Writer = StringWriter()
            val printWriter = PrintWriter(result)
            e.printStackTrace(printWriter)
            val crashLog = getCrashContent(result.toString())
            printWriter.close()
            return crashLog
        }

        val exceptionFileList: Array<File>?
            get() {
                val dir = File(CrashReporter.crashLogFilesPath).listFiles()
                dir?.filter { it.name.endsWith(AppUtils.FILE_EXTENSION) }
                dir?.sortByDescending { it.lastModified() }
                return dir
            }

        val isHaveCrashData: Boolean get() = crashLogsCount > 0

        val crashLogsCount: Int get() = if (exceptionFileList != null) exceptionFileList?.size!! else 0

        fun clearAllCrashLogs() {
            Thread(Runnable {
                File(CrashReporter.crashLogFilesPath).deleteRecursively()
            }).start()
        }
    }
}