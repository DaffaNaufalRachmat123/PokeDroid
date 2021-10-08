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

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.annotation.ColorInt
import android.view.View
import android.view.WindowManager

// SYSTEM BARS

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
inline fun Activity.setSystemBarColor(@ColorInt color: Int) {
    setStatusBarColor(color)
    setNavigationBarColor(color)
}

inline fun Activity.supportSetSystemBarColor(@ColorInt color: Int) {
    onLollipop {
        setSystemBarColor(color)
    }
}

@TargetApi(Build.VERSION_CODES.KITKAT)
inline fun Activity.setTranslucentSystemBars(enabled: Boolean = true) {
    setTranslucentStatusBar(enabled)
    setTranslucentNavigationBar(enabled)
}

inline fun Activity.supportSetTranslucentSystemBars(enabled: Boolean = true) {
    onKitKat {
        setTranslucentSystemBars(enabled)
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
inline fun Activity.setTransparentSystemBars(enabled: Boolean = true) {
    setTransparentStatusBar(enabled)
    setTransparentNavigationBar(enabled)
}

inline fun Activity.supportSetTransparentSystemBars(enabled: Boolean = true) {

    onLollipop {
        setTransparentSystemBars(enabled)
    }
}

inline fun Activity.setSystemBarsHidden(hidden: Boolean = true) {
    setStatusBarHidden(hidden)
    setNavigationBarHidden(hidden)
}

// STATUS BAR
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
inline fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color
}

inline fun Activity.supportSetStatusBarColor(@ColorInt color: Int) {
    onLollipop {
        setStatusBarColor(color)
    }
}

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
inline fun Activity.setDrawUnderStatusBar(enabled: Boolean = true) {
    val decorView = window.decorView
    val systemUiVisibility = decorView.systemUiVisibility
    val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    if (enabled) {
        decorView.systemUiVisibility = systemUiVisibility or flags
    } else {
        decorView.systemUiVisibility = systemUiVisibility and flags.inv()
    }
}

inline fun Activity.supportSetDrawUnderStatusBar(enabled: Boolean = true) {
    onJellyBean {
        setDrawUnderStatusBar(enabled)
    }
}

@TargetApi(Build.VERSION_CODES.KITKAT)
inline fun Activity.setTranslucentStatusBar(enabled: Boolean = true) {
    val winParams = window.attributes
    if (enabled) {
        winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    } else {
        winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
    }
}

inline fun Activity.supportSetTranslucentStatusBar(enabled: Boolean = true) {
    onKitKat {
        setTranslucentStatusBar(enabled)
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
inline fun Activity.setTransparentStatusBar(enabled: Boolean = true) {
    if (enabled) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        setStatusBarColor(Color.TRANSPARENT)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }

    setDrawUnderStatusBar(enabled)
}

inline fun Activity.supportSetTransparentStatusBar(enabled: Boolean = true) {
    onLollipop {
        setTransparentStatusBar(enabled)
    }
}

inline fun Activity.setStatusBarHidden(hidden: Boolean = true) {
    if (hidden) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

// NAVIGATION BAR

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
inline fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    window.navigationBarColor = color
}

inline fun Activity.supportSetNavigationBarColor(@ColorInt color: Int) {
    onLollipop {
        setNavigationBarColor(color)
    }
}

@TargetApi(Build.VERSION_CODES.KITKAT)
inline fun Activity.setTranslucentNavigationBar(enabled: Boolean = true) {
    val winParams = window.attributes
    if (enabled) {
        winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
    } else {
        winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION.inv()
    }
}

inline fun Activity.supportSetTranslucentNavigationBar(enabled: Boolean = true) {
    onKitKat {
        setTranslucentNavigationBar(enabled)
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
inline fun Activity.setTransparentNavigationBar(enabled: Boolean = true) {
    if (enabled) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}

inline fun Activity.supportSetTransparentNavigationBar(enabled: Boolean = true) {
    onLollipop {
        setTransparentNavigationBar(enabled)
    }
}

@TargetApi(Build.VERSION_CODES.KITKAT)
inline fun Activity.setNavigationBarHidden(enabled: Boolean = true) {
    val decorView = window.decorView
    var  systemUiVisibility = decorView.systemUiVisibility
    if (enabled) {
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    } else {
        systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
        systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_IMMERSIVE.inv()
        systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
        decorView.systemUiVisibility = systemUiVisibility
    }
}

inline fun Activity.supportSetNavigationBarHidden(enabled: Boolean = true) {
    onKitKat {
        setNavigationBarHidden(enabled)
    }
}

@TargetApi(Build.VERSION_CODES.O)
inline fun Activity.setLightNavigationBar(enabled: Boolean = true) {
    val decorView = window.decorView
    val systemUiVisibility = decorView.systemUiVisibility
    if (enabled) {
        decorView.systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    } else {
        decorView.systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    }
}

inline fun Activity.supportSetLightNavigationBar(enabled: Boolean = true) {
    onOreo {
        setLightNavigationBar(enabled)
    }
}