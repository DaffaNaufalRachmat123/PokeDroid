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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.IntegerRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.pokedroid.common.R
import com.google.android.material.snackbar.Snackbar


fun View.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.show()
    return snack
}

fun View.snackBar(@StringRes() text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.show()
    return snack
}

fun View.snackBarWhite(@StringRes text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.view.setBackgroundColor(ContextCompat.getColor(context,android.R.color.white))
    val textView = snack.view.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(Color.parseColor("#606060"))
    snack.init()
    snack.show()
    return snack
}

fun View.snackBarWhite(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.config(context)
    val textView = snack.view.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
    snack.init()
    snack.show()
    return snack
}

fun Fragment.snackBarWhite(@StringRes text: Int, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar =
        view?.snackBarWhite(text, duration, init)!!

fun Fragment.snackBarWhite(text: CharSequence, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar =
        view?.snackBarWhite(text, duration, init)!!

fun Fragment.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar =
        view?.snackBar(text, duration, init)!!

fun Fragment.snackBar(@StringRes text: Int, duration: Int = Snackbar.LENGTH_LONG, init: Snackbar.() -> Unit = {}): Snackbar =
        view?.snackBar(text, duration, init)!!

fun Activity.snackBar(@StringRes text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val v = this.findViewById<View>(android.R.id.content)
    return v.snackBar(text, duration, init)
}

fun Activity.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val v = this.findViewById<View>(android.R.id.content)
    return v.snackBar(text, duration, init)
}

fun Activity.snackBarWhite(@StringRes text: Int, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val v = this.findViewById<View>(android.R.id.content)
    return v.snackBarWhite(text, duration, init)
}

fun Activity.snackBarWhite(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val v = this.findViewById<View>(android.R.id.content)
    return v.snackBarWhite(text, duration, init)
}

fun CoordinatorLayout.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    return this.snackBarWhite(text, duration, init)
}

fun RelativeLayout.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    return this.snackBarWhite(text, duration, init)
}

@SuppressLint("ResourceType")
fun Snackbar.action(@IntegerRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

fun FragmentActivity.setContentFragment(containerViewId: Int, f: () -> Fragment): Fragment? {
    val manager = supportFragmentManager
    val fragment = manager.findFragmentById(containerViewId)
    fragment?.let { return it }

    return f().apply {
        manager.beginTransaction().add(containerViewId, this).commit()
    }
}

inline fun Activity.getScreenHeight(): Int {
    (this.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay?.let {
        val size = Point()
        it.getSize(size)
        return size.y
    }
    return 0
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Snackbar.config(context: Context){
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    this.view.layoutParams = params
    ViewCompat.setElevation(this.view, 6f)
}
