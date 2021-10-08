package com.pokedroid.common.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.pokedroid.common.extension.currentSdk
import com.pokedroid.common.extension.sharedPrefs
import com.github.ajalt.timberkt.i
import com.pokedroid.core.local.prefs.get
import com.pokedroid.core.local.prefs.set
import java.util.*

object ThemeManager {
    private const val PREF_KEY = "theme_style"
    private const val THEME_STYLE_LIGHT = "light"
    private const val THEME_STYLE_DARK = "dark"
    private const val THEME_STYLE_DEFAULT = "default"
    private const val THEME_STYLE_AUTO = "auto"

    private val themeStyleChanger = AppCompatDelegate::setDefaultNightMode

    enum class Mode(val value: String) {
        DEFAULT(THEME_STYLE_DEFAULT),
        AUTO(THEME_STYLE_AUTO),
        LIGHT(THEME_STYLE_LIGHT),
        DARK(THEME_STYLE_DARK)
    }

    fun setThemeStyle(mode: Mode) = mode.also { sharedPrefs[PREF_KEY] = it.value }

    fun getThemeStyle(): String = sharedPrefs[PREF_KEY, Mode.DEFAULT.value]

    fun useDarkMode(): Boolean {
        return getThemeStyle() == Mode.DARK.value
    }

    fun Context.isDarkMode(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

    fun applyTheme(mode: Mode) {
        i { "ThemeStyle : ${mode.value}" }
        when (mode) {
            Mode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO.let(themeStyleChanger)
            Mode.DARK -> AppCompatDelegate.MODE_NIGHT_YES.let(themeStyleChanger)
            Mode.AUTO -> {

                if (isNight()) {
                    AppCompatDelegate.MODE_NIGHT_YES.let(themeStyleChanger)
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO.let(themeStyleChanger)
                }
            }
            Mode.DEFAULT -> {
                if (currentSdk > Build.VERSION_CODES.P) {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM.let(themeStyleChanger)
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO.let(themeStyleChanger)
                }

            }
        }
    }

    fun valueToModeMapper(value: String): Mode {
        return when (value) {
            Mode.LIGHT.value -> {
                Mode.LIGHT
            }
            Mode.DARK.value -> {
                Mode.DARK
            }
            Mode.AUTO.value -> {
                Mode.AUTO
            }
            else -> {
                Mode.DEFAULT
            }
        }
    }

    private fun isNight(): Boolean = Calendar.getInstance().run {
        val hour = get(Calendar.HOUR_OF_DAY)
        i { "hour $hour" }
        return@run hour < 6 || hour >= 18
    }
}