package com.pokedroid.common.extension

import android.content.res.Resources
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Float
    get() = (Resources.getSystem().displayMetrics.density * this)

fun Int.toRupiahFormat() : String {
    val decimalFormat : DecimalFormat = DecimalFormat.getCurrencyInstance(Locale("in" , "ID")) as DecimalFormat
    val formatSymbols = DecimalFormatSymbols()
    formatSymbols.currencySymbol = "Rp. "
    formatSymbols.groupingSeparator = '.'
    return decimalFormat.format(this).split(",")[0]
}

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Float.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)

inline val Float.dpToPx: Float
    get() = this * Resources.getSystem().displayMetrics.density

inline val Int.dpToPx: Int
    get() = toFloat().dpToPx.toInt()

fun Int.checkIndexOutOfBounds(index: Int): Boolean {
    return index >= this || index < 0
}