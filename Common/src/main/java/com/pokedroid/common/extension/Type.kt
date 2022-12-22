package com.pokedroid.common.extension

/**
 * @return `this` is not null, throws [exception] otherwise
 */
fun Boolean?.orFalse(): Boolean = this ?: false

fun Int?.orZero(): Int = this ?: 0

// endregion
// region Long
/**
 * @return `this` is not null, 0 otherwise
 */
// endregion
// region CharSequence
/**
 * @return `this` as a string if not null, empty string otherwise
 */
fun CharSequence?.orEmpty(): String = this?.toString() ?: ""

/**
 * Replaces [regex] with an empty string.
 *
 * @return the new string
 */