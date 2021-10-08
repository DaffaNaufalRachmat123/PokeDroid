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

import java.util.ArrayList

fun Number?.orZero(): Number = this ?: 0

fun Int?.aboveZero(): Boolean = this.orZero() > 0

fun String?.orStrip(): String = this ?: "-"

fun String?.orBlank(): String = this ?: ""

fun Float?.orZero(): Float = this ?: 0.0f

fun Double?.orZero(): Double = this ?: 0.0

fun ArrayList<Any>?.orEmpty(): ArrayList<Any> = this ?: ArrayList()

inline fun ifNotNull(vararg params: Any?, lambda: () -> Unit) =
        if (params.all { it != null }) {
            lambda()
            ExecutedCode(true)
        } else {
            ExecutedCode(false)
        }

inline fun ifAnyNull(vararg params: Any?, lambda: () -> Unit) =
        if (params.any { it == null }) {
            lambda()
            ExecutedCode(true)
        } else {
            ExecutedCode(false)
        }

inline fun ifAllNull(vararg params: Any?, lambda: () -> Unit) =
        if (params.all { it == null }) {
            lambda()
            ExecutedCode(true)
        } else {
            ExecutedCode(false)
        }