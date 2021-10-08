package com.pokedroid.core.local.prefs

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/*
 Shared prefs usage
 val prefs = defaultPrefs(this)
 prefs[KEY] = "any_type_of_value" //setter
 val value: String? = prefs[KEY] //getter
 val anotherValue: Int? = prefs[KEY, 10] //getter with default value
*/
object SharedPrefsManager : KoinComponent {
    private const val PREFS_FILENAME = "pk_preference"

    val sharedPrefsManager: SharedPreferences by inject()

    fun initSharedPrefs(context: Context): SharedPreferences = context.getSharedPreferences(
        PREFS_FILENAME, Context.MODE_PRIVATE)

    fun initCustomPrefs(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
}

private inline fun SharedPreferences.edit(operation: SharedPreferences.Editor.() -> Unit) {
    val editor: SharedPreferences.Editor = edit()

    operation(editor)

    editor.apply()
}

/**
 * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
 */
operator fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit { putString(key, value as String) }
        is Int? -> edit { putInt(key, value as Int) }
        is Boolean? -> edit { putBoolean(key, value as Boolean) }
        is Float? -> edit { putFloat(key, value as Float) }
        is Long? -> edit { putLong(key, value as Long) }

        else -> throw UnsupportedOperationException()
    }
}

/**
 * finds value on given key.
 * [T] is the type of value
 * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
 */
inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T = when (T::class) {
    String::class -> getString(key, defaultValue as? String ?: "") as T
    Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
    Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
    Float::class -> getFloat(key, defaultValue as? Float ?: -1F) as T
    Long::class -> getLong(key, defaultValue as? Long ?: -1L) as T

    else -> throw UnsupportedOperationException()
}