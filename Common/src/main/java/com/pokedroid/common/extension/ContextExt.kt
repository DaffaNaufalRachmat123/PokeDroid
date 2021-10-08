@file:Suppress("unused")

package com.pokedroid.common.extension

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.Intent.*
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityManagerCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.pokedroid.common.base.BaseApp
import com.pokedroid.common.extension.ValueHolder.VALUE


fun Context.getLifecycleFromContext(): Lifecycle? {
    return when (this) {
        is FragmentActivity -> this.lifecycle
        is ContextWrapper -> (this.baseContext as? FragmentActivity)?.lifecycle
        else -> null
    }
}

fun Context.isServiceRunning(service_class : Class<*>) : Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for(service in manager.getRunningServices(Int.MAX_VALUE))
        if(service_class.name == service.service.className)
            return true
    return false
}

val Context.lifecycleOwner: LifecycleOwner?
    get() {
        var context: Context? = this

        while (context != null && context !is LifecycleOwner) {
            val baseContext = (context as? ContextWrapper?)?.baseContext
            context = if (baseContext == context) null else baseContext
        }

        return if (context is LifecycleOwner) context else null
    }


/**
 * Exactly whether a device is low-RAM is ultimately up to the device configuration, but currently
 * it generally means something in the class of a 512MB device with about a 800x480 or less screen.
 * This is mostly intended to be used by apps to determine whether they should
 * turn off certain features that require more RAM.
 *
 * @return true if this is a low-RAM device.
 */
fun Context?.isLowRamDevice(): Boolean {
    val activityManager = this?.getSystemService<ActivityManager>()
    return if (activityManager != null)
        ActivityManagerCompat.isLowRamDevice(activityManager)
    else false
}


inline fun <reified T> Context.getSystemService(): T? =
    ContextCompat.getSystemService(this, T::class.java)

fun Context.getActivityPendingIntent(
    requestCode: Int = 0,
    intent: Intent,
    flags: Int = PendingIntent.FLAG_ONE_SHOT
): PendingIntent =
    PendingIntent.getActivity(this, requestCode, intent, flags)

fun Context.getBroadcastPendingIntent(
    requestCode: Int = 0,
    intent: Intent,
    flags: Int = PendingIntent.FLAG_ONE_SHOT
): PendingIntent =
    PendingIntent.getBroadcast(this, requestCode, intent, flags)

fun Context.inflaterView(@LayoutRes layoutRes: Int, parent: ViewGroup? = null): View =
    View.inflate(this, layoutRes, parent)


fun Context.callPhone(phoneNumber: String) {
    val i = getCallIntent(phoneNumber)
    tryHandleIntent(i)
}

fun getCallIntent(phoneNumber: String): Intent {
    val intent = Intent("android.intent.action.CALL", Uri.parse("tel:$phoneNumber"))
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}


private fun Context.tryHandleIntent(intent: Intent): Boolean {
    if (canHandleIntent(intent)) {
        startActivity(intent)
        return true
    }
    return false
}

private fun Context.canHandleIntent(intent: Intent) =
    packageManager.queryIntentActivities(intent, 0).size > 0


// RESOURCES
fun Context.resolveColor(@ColorRes @AttrRes id: Int) = with(TypedValue()) {
    when {
        theme.resolveAttribute(id, this, true) -> data
        isMarshmallow -> getColor(id)
        else -> getResColor(id)
    }
}

private fun Context.applyDimension(unit: Int, number: Number) =
    TypedValue.applyDimension(unit, number.toFloat(), resources.displayMetrics)

fun Context.dpToPx(dp: Number) = applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp)
fun Context.pxToDp(px: Number) = applyDimension(TypedValue.COMPLEX_UNIT_PX, px)
fun Context.spToPx(sp: Int) = applyDimension(TypedValue.COMPLEX_UNIT_PX, sp)
fun Context.pxToSp(px: Int) = applyDimension(TypedValue.COMPLEX_UNIT_PX, px)

fun Context.getResAnim(@AnimRes resId: Int): Animation = AnimationUtils.loadAnimation(this, resId)

fun Context.getResIntArray(@ArrayRes resId: Int): IntArray = resources.getIntArray(resId)

fun Context.getResStringArray(@ArrayRes resId: Int): Array<String> =
    resources.getStringArray(resId)

fun Context.getResTextArray(@ArrayRes resId: Int): Array<CharSequence> =
    resources.getTextArray(resId)

fun Context.getResTypedArray(@ArrayRes resId: Int): TypedArray = resources.obtainTypedArray(resId)

fun Context.getResBool(@BoolRes resId: Int): Boolean = resources.getBoolean(resId)

fun Context.getResDimen(@DimenRes resId: Int): Float = this.resources.getDimension(resId)

@Px
fun Context.getResDimenPx(@DimenRes resId: Int): Int = this.resources.getDimensionPixelSize(resId)

@Px
fun Context.getResDimenPxOffset(@DimenRes resId: Int): Int =
    this.resources.getDimensionPixelOffset(resId)

fun Context.getResFloat(@DimenRes resId: Int): Float {
    val value = VALUE
    resources.getValue(resId, value, true)
    return value.float
}

fun Context.getResInt(@IntegerRes resId: Int): Int = resources.getInteger(resId)

fun Context.getResVectorDrawable(@DrawableRes resId: Int): Drawable =
    VectorDrawableCompat.create(this.resources, resId, this.theme) as Drawable

fun Context.getResBitmap(@DrawableRes resId: Int): Bitmap? = getResDrawable(resId)?.toBitmap()

fun Context.getResourceColor(@AttrRes resource: Int): Int {
    val typedArray = obtainStyledAttributes(intArrayOf(resource))
    val attrValue = typedArray.getColor(0, 0)
    typedArray.recycle()
    return attrValue
}

@ColorInt
fun Context.getResColor(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

@ColorInt
fun Fragment.getResColor(@ColorRes resId: Int): Int =
    ContextCompat.getColor(requireContext(), resId)

/**
 * Returns the color state list for this resource
 */
fun Context.getResColorStateList(resId: Int): ColorStateList? =
    ContextCompat.getColorStateList(this, resId)

/**
 * Returns the drawable for this resource
 */
fun Context.getResDrawable(@DrawableRes resId: Int, tintColorInt: Int = 0): Drawable? {
    if (tintColorInt != 0) {
        val d = ContextCompat.getDrawable(this, resId)
        d?.tint(getResColor(tintColorInt))
        return d
    }

    return ContextCompat.getDrawable(this, resId)
}

fun Fragment.getResDrawable(@DrawableRes resId: Int, tintColorInt: Int = 0): Drawable? {
    if (tintColorInt != 0) {
        val d = ContextCompat.getDrawable(requireContext(), resId)
        d?.tint(getResColor(tintColorInt))
        return d
    }

    return ContextCompat.getDrawable(requireContext(), resId)
}

/**
 * Returns the font for this resource
 */
fun Context.getResFont(@FontRes resId: Int): Typeface = ResourcesCompat.getFont(this, resId)!!

// TINTED DRAWABLES

/**
 * Returns the tinted drawable
 */
fun Context.createTintedDrawable(
    @DrawableRes drawableRes: Int,
    @ColorInt color: Int,
    mode: Mode = Mode.SRC_IN
): Drawable {
    val drawable = getResDrawable(drawableRes)
    drawable?.tint(color, mode)

    return drawable!!
}

/**
 * Returns the tinted vector drawable
 */
fun Context.createTintedVectorDrawable(
    @DrawableRes drawableRes: Int,
    @ColorInt color: Int,
    mode: Mode = Mode.SRC_IN
): Drawable {
    val drawable = getResVectorDrawable(drawableRes)
    drawable.tint(color, mode)

    return drawable
}

object ValueHolder {
    val VALUE = TypedValue()
}

// DISPLAY

/**
 * Returns whether is portrait
 */
fun Context.isPortrait(): Boolean = !isLandscape()

/**
 * Returns whether is landscape
 */
fun Context.isLandscape(): Boolean {
    val rotation = getRotation()
    return rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270
}

/**
 * Returns the current rotation
 */
fun Context.getRotation(): Int = windowManager.defaultDisplay.rotation

val metrics = DisplayMetrics()

/**
 * Returns the screen height
 */
@Px
fun Context.getScreenHeight(): Int {
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.heightPixels
}

/**
 * Returns the screen width
 */
@Px
fun Context.getScreenWidth(): Int {
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

/**
 * Returns the real screen height
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@Px
fun Context.getRealScreenHeight(): Int {
    windowManager.defaultDisplay.getRealMetrics(metrics)
    return metrics.heightPixels
}

/**
 * Returns the real screen width
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@Px
fun Context.getRealScreenWidth(): Int {
    windowManager.defaultDisplay.getRealMetrics(metrics)
    return metrics.widthPixels
}

/**
 * Returns whether has navigation bar
 */
fun Context.hasNavigationBar(): Boolean {
    val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return id > 0 && resources.getBoolean(id)
}

// NETWORK
@Suppress("DEPRECATION")
val Context.isConnected: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        } else {
            connectivityManager.activeNetworkInfo?.isConnected.orFalse()
        }
    }

@get:RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
val Fragment.isConnected: Boolean
    get() = context?.isConnected ?: false



@SuppressLint("NewApi")
@Suppress("DEPRECATION")
fun getBoldString(textNotBoldFirst: String, textToBold: String, textNotBoldLast: String): Spanned {
    var resultant: String?

    resultant = "$textNotBoldFirst <b>$textToBold</b> $textNotBoldLast"
    resultant = resultant.replace("\n", "<br>")

    return when {
        isNougat -> Html.fromHtml(resultant, Html.FROM_HTML_MODE_LEGACY)
        else -> Html.fromHtml(resultant)
    }

}

fun drawableToBitmap(context: Context, resourceId: Int): Bitmap? {
    val vectorDrawable = ContextCompat.getDrawable(context, resourceId)
    val h = vectorDrawable?.intrinsicHeight
    val w = vectorDrawable?.intrinsicWidth
    w?.let { h?.let { it1 -> vectorDrawable.setBounds(0, 0, it, it1) } }
    val bm = w?.let { h?.let { it1 -> Bitmap.createBitmap(it, it1, Bitmap.Config.ARGB_8888) } }
    bm?.let { Canvas(it) }?.let { vectorDrawable.draw(it) }
    return bm
}


val Context.app: BaseApp
    get() = this.applicationContext as BaseApp

@Suppress("DEPRECATION")
fun Context.getVersionCode(): Int = packageManager.getPackageInfo(packageName, 0).versionCode

fun Context.getVersionName(): String = packageManager.getPackageInfo(packageName, 0).versionName
/*
fun Context.showOtherNotification(title: String, message: String) {
    val notificationManager =
        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notification: Notification

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("OtherNotification", "Other Notification", importance)
        notificationManager.createNotificationChannel(channel)

        notification = Notification.Builder(this, "OtherNotification")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.notif_icon)
            .build()
    } else {
        notification = NotificationCompat.Builder(this, "OtherNotification")
            .setSmallIcon(R.drawable.notif_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true).build()
    }

    notificationManager.notify(2828, notification)
}*/

/*fun Context.showNotification(title : String , message : String , r_code : Int? = null , pendingIntent : PendingIntent? = null , notification_type : String? = null){
    createNotificationChannel()
    notifyThis(title , message , r_code , pendingIntent , notification_type)
}*/

fun Context.createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name: CharSequence = "namad_daffa"
        val description = "description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("CHANNEL_ID", name, importance)
        channel.description = description
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(channel)
    }
}
/*
fun Context.notifyThis(title: String?, message: String? , r_code : Int? = null , pendingIntent : PendingIntent? = null , notification_type : String? = null) {
    var count_message = 0
    val inboxStyle = NotificationCompat.InboxStyle()
    val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_klinday_logo_2)
        .setContentTitle(title)
        .setPriority(NotificationCompat.PRIORITY_MAX)
    if(notification_type != null) {
        notification_type?.let {
            if (it == CHAT_CUSTOMER_RECEIVER) {
                mBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
                inboxStyle.addLine(message)
                mBuilder.setStyle(inboxStyle)
                mBuilder.setContentText("$count_message pesan baru")
                count_message += 1
            } else {
                mBuilder.setContentText(message)
            }
        }
    } else {
        mBuilder.setContentText(message)
    }
    pendingIntent?.let {
        mBuilder.setContentIntent(it)
    }
    val notificationManager = NotificationManagerCompat.from(this)
    // notificationId is a unique int for each notification that you must define
    notificationManager.notify(r_code ?: System.currentTimeMillis().toInt(), mBuilder.build())
}*/

fun Context.resolveThemeAttribute(@AttrRes resId: Int): TypedValue =
    TypedValue().apply { theme.resolveAttribute(resId, this, true) }

@ColorInt
fun Context.resolveThemeColor(@AttrRes attrId: Int): Int = resolveThemeAttribute(attrId).data

inline val Context.hasSoftNavigationKeys: Boolean
    get() {
        val display = windowManager.defaultDisplay

        val realMetrics = DisplayMetrics()
        display.getRealMetrics(realMetrics)

        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)

        return realMetrics.widthPixels > displayMetrics.widthPixels ||
                realMetrics.heightPixels > displayMetrics.heightPixels
    }


inline fun <reified T : AppCompatActivity> Context.intent() = Intent(this, T::class.java)

inline fun <reified T : AppCompatActivity> Context.intent(body: Intent.() -> Unit): Intent {
    val intent = Intent(this, T::class.java)
    intent.body()
    return intent
}

inline fun <reified T : AppCompatActivity> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    ContextCompat.startActivity(this, intent, null)
}

inline fun <reified T : AppCompatActivity> Context.startActivity(body: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.body()
    ContextCompat.startActivity(this, intent, null)
}

inline fun <reified T : AppCompatActivity> Context.startActivity(
    @AnimRes enterResId: Int = 0,
    @AnimRes exitResId: Int = 0
) {
    val intent = Intent(this, T::class.java)
    val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId)
    ContextCompat.startActivity(this, intent, optionsCompat.toBundle())
}

inline fun <reified T : AppCompatActivity> Context.startActivity(
    @AnimRes enterResId: Int = 0, @AnimRes exitResId: Int = 0,
    body: Intent.() -> Unit
) {
    val intent = Intent(this, T::class.java)
    intent.body()
    val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId)
    ContextCompat.startActivity(this, intent, optionsCompat.toBundle())
}

fun Context.share(text: String, subject: String = ""): Boolean {
    val intent = Intent()
    intent.type = "text/plain"
    intent.putExtra(EXTRA_SUBJECT, subject)
    intent.putExtra(EXTRA_TEXT, text)
    return try {
        startActivity(createChooser(intent, null))
        true
    } catch (e: ActivityNotFoundException) {
        false
    }
}

fun Context.email(
    address: Array<String> = emptyArray(),
    subject: String = "",
    text: String = ""
): Boolean {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, address)
    if (subject.isNotBlank()) intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    if (text.isNotBlank()) intent.putExtra(Intent.EXTRA_TEXT, text)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false
}

fun Context.browse(url: Uri?, newTask: Boolean = false): Boolean {
    val intent = Intent(ACTION_VIEW)
    intent.data = url
    if (newTask) intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
    return try {
        startActivity(intent)
        true
    } catch (e: Exception) {
        false
    }
}

fun Context.makeCall(tel: String): Boolean {
    return try {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel")))
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.sendSMS(number: String, text: String = ""): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
        intent.putExtra("sms_body", text)
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.rate(): Boolean =
    browse(Uri.parse("market://details?id=$packageName")) or browse(Uri.parse("http://play.google.com/store/apps/details?id=$packageName"))

inline val Context.navigationBarHeight
    get() = runCatching {
        resources.getIdentifier(
            "navigation_bar_height",
            "dimen",
            "android"
        ).run {
            resources.getDimensionPixelSize(this)
        }
    }.getOrNull()

fun Context.isActivityNotDestroyed(): Boolean {
    if (this is Activity) {
        if (this.isDestroyed || this.isFinishing) {
            return false
        }
    }
    return true
}