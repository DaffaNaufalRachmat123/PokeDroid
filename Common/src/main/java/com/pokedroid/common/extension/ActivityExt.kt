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

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewbinding.ViewBinding
import com.pokedroid.common.R

fun Activity.isFromDeepLink() = intent?.getBooleanExtra("is_deep_link_flag", false) ?: false

inline val Activity.contentView: View
    get() = findViewById(android.R.id.content)

inline val Activity.act: Context
    get() = this

/**
 * @param activity
 * @param id
 * @return int from resources by id, more concretely from dimens.xml file.
 */
fun Activity.getIntFromRes(id: Int): Int {
    val resources = this.resources ?: return 0
    return resources.getInteger(id)
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(crossinline factory: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        factory(layoutInflater)
    }


// KEYBOARD

inline fun Activity.hideInputMethod() {

    inputMethodManager.hideSoftInputFromWindow(window.peekDecorView().windowToken, 0)
}

inline fun Activity.hideInputMethod(v: View) {
    val imm: InputMethodManager = v.context
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(
        v.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )

    hideInputMethod()
}

inline fun Activity.showInputMethod(v: EditText) {
    v.requestFocus()
    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED)
}

// FINISHING

inline fun Activity.finishWithoutTransition() {
    overridePendingTransition(0, 0)
    finish()
}

inline fun Activity.supportFinishAfterTransition() {
    ActivityCompat.finishAfterTransition(this)
}

inline fun Activity.supportFinishAffinity() {
    ActivityCompat.finishAffinity(this)
}

inline fun Activity.finishWithResult(resultCode: Int, data: Intent) {
    setResult(resultCode, data)
    finish()
}
// LOCK ORIENTATION

inline fun Activity.lockCurrentScreenOrientation(orientation: Int = resources.configuration.orientation) {
    requestedOrientation = when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        else -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }
}

inline fun Activity.unlockScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
}

inline fun <T> Activity.extra(key: String): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        @Suppress("UNCHECKED_CAST")
        intent?.extras?.get(key) as T
    }
}

inline fun <T> Activity.extraOrNull(key: String): Lazy<T?> {
    return lazy(LazyThreadSafetyMode.NONE) {
        @Suppress("UNCHECKED_CAST")
        intent?.extras?.get(key) as? T?
    }
}


const val KEY_RESULT = "KEY_RESULT"
const val KEY_CONTENT = "KEY_CONTENT"

const val REQUEST_CODE = 2304
const val WRITE_PERMISSION = 2134
