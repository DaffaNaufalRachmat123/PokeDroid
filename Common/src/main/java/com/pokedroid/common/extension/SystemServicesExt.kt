/*
 * Copyright 2017 Al Musthofa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.pokedroid.common.extension

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.app.usage.NetworkStatsManager
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.content.RestrictionsManager
import android.content.pm.LauncherApps
import android.content.pm.ShortcutManager
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.media.midi.MidiManager
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.nfc.NfcManager
import android.os.*
import android.os.storage.StorageManager
import android.print.PrintManager
import android.telecom.TelecomManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.inputmethod.InputMethodManager
import android.view.textservice.TextServicesManager

inline val Context.accessibilityManager get() = getSystemService(
        Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

inline val Context.accountManager get() = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

inline val Context.activityManager get() = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

inline val Context.alarmManager get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

inline val Context.audioManager get() = getSystemService(Context.AUDIO_SERVICE) as AudioManager

inline val Context.clipboardManager get() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

inline val Context.connectivityManager get() = getSystemService(
        Context.CONNECTIVITY_SERVICE) as ConnectivityManager

inline val Context.keyguardManager get() = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

inline val Context.layoutInflater get() = getSystemService(
        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

inline val Context.locationManager get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

inline val Context.notificationManager get() = getSystemService(
        Context.NOTIFICATION_SERVICE) as NotificationManager

inline val Context.powerManager get() = getSystemService(Context.POWER_SERVICE) as PowerManager

inline val Context.searchManager get() = getSystemService(Context.SEARCH_SERVICE) as SearchManager

inline val Context.sensorManager get() = getSystemService(Context.SENSOR_SERVICE) as SensorManager

inline val Context.telephonyManager get() = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

inline val Context.vibrator get() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

inline val Context.wallpaperManager get() = getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager

inline val Context.wifiManager
    @SuppressLint("WifiManagerLeak")
    get() = getSystemService(Context.WIFI_SERVICE) as WifiManager

inline val Context.windowManager get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

inline val Context.inputMethodManager get() = getSystemService(
        Context.INPUT_METHOD_SERVICE) as InputMethodManager

inline val Context.devicePolicyManager get() = getSystemService(
        Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

inline val Context.dropBoxManager get() = getSystemService(Context.DROPBOX_SERVICE) as DropBoxManager

inline val Context.uiModeManager get() = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

inline val Context.downloadManager get() = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

inline val Context.storageManager get() = getSystemService(Context.STORAGE_SERVICE) as StorageManager

inline val Context.nfcManager get() = getSystemService(Context.NFC_SERVICE) as NfcManager

inline val Context.usbManager get() = getSystemService(Context.USB_SERVICE) as UsbManager

inline val Context.textServicesManager get() = getSystemService(
        Context.TEXT_SERVICES_MANAGER_SERVICE) as TextServicesManager

inline val Context.wifiP2pManager get() = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager

inline val Context.inputManager @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
get() = getSystemService(Context.INPUT_SERVICE) as InputManager

inline val Context.mediaRouter @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
get() = getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter

inline val Context.nsdManager @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
get() = getSystemService(Context.NSD_SERVICE) as NsdManager

inline val Context.displayManager @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
get() = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager

inline val Context.userManager @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
get() = getSystemService(Context.USER_SERVICE) as UserManager

inline val Context.bluetoothManager @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
get() = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

inline val Context.appOpsManager @TargetApi(Build.VERSION_CODES.KITKAT)
get() = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

inline val Context.captioningManager @TargetApi(Build.VERSION_CODES.KITKAT)
get() = getSystemService(Context.CAPTIONING_SERVICE) as CaptioningManager

inline val Context.consumerIrManager @TargetApi(Build.VERSION_CODES.KITKAT)
get() = getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager

inline val Context.printManager @TargetApi(Build.VERSION_CODES.KITKAT)
get() = getSystemService(Context.PRINT_SERVICE) as PrintManager

inline val Context.appWidgetManager @TargetApi(Build.VERSION_CODES.LOLLIPOP)
get() = getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager

inline val Context.batteryManager @TargetApi(Build.VERSION_CODES.LOLLIPOP)
get() = getSystemService(Context.BATTERY_SERVICE) as BatteryManager

inline val Context.cameraManager @TargetApi(Build.VERSION_CODES.LOLLIPOP)
get() = getSystemService(Context.CAMERA_SERVICE) as CameraManager

inline val Context.jobScheduler @TargetApi(Build.VERSION_CODES.LOLLIPOP)
get() = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

inline val Context.launcherApps @TargetApi(Build.VERSION_CODES.LOLLIPOP)
get() = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

inline val Context.mediaProjectionManager @TargetApi(Build.VERSION_CODES.LOLLIPOP)
get() = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

inline val Context.mediaSessionManager @TargetApi(Build.VERSION_CODES.LOLLIPOP)
get() = getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager

inline val Context.restrictionsManager @TargetApi(Build.VERSION_CODES.LOLLIPOP)
get() = getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager

inline val Context.telecomManager @TargetApi(Build.VERSION_CODES.LOLLIPOP)
get() = getSystemService(Context.TELECOM_SERVICE) as TelecomManager

inline val Context.tvInputManager @TargetApi(Build.VERSION_CODES.LOLLIPOP)
get() = getSystemService(Context.TV_INPUT_SERVICE) as TvInputManager

inline val Context.subscriptionManager @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
get() = getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

inline val Context.usageStatsManager @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
get() = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

inline val Context.carrierConfigManager @TargetApi(Build.VERSION_CODES.M)
get() = getSystemService(Context.CARRIER_CONFIG_SERVICE) as CarrierConfigManager

inline val Context.midiManager
    @TargetApi(Build.VERSION_CODES.M)
    get() = getSystemService(Context.MIDI_SERVICE) as MidiManager

inline val Context.networkStatsManager
    @TargetApi(Build.VERSION_CODES.M)
    get() = getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager

inline val Context.shortcutManager
    @TargetApi(Build.VERSION_CODES.N_MR1)
    get() = getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager