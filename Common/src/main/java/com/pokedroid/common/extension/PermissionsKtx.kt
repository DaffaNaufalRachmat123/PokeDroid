/*
package com.pokedroid.common.extension

import android.Manifest.permission.*
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX

private typealias Granted = (allGranted: Boolean, grantedList: List<String>, deniedList: List<String>) -> Unit

fun checkPermission(
    activity: FragmentActivity,
    vararg permissions: String
): Boolean {
    for (per in permissions) {
        val hasPermission = ActivityCompat.checkSelfPermission(
            activity,
            per
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) return false
    }
    return true
}

private fun checkPermissionAndRequest(
    activity: FragmentActivity,
    fragment: Fragment?,
    pers: List<String>,
    request: Granted = { _, _, _ -> },
    granted: () -> Unit = {}
) {
    val appName = activity.applicationInfo.loadLabel(activity.packageManager).toString()
    (if (fragment == null) PermissionX.init(activity) else PermissionX.init(fragment)).permissions(
        pers
    )
        .explainReasonBeforeRequest()
        .onExplainRequestReason { scope, deniedList, beforeRequest ->
            if (beforeRequest) {
                // Explain the reason for the permission request before requesting permission
                val message = "$appName memerlukan izin akses ${transformText(deniedList).joinToString(",")}\nIzinkan akses untuk melanjutkan?"
                scope.showRequestReasonDialog(
                    deniedList,
                    message,
                    "Izinkan",
                    "Batal"
                )
            } else {
                val message =
                    "Kamu menolak izin akses ${transformText(deniedList).joinToString(",")}\n${appName} tidak dapat berjalan jika kamu menolak izin tsb, izinkan akses untuk melanjutkan?"
                scope.showRequestReasonDialog(
                    deniedList,
                    message,
                    "Izinkan",
                    "Batal"
                )
            }
        }
        .onForwardToSettings { scope, deniedList ->
            val settingMessage =
                "Kamu menolak secara permanen izin akses ${transformText(deniedList).joinToString(",")}\n${appName} tidak dapat berjalan jika kamu menolak izin tsb, harap aktifkan kembali izin pada pengaturan aplikasi"
            scope.showForwardToSettingsDialog(deniedList, settingMessage, "Ke Pengaturan", "Batal")
        }
        .request { allGranted, grantedList, deniedList ->
            request(allGranted, grantedList, deniedList)
            if (allGranted) granted() else {
                activity.toast("Izin akses ${transformText(deniedList).joinToString(",")} ditolak")
            }
        }
}

fun FragmentActivity.checkPermissionAndRequest(
    vararg permissions: String,
    request: Granted = { _, _, _ -> },
    granted: () -> Unit = {}
) = checkPermissionAndRequest(this, null, permissions.toList(), request, granted)

fun Fragment.checkPermissionAndRequest(
    vararg permissions: String,
    request: Granted = { _, _, _ -> },
    granted: () -> Unit = {}
) = checkPermissionAndRequest(requireActivity(), this, permissions.toList(), request, granted)

fun Dialog.checkPermissionAndRequest(
    vararg permissions: String,
    request: Granted = { _, _, _ -> },
    granted: () -> Unit = {}
) = (getActivity() as? FragmentActivity)?.checkPermissionAndRequest(
    *permissions,
    request = request,
    granted = granted
) ?: throw RuntimeException("activity 为 null")

fun Dialog.getActivity(): Activity? {
    var context: Context = context
    while (context is ContextWrapper) {
        if (context is FragmentActivity) return context
        context = context.baseContext
    }
    return null
}

fun FragmentActivity.requestLocationPermission(onGranted: () -> Unit = {}) {
    checkPermissionAndRequest(
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION,
        granted = onGranted
    )
}

fun FragmentActivity.requestStoragePermission(onGranted: () -> Unit = {}) {
    checkPermissionAndRequest(
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE,
        CAMERA,
        granted = onGranted
    )
}

private fun transformText(permissions: List<String>): List<String> {
    val textList: MutableList<String> =
        ArrayList()

    fun addMessage(message: String) {
        if (!textList.contains(message))
            textList.add(message)
    }
    for (permission in permissions) {
        when (permission) {
            READ_CALENDAR, WRITE_CALENDAR -> addMessage("Kalender")
            CAMERA -> addMessage("Kamera")
            READ_CONTACTS, WRITE_CONTACTS, GET_ACCOUNTS -> addMessage("Kontak")
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION -> addMessage("Lokasi")
            RECORD_AUDIO -> addMessage("Rekam audio")
            READ_PHONE_STATE, CALL_PHONE, READ_CALL_LOG, WRITE_CALL_LOG, USE_SIP, PROCESS_OUTGOING_CALLS -> addMessage(
                "Telepon"
            )
            BODY_SENSORS -> addMessage("Body sensor")
            SEND_SMS, RECEIVE_SMS, READ_SMS, RECEIVE_WAP_PUSH, RECEIVE_MMS -> addMessage("SMS")
            READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE -> addMessage("Penyimpanan")
        }
    }
    return textList
}

*/
/**
 * Open current application detail activity so user can change permission manually.
 *//*

fun ComponentActivity.openAppDetailsActivity() {
    startActivity(Intent().apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        data = Uri.parse("package:$packageName")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    })
}
*/
