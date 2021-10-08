package com.pokedroid.common.extension

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.core.database.getIntOrNull

fun Cursor.getStringValue(key: String) = getString(getColumnIndex(key))

fun Cursor.getIntValue(key: String) = getInt(getColumnIndex(key))

fun Cursor.getIntValueOrNull(key: String) = getIntOrNull(getColumnIndex(key))

fun Cursor.getLongValue(key: String) = getLong(getColumnIndex(key))

fun Cursor.getBlobValue(key: String) = getBlob(getColumnIndex(key))

fun Context.getLatestMediaByDateId(uri: Uri): Long {
    val projection = arrayOf(BaseColumns._ID)
    val sortOrder = "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"
    var cursor: Cursor? = null
    try {
        cursor = contentResolver.query(uri, projection, null, null, sortOrder)
        if (cursor?.moveToFirst() == true) {
            return cursor.getLongValue(BaseColumns._ID)
        }
    } finally {
        cursor?.close()
    }
    return 0
}
