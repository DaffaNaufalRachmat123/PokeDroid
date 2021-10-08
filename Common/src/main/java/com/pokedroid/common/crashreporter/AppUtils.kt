package com.pokedroid.common.crashreporter


import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import android.os.PowerManager.*
import android.text.format.DateFormat
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import androidx.annotation.RequiresApi
import com.github.ajalt.timberkt.i
import java.security.MessageDigest
import java.util.*

object AppUtils {

    const val EXCEPTION_SUFFIX = "_exception"
    const val CRASH_SUFFIX = "_crash"
    const val FILE_EXTENSION = ".txt"
    const val CRASH_REPORT_DIR = "crashReports"
    const val NOTIFICATION_ID = 1
    const val CHANNEL_NOTIFICATION_ID = "crash_reporter_channel_id"
    const val LANDING = "landing"

    @JvmStatic
    fun getDeviceDetails(context: Context): String {
        return """
        DATE & TIME : ${DateFormat.format("dd-MM-yyyy hh:mm:ss a", Date())}
        APP VERSION : ${getAppVersion(context)}
        APP VERSION_NAME : ${getAppVersionName(context)}
        TIMEZONE : ${timeZone()}
        PACKAGE NAME : ${context.packageName}
        OS VERSION : ${Build.VERSION.RELEASE}
        VERSION.SDK.NUMBER : ${Build.VERSION.SDK_INT}
        BRAND : ${Build.BRAND}
        MODEL : ${Build.MODEL}
        MANUFACTURER : ${Build.MANUFACTURER}
        DEVICE : ${Build.DEVICE}
        BOARD : ${Build.BOARD}
        PRODUCT : ${Build.PRODUCT}
        ID : ${Build.ID}
        HOST : ${Build.HOST}
        HARDWARE : ${Build.HARDWARE}
        BOOTLOADER : ${Build.BOOTLOADER}
        CPU_ABI : ${Build.CPU_ABI}
        CPU_ABI2 : ${Build.CPU_ABI2}
        DISPLAY : ${Build.DISPLAY}
        TIMESTAMP : ${Build.TIME}""".trimIndent()
    }

    private fun timeZone(): String {
        val tz = TimeZone.getDefault()
        return tz.id
    }

    private fun getAppVersion(context: Context): Long {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        var versionCode: Long = 0
        try {
            versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode
            } else {
                pInfo.versionCode.toLong()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionCode
    }

    private fun getAppVersionName(context: Context): String {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return pInfo.versionName
    }

    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
            stringId
        )
    }
}

private inline val Context.batteryManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.BATTERY_SERVICE) as BatteryManager

internal val Context.getFirstInstallTime
    get() = packageManager.getPackageInfo(
        packageName,
        0
    ).firstInstallTime
internal val Context.lastUpdateTime
    get() = packageManager.getPackageInfo(
        packageName,
        0
    ).lastUpdateTime
internal val Context.requestedPermissions
    get() = tryOrNull {
        packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PERMISSIONS
        ).requestedPermissions.toList()
    }


internal val Context.getBatteryPercentage get() = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

internal val Context.isBatteryCharging
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        batteryManager.isCharging
    } else {
        null
    }

private inline val Context.powerManager
    get() = getSystemService(Context.POWER_SERVICE) as PowerManager?

private inline val Context.activityManager: ActivityManager
    get() = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

internal val Context.isSustainedPerformanceModeSupported
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        powerManager?.isSustainedPerformanceModeSupported.asYesOrNo()
    } else {
        notAvailableString
    }

internal val Context.isInPowerSaveMode
    get() = powerManager?.isPowerSaveMode.asYesOrNo()

internal val Context.isInInteractiveState
    get() = powerManager?.isInteractive.asYesOrNo()

internal val Context.isIgnoringBatteryOptimization
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        powerManager?.isIgnoringBatteryOptimizations(packageName).asYesOrNo()
    } else {
        notAvailableString
    }

internal fun Boolean?.asYesOrNo() =
    when (this) {
        true -> "Yes"
        false -> "No"
        null -> notAvailableString
    }


/**
 *  THERMAL_STATUS_NONE if device in not under thermal throttling. Value is
 *  THERMAL_STATUS_NONE, THERMAL_STATUS_LIGHT,
 *  THERMAL_STATUS_MODERATE, THERMAL_STATUS_SEVERE, THERMAL_STATUS_CRITICAL, THERMAL_STATUS_EMERGENCY, or THERMAL_STATUS_SHUTDOWN
 */
internal val Context.getThermalStatus: String
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when (powerManager?.currentThermalStatus) {
                THERMAL_STATUS_NONE -> "STATUS_NONE"
                THERMAL_STATUS_LIGHT -> "STATUS_LIGHT"
                THERMAL_STATUS_MODERATE -> "STATUS_MODERATE"
                THERMAL_STATUS_SEVERE -> "STATUS_SEVERE"
                THERMAL_STATUS_CRITICAL -> "STATUS_CRITICAL"
                THERMAL_STATUS_EMERGENCY -> "STATUS_EMERGENCY"
                THERMAL_STATUS_SHUTDOWN -> "STATUS_SHUTDOWN"

                else -> notAvailableString
            }
        } else {
            notAvailableString
        }
    }

/**
 * 	Value is LOCATION_MODE_NO_CHANGE, LOCATION_MODE_GPS_DISABLED_WHEN_SCREEN_OFF,
 * 	LOCATION_MODE_ALL_DISABLED_WHEN_SCREEN_OFF,
 * 	LOCATION_MODE_FOREGROUND_ONLY, or LOCATION_MODE_THROTTLE_REQUESTS_WHEN_SCREEN_OFF
 */
internal val Context.locationPowerSaveMode: String
    get() {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            when (powerManager?.locationPowerSaveMode) {
                LOCATION_MODE_NO_CHANGE -> "MODE_NO_CHANGE"
                LOCATION_MODE_GPS_DISABLED_WHEN_SCREEN_OFF -> "MODE_GPS_DISABLED_WHEN_SCREEN_OFF"
                LOCATION_MODE_ALL_DISABLED_WHEN_SCREEN_OFF -> "MODE_ALL_DISABLED_WHEN_SCREEN_OFF"
                LOCATION_MODE_FOREGROUND_ONLY -> "MODE_FOREGROUND_ONLY"
                LOCATION_MODE_THROTTLE_REQUESTS_WHEN_SCREEN_OFF -> "MODE_THROTTLE_REQUESTS_WHEN_SCREEN_OFF"
                else -> notAvailableString
            }
        } else {
            notAvailableString
        }
    }

internal val Context.isDeviceIdle
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        powerManager?.isDeviceIdleMode.asYesOrNo()
    } else {
        notAvailableString
    }

internal inline fun <T> tryOrNull(block: () -> T): T? = try {
    block()
} catch (e: Exception) {
    null
}

internal inline fun <T> tryOrIgnore(block: () -> T) {
    try {
        block()
    } catch (e: Exception) {
        i{"tryOrIgnore catch: ${e.message}"}
    }
}

internal fun Context.getRunningProcesses() =
    tryOrNull {
        activityManager.getRunningServices(Integer.MAX_VALUE).map {
            it.service.className
        }
    }.notAvailableIfNullNewLine().replace("[", "").replace("]", "").replace(",", "\n")

internal fun buildImportance(importance: Int): String {
    return when (importance) {
        RunningAppProcessInfo.IMPORTANCE_FOREGROUND -> "FOREGROUND"
        RunningAppProcessInfo.IMPORTANCE_FOREGROUND_SERVICE -> "FOREGROUND_SERVICE"
        RunningAppProcessInfo.IMPORTANCE_TOP_SLEEPING -> "TOP_SLEEPING"
        RunningAppProcessInfo.IMPORTANCE_VISIBLE -> "VISIBLE"
        RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE -> "PERCEPTIBLE"
        RunningAppProcessInfo.IMPORTANCE_CANT_SAVE_STATE -> "CANT_SAVE_STATE"
        RunningAppProcessInfo.IMPORTANCE_SERVICE -> "SERVICE"
        RunningAppProcessInfo.IMPORTANCE_CACHED -> "CACHED"
        RunningAppProcessInfo.IMPORTANCE_GONE -> "GONE"
        else -> notAvailableString
    }
}

internal const val notAvailableString = "N/A"

internal fun String?.notAvailableIfNull() = if (this.isNullOrEmpty()) notAvailableString else this

internal fun <T> Collection<T>?.notAvailableIfNullNewLine(): String =
    if (this.isNullOrEmpty()) "N/A" else "\n${this}"

internal fun <T> Collection<T>?.notAvailableIfNull(): String =
    if (this.isNullOrEmpty()) "N/A" else "$this"


internal val Context.actualPackageName: String?
    get() = applicationContext.javaClass.`package`?.name

internal val Context.flavor: String?
    get() = getBuildConfigValue(actualPackageName, "FLAVOR") as String?

internal val Context.appName: String?
    get() {
        val applicationInfo = applicationContext.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) {
            applicationInfo.nonLocalizedLabel.toString()
        } else {
            applicationContext.getString(stringId)
        }
    }

internal fun Context.getVersionName(): String =
    packageManager.getPackageInfo(packageName, 0).versionName


internal fun Context.getVersionCodeCompat(): Long =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageManager.getPackageInfo(packageName, 0).longVersionCode
    } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, 0).versionCode.toLong()
    }

/**
 * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
 * are used at the project level to set custom fields.
 * @param fieldName The name of the field-to-access
 * @return The value of the field, or `null` if the field is not found.
 */
private fun getBuildConfigValue(packageName: String?, fieldName: String): Any? {
    val buildConfigClassName = "$packageName.BuildConfig"
    return try {
        val clazz = Class.forName(buildConfigClassName)
        val field = clazz.getField(fieldName)
        field.get(null)
    } catch (e: ClassNotFoundException) {
        null
    } catch (e: NoSuchFieldException) {
        null
    } catch (e: IllegalAccessException) {
        null
    }
}

internal val Context.shortAppName: String?
    get() = actualPackageName?.substringAfterLast('.')


internal val Context.apkSignatures
    get() = currentSignatures.toList()

@Suppress("DEPRECATION", "RemoveExplicitTypeArguments")
private val Context.currentSignatures: Array<String>
    get() {
        val actualSignatures = ArrayList<String>()
        val signatures = try {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    PackageManager.GET_SIGNING_CERTIFICATES
                else PackageManager.GET_SIGNATURES
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (packageInfo.signingInfo.hasMultipleSigners())
                    packageInfo.signingInfo.apkContentsSigners
                else packageInfo.signingInfo.signingCertificateHistory
            } else packageInfo.signatures
        } catch (e: Exception) {
            emptyArray<Signature>()
        }
        signatures.forEach { signature ->
            val messageDigest = MessageDigest.getInstance("SHA")
            messageDigest.update(signature.toByteArray())
            tryOrIgnore {
                actualSignatures.add(
                    encodeToString(
                        messageDigest.digest(),
                        DEFAULT
                    ).trim()
                )
            }
        }
        return actualSignatures.filter { it.isNotEmpty() && it.isNotBlank() }.toTypedArray()
    }

internal fun formatMillisToHoursMinutesSeconds(millis: Long) = String.format(
    "%d hr %d min, %d sec",
    millis / (1000 * 60 * 60),
    (millis % (1000 * 60 * 60)) / (1000 * 60),
    ((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000
)

internal fun <T> List<T>.mapWithNewLine() =
    mapIndexed { index, t -> if (index == lastIndex) t.toString() else "$t\n" }.toString()
        .replace("[", "").replace("]", "")

internal fun <T> Array<T>.mapWithNewLine() =
    mapIndexed { index, t -> if (index == lastIndex) t.toString() else "$t\n" }.toString()
        .replace("[", "").replace("]", "")

internal fun <T> List<T>.mapWithoutNewLine() =
    map { it.toString() }.toString().replace("[", "").replace("]", "")

internal fun <T> Array<T>.mapWithoutNewLine() =
    map { it.toString() }.toString().replace("[", "").replace("]", "")

internal val Context.systemFeatures get() = packageManager.systemAvailableFeatures.mapWithNewLine()

internal fun Context.isDebuggable(): Boolean =
    applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
