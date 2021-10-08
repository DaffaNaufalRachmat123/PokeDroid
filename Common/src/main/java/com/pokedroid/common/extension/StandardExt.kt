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
import android.os.Build
import android.os.Parcel
import com.github.ajalt.timberkt.Timber.i
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/** Write a boolean to a Parcel (copied from Parcel, where this is @hidden). */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun Parcel.writeBoolean(value: Boolean) = writeInt(if (value) 1 else 0)

/** Read a boolean from a Parcel (copied from Parcel, where this is @hidden). */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun Parcel.readBoolean() = readInt() != 0

fun <T> Collection<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}

inline fun <T> semiSafeLazy(noinline initializer: () -> T) = lazy(LazyThreadSafetyMode.PUBLICATION, initializer)

inline fun <T> unsafeLazy(noinline initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Double.roundHalfUp(scale: Int = 1) = BigDecimal(this).setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()

@SuppressLint("SimpleDateFormat")
private var output = SimpleDateFormat("yyyy-MM-dd")

fun String.convertDate(): String {
    val parsed = output.parse(this) ?: Date()
    return output.format(parsed)
}

@Synchronized
fun isRunningTest(): Boolean {
    return try {
        Class.forName("android.support.test.espresso.Espresso")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

val isEmulator = Build.FINGERPRINT.startsWith("generic")
        || Build.FINGERPRINT.startsWith("unknown")
        || Build.MODEL.contains("google_sdk")
        || Build.MODEL.contains("Emulator")
        || Build.MODEL.contains("Android SDK built for x86")
        || Build.MANUFACTURER.contains("Genymotion")
        || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
        || Build.PRODUCT == "google_sdk"

fun File.readFileToString(): String {
    return if (this.exists()) this.bufferedReader().use { it.readText() } else ""
}

fun <T> measureTimeAndPrint(tag: String, body: () -> T): T {
    val start = System.currentTimeMillis()
    val result = body()
    val end = System.currentTimeMillis()

    i { "measureTime $tag ${(end - start)} ms" }
    return result
}

fun Int.have(value: Int): Boolean = if (this == 0 || value == 0) false
else if (this == 0 && value == 0) true
else {
    ((this > 0 && value > 0) || (this < 0 && value < 0)) &&
            this and value == value
}

inline fun <T> Iterable<T>.firstIndexOrNull(predicate: (T) -> Boolean): Int? {
    return this.mapIndexed { index, item -> Pair(index, item) }
            .firstOrNull { predicate(it.second) }
            ?.first
}

/**
 * Conversion from [LocalDateTime] to [String].
 *
 * @param format [LocalDateTimeFormat]
 * @return [String]
 */
fun LocalDateTime.toString(format: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern(format)
        format(formatter)
    } catch (e: Exception) {
        ""
    }
}
