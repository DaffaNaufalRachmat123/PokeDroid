package com.pokedroid.common.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.*
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.pokedroid.common.BuildConfig
import com.github.ajalt.timberkt.d
import com.pokedroid.core.local.prefs.SharedPrefsManager
import com.pokedroid.core.local.prefs.get
import com.pokedroid.core.local.prefs.set
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.math.BigDecimal
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLDecoder
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/**
 * Markers to isolate respective extension @PkUtils functions to their extended class
 * Avoids having a whole bunch of methods for nested calls
 */
@DslMarker
annotation class PkUtils

@PkUtils
inline val debugMode: Boolean
    get() = BuildConfig.DEBUG


@PkUtils
fun baseUrl(): String {
    return BuildConfig.BASE_URL
}

/**
 * Return true if this [Context] is available.
 * Availability is defined as the following:
 * + [Context] is not null
 * + [Context] is not destroyed (tested with [FragmentActivity.isDestroyed] or [Activity.isDestroyed])
 */
@PkUtils
fun Context?.isAvailable(): Boolean {
    if (this == null) {
        return false
    } else if (this !is Application) {
        if (this is FragmentActivity) {
            return !this.isDestroyed
        } else if (this is Activity) {
            return !this.isDestroyed
        }
    }
    return true
}

val Context.isDarkMode: Boolean
    get() {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }

@PkUtils
fun getDistance(sLat: Double, sLon: Double, dLat: String, dLon: String): String {
    val distance: Double
    val hasil: String

    val pLoc = Location("ploc")
    pLoc.latitude = sLat
    pLoc.longitude = sLon

    val pLoc2 = Location("ploc2")
    pLoc2.latitude = dLat.toDouble()
    pLoc2.longitude = dLon.toDouble()

    distance = pLoc.distanceTo(pLoc2).toDouble()
    hasil = if (distance > 500) {
        "${(distance / 1000).roundHalfUp()} km"
    } else {
        "${distance.roundHalfUp(0)} m"
    }

    return hasil
}

@PkUtils
inline fun Context.hasInternet(onConnected: () -> Unit, crossinline onDisConnected: () -> Unit) =
        if (isConnected) onConnected()
        else onDisConnected()

@PkUtils
fun Double?.round(decimalCount: Int = 1): String = BigDecimal(this.orZero()).setScale(decimalCount, BigDecimal.ROUND_HALF_UP).toString()

/**
 * Use block for autocloseables
 */
inline fun <T : AutoCloseable, R> T.use(block: (T) -> R): R {
    var closed = false
    try {
        return block(this)
    } catch (e: Exception) {
        closed = true
        try {
            close()
        } catch (closeException: Exception) {
            e.addSuppressed(closeException)
        }
        throw e
    } finally {
        if (!closed) {
            close()
        }
    }
}

@PkUtils
fun Throwable.errorMessage(): String {
    val msg: String

    when (this) {
        is HttpException -> {
            d { "Error Type : HttpException" }
            val responseBody = this.response()?.errorBody()
            val code = response()?.code()
            msg = when (code) {
                500 -> {
                    "Terjadi masalah dengan server kami, coba lagi nanti"
                }
                404 -> {
                    "Gagal memuat data dari server"
                }
                else -> {
                    responseBody.getErrorMessage()
                }
            }
            println("HttpException checkApiError onError Code : $code : $msg")

        }
        is SocketTimeoutException -> {
            msg = "Timeout, Coba lagi"
        }
        else -> {
            d { "Error Type : $message"}
            msg = if (message == null || message?.startsWith("Unable to resolve host").orFalse() || message == "null" || message?.startsWith("Failed to connect to").orFalse()
                    || message?.startsWith("Write error: ssl").orFalse() || message?.startsWith("Value <!DOCTYPE ").orFalse()
            ) {
                "Tidak ada koneksi jaringan"
            } else {
                message ?: "Terjadi kesalahan, silahkan coba lagi"
            }


        }
    }
    println("ApiOnError : $msg")
    return msg
}

fun ResponseBody?.getErrorMessage(): String {
    return try {
        val jsonObject = JSONObject(this?.string() ?: "")
        println("jsonObjectError : $jsonObject")
        val errorMsg: String
        errorMsg = when {
            jsonObject.has("message") -> jsonObject.getString("message")
            jsonObject.has("errors") -> jsonObject.getString("errors")
            jsonObject.has("error") -> jsonObject.getString("error")
            jsonObject.has("info") -> jsonObject.getString("info")
            else -> "Terjadi kesalahan, Coba lagi"
        }
        return errorMsg
    } catch (e: JSONException) {
        e.message.toString()
    }
}

@PkUtils
fun Response<ResponseBody>.responseToString(): String {
    val strBuilder = AtomicReference(StringBuilder())
    val content = ByteArrayInputStream(this.toString().toByteArray())
    val reader = BufferedReader(InputStreamReader(content))

    while (reader.readLine() != null) {
        strBuilder.get().append(reader.readLine())
    }
    reader.close()

    return this.body()?.string() ?: throw JSONException("Failed parse response toString")
}

@PkUtils
fun getRandomPrefLocation(dataFavLoc: List<String>): String {
    val generator = Random()
    val randomIndex = generator.nextInt(dataFavLoc.size)
    return dataFavLoc[randomIndex]
}

@PkUtils
fun String.getQueryParams(): Map<String, String> {

    val queryPairs = LinkedHashMap<String, String>()
    val url = URL(this)
    val query = url.query
    val pairs = query?.split("&".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
            ?: arrayOf()
    for (pair in pairs) {
        val idx = pair.indexOf("=")
        queryPairs[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] = URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
    }

    return queryPairs
}



@PkUtils
val sharedPrefs: SharedPreferences
    get() = SharedPrefsManager.sharedPrefsManager

@PkUtils
fun firstInit(key: String) {
    sharedPrefs[key] = true
}

@PkUtils
fun getFirstInit(key: String): Boolean {
    return sharedPrefs[key]
}

const val CONNECTION_TIME_OUT = 30 * 1000
const val REQUEST_TIME_OUT = 2 * 60 * 1000

@PkUtils
fun uploadRestClient(): OkHttpClient.Builder {
    return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(REQUEST_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)

}

@PkUtils
fun requestUploadBuilder(url: String): Request.Builder {

    val urls = URL(url)
    return Request.Builder()
            .url(urls)
}

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
fun Any.wait(millis: Long) {
    (this as java.lang.Object).wait(millis)
}

internal const val MAX_ALPHA = 255

inline fun <T, R> T?.runIfNotNull(block: T.() -> R): R? = this?.block()


fun Context?.isInPortrait() = this?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT
fun Context.getAttrId(attrId: Int): Int {
    TypedValue().run {
        return when {
            !theme.resolveAttribute(attrId, this, true) -> INVALID_RESOURCE_ID
            else -> resourceId
        }
    }
}

fun hasMinimumSdk(minimumSdk: Int) = Build.VERSION.SDK_INT >= minimumSdk

fun hasMaximumSdk(maximumSdk: Int) = Build.VERSION.SDK_INT <= maximumSdk

@ColorInt
internal fun calculateColor(@ColorInt to: Int, ratio: Float): Int {
    val alpha = (MAX_ALPHA - (MAX_ALPHA * ratio)).toInt()
    return Color.argb(alpha, Color.red(to), Color.green(to), Color.blue(to))
}

//endregion

/**
 * For API level 28+ check if background is restricted
 * Else always return false
 *
 * @param context the context
 *
 * @return Boolean
 *
 * @see isBackgroundRestricted
 */
fun isBackgroundJobEnabled(context: Context): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        !activityManager.isBackgroundRestricted
    } else true
}


fun Context.showAlert(msg: String?, action: () -> Unit) {
    MaterialDialog(this).show {
        cornerRadius(16f)
        message(text = msg)
        positiveButton(text = "Tutup") {
            it.dismiss()
            action.invoke()
        }
    }
}

@PkUtils
fun doubleText(value: String): String = "$value.0"

fun postDelayed(delay: Long, action: () -> Unit) {
    Handler().postDelayed(action, delay)
}

@PkUtils
inline val Int.dp2Px: Int
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), Resources.getSystem().displayMetrics).toInt()


@SuppressLint("NewApi")
fun Context.getCapturedImage(selectedPhotoUri: Uri): Bitmap {

    return when {
        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                contentResolver,
                selectedPhotoUri
        )
        else -> {
            val source = ImageDecoder.createSource(contentResolver, selectedPhotoUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
}