package com.pokedroid.core.local.prefs

import com.chibatching.kotpref.KotprefModel

object AppPrefs : KotprefModel(){
    override val commitAllPropertiesByDefault: Boolean = true

    var hasloginFromIntent by booleanPref(default = false)
    var collectionHasLoaded by booleanPref(default = false)
    var uploadMenuIsRunning by booleanPref(default = false)
    var uploadReviewIsRunning by booleanPref(default = false)
}