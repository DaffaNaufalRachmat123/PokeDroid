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

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi

inline fun Drawable.toBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
    val bitmap: Bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, config)
    } else {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, config)
    }

    if (this is BitmapDrawable) {
        if (this.bitmap != null) {
            return this.bitmap
        }
    }

    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

inline fun Drawable.tint(@ColorInt color: Int,
                         mode: Mode = Mode.SRC_IN,
                         mutate: Boolean = true) {

    colorFilter(color, mode)
    if (mutate) {
        mutate()
    }
}

fun Drawable.colorFilter(color: Int, mode: Mode = Mode.SRC_ATOP) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, mode.getBlendMode())
    } else {
        @Suppress("DEPRECATION")
        setColorFilter(color, mode.getPorterDuffMode())
    }
}

// This class is needed to call the setColorFilter
// with different BlendMode on older API (before 29).
enum class Mode {
    CLEAR,
    SRC,
    DST,
    SRC_OVER,
    DST_OVER,
    SRC_IN,
    DST_IN,
    SRC_OUT,
    DST_OUT,
    SRC_ATOP,
    DST_ATOP,
    XOR,
    DARKEN,
    LIGHTEN,
    MULTIPLY,
    SCREEN,
    ADD,
    OVERLAY;

    @RequiresApi(29)
    fun getBlendMode(): BlendMode =
            when (this) {
                CLEAR -> BlendMode.CLEAR
                SRC -> BlendMode.SRC
                DST -> BlendMode.DST
                SRC_OVER -> BlendMode.SRC_OVER
                DST_OVER -> BlendMode.DST_OVER
                SRC_IN -> BlendMode.SRC_IN
                DST_IN -> BlendMode.DST_IN
                SRC_OUT -> BlendMode.SRC_OUT
                DST_OUT -> BlendMode.DST_OUT
                SRC_ATOP -> BlendMode.SRC_ATOP
                DST_ATOP -> BlendMode.DST_ATOP
                XOR -> BlendMode.XOR
                DARKEN -> BlendMode.DARKEN
                LIGHTEN -> BlendMode.LIGHTEN
                MULTIPLY -> BlendMode.MULTIPLY
                SCREEN -> BlendMode.SCREEN
                ADD -> BlendMode.PLUS
                OVERLAY -> BlendMode.OVERLAY
            }

    fun getPorterDuffMode(): PorterDuff.Mode =
            when (this) {
                CLEAR -> PorterDuff.Mode.CLEAR
                SRC -> PorterDuff.Mode.SRC
                DST -> PorterDuff.Mode.DST
                SRC_OVER -> PorterDuff.Mode.SRC_OVER
                DST_OVER -> PorterDuff.Mode.DST_OVER
                SRC_IN -> PorterDuff.Mode.SRC_IN
                DST_IN -> PorterDuff.Mode.DST_IN
                SRC_OUT -> PorterDuff.Mode.SRC_OUT
                DST_OUT -> PorterDuff.Mode.DST_OUT
                SRC_ATOP -> PorterDuff.Mode.SRC_ATOP
                DST_ATOP -> PorterDuff.Mode.DST_ATOP
                XOR -> PorterDuff.Mode.XOR
                DARKEN -> PorterDuff.Mode.DARKEN
                LIGHTEN -> PorterDuff.Mode.LIGHTEN
                MULTIPLY -> PorterDuff.Mode.MULTIPLY
                SCREEN -> PorterDuff.Mode.SCREEN
                ADD -> PorterDuff.Mode.ADD
                OVERLAY -> PorterDuff.Mode.OVERLAY
            }
}