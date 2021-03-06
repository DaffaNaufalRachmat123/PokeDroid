@file:JvmName("CollectionExt")
@file:Suppress("unused")
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

package com.pokedroid.common.extension

import java.util.*

inline fun <T> Array<T>.forEachByIndex(action: (T) -> Unit) {
    val lastIndex = size - 1
    for (i in 0..lastIndex) {
        action(get(i))
    }
}

inline fun <T> Array<T>.forEachWithIndex(action: (Int, T) -> Unit) {
    val lastIndex = size - 1
    for (i in 0..lastIndex) {
        action(i, get(i))
    }
}

inline fun <T> Array<T>.forEachReversedByIndex(action: (T) -> Unit) {
    var  i = size - 1
    while (i >= 0) {
        action(get(i))
        i--
    }
}

inline fun <T> Array<T>.forEachReversedWithIndex(action: (Int, T) -> Unit) {
    var  i = size - 1
    while (i >= 0) {
        action(i, get(i))
        i--
    }
}

inline fun <T> List<T>.forEachByIndex(action: (T) -> Unit) {
    val lastIndex = size - 1
    for (i in 0..lastIndex) {
        action(get(i))
    }
}

inline fun <T> List<T>.forEachWithIndex(action: (Int, T) -> Unit) {
    val lastIndex = size - 1
    for (i in 0..lastIndex) {
        action(i, get(i))
    }
}

inline fun <T> List<T>.forEachReversedByIndex(action: (T) -> Unit) {
    var  i = size - 1
    while (i >= 0) {
        action(get(i))
        i--
    }
}

inline fun <T> List<T>.forEachReversedWithIndex(action: (Int, T) -> Unit) {
    var  i = size - 1
    while (i >= 0) {
        action(i, get(i))
        i--
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun MutableList<*>.swap(from: Int, to: Int) {
    Collections.swap(this, from, to)
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> List<T>.swapped(from: Int, to: Int): List<T> {
    val copy = toMutableList()
    copy.swap(from, to)
    return copy
}

/** Null check for multiple objects at a time*/
inline fun <T : Any, R : Any> Collection<T?>.whenAllNotNull(block: (List<T>) -> R) {
    if (this.all { it != null }) {
        block(this.filterNotNull())
    }
}

/** Null check for at least one object at a time*/
inline fun <T : Any, R : Any> Collection<T?>.whenAnyNotNull(block: (List<T>) -> R) {
    if (this.any { it != null }) {
        block(this.filterNotNull())
    }
}

fun <T> emptyMutableList(): MutableList<T> = ArrayList<T>()