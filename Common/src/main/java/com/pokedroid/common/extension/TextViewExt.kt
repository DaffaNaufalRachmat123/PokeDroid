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

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.pokedroid.common.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

fun TextView.setTextRes(@StringRes textRes: Int) {
    text = resources.getText(textRes)
}

inline var TextView.compoundDrawableLeft: Drawable?
    get() {
        return if (compoundDrawables.isEmpty()) {
            null
        } else {
            compoundDrawables[0]
        }
    }
    set(value) {
        setOptionalCompoundDrawables(left = value)
    }

inline var TextView.compoundDrawableStart: Drawable?
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    get() {
        return if (compoundDrawablesRelative.isEmpty()) {
            null
        } else {
            compoundDrawablesRelative[0]
        }
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    set(value) {
        setOptionalRelativeCompoundDrawables(start = value)
    }

inline var TextView.compoundDrawableTop: Drawable?
    get() {
        return if (compoundDrawables.isEmpty()) {
            null
        } else {
            compoundDrawables[1]
        }
    }
    set(value) {
        setOptionalCompoundDrawables(top = value)
    }

inline var TextView.compoundDrawableRight: Drawable?
    get() {
        return if (compoundDrawables.isEmpty()) {
            null
        } else {
            compoundDrawables[2]
        }
    }
    set(value) {
        setOptionalCompoundDrawables(right = value)
    }

inline var TextView.compoundDrawableEnd: Drawable?
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    get() {
        return if (compoundDrawablesRelative.isEmpty()) {
            null
        } else {
            compoundDrawablesRelative[2]
        }
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    set(value) {
        setOptionalRelativeCompoundDrawables(end = value)
    }

inline var TextView.compoundDrawableBottom: Drawable?
    get() {
        return if (compoundDrawables.isEmpty()) {
            null
        } else {
            compoundDrawables[3]
        }
    }
    set(value) {
        setOptionalCompoundDrawables(bottom = value)
    }

inline fun TextView.setOptionalCompoundDrawables(
    left: Drawable? = compoundDrawableLeft,
    top: Drawable? = compoundDrawableTop,
    right: Drawable? = compoundDrawableRight,
    bottom: Drawable? = compoundDrawableBottom
) {
    setCompoundDrawables(left, top, right, bottom)
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
inline fun TextView.setOptionalRelativeCompoundDrawables(
    start: Drawable? = compoundDrawableStart,
    top: Drawable? = compoundDrawableTop,
    end: Drawable? = compoundDrawableEnd,
    bottom: Drawable? = compoundDrawableBottom
) {
    setCompoundDrawablesRelative(start, top, end, bottom)
}

fun String.txtFromHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
} else {
    @Suppress("DEPRECATION")
    Html.fromHtml(this)
}

fun TextView.setDrawableLeft(@DrawableRes drawableRes: Int) {
    val drawable = VectorDrawableCompat.create(resources, drawableRes, this.context.theme)
    setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

fun TextView.textChanges(): Flow<String> {
    return callbackFlow<String> {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                offer(s.toString())
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text.toString()) }
}

fun TextView.setMultipleText(multiTextList : MutableList<MultipleText>) {
    for((index , text) in multiTextList.withIndex()){
        this.append(text.spannableWord)
    }
}

data class MultipleText(
    var index : Int ,
    var spannableWord : Spannable
)

const val DEBOUNCE_PERIOD: Long = 300L
val EditText.textChanges: Flow<String>
    get() = this.textChanges()
        .drop(1)
        .debounce(DEBOUNCE_PERIOD)
        .map(String::trim)
        .conflate()