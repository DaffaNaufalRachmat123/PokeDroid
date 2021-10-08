/*
package com.pokedroid.core.local.prefs

import com.chibatching.kotpref.KotprefModel


object UserSession : KotprefModel() {
    override val commitAllPropertiesByDefault: Boolean = true

    const val PREF_NAME = "session_login"
    const val USER_ID = "id"
    const val IS_LOGGED_IN = "is_logged_in"
    const val USER_TOKEN = "user_token"

    var isLoggedIn by booleanPref(default = false, key = IS_LOGGED_IN)
    var userId by intPref(default = 0, key = USER_ID)
    var userToken by stringPref(default = "" , key = USER_TOKEN)

    fun doLogin(
            id: Int,
            token : String
    ) {
        UserSession.apply {
            isLoggedIn = true
            userId = id
            userToken = token
        }
    }

    override fun toString(): String {
        return "isLoggedIn: $isLoggedIn"
    }

}*/
