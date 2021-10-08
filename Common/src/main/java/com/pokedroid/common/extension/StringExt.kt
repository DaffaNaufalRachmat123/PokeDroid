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
import android.graphics.Typeface
import android.net.Uri
import android.net.UrlQuerySanitizer
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Patterns
import com.github.ajalt.timberkt.Timber.d
import com.github.ajalt.timberkt.Timber.w
import org.json.JSONArray
import org.json.JSONException
import timber.log.Timber
import java.io.PrintWriter
import java.io.StringWriter
import java.math.BigInteger
import java.net.URLDecoder
import java.net.UnknownHostException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.LinkedHashMap

@PkUtils
fun String?.orDefaultImage(): String {
    return if (this.isNullOrEmpty()) "https://digitiket.com/blog/assets/images/nopic.png"
    else this
}

inline fun CharSequence.isEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

inline fun CharSequence.isIp() = Patterns.IP_ADDRESS.matcher(this).matches()
