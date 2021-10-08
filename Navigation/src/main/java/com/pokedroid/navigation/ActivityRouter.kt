@file:JvmName("ActivityRouter")

package com.pokedroid.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment


private const val PACKAGE_NAME = "com.android.frezzfarm"
private const val FEATURES = "com.frezzfarm.features"

/**
 * Create an Intent with [Intent.ACTION_VIEW] to an [AddressableActivity].
 */
fun Context.intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW)
            .setClassName(this, addressableActivity.className)
}

fun Context.intentToShortcut(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW)
        .setClassName(this, addressableActivity.className)
}

fun Context.intentToMainShortcut(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_MAIN)
        .setClassName(this, addressableActivity.className)
}

fun Fragment.intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW)
            .setClassName(this.requireContext(), addressableActivity.className)
}

fun Context.startFeature(
    addressableActivity: AddressableActivity,
    @AnimRes enterResId: Int = android.R.anim.fade_in,
    @AnimRes exitResId: Int = android.R.anim.fade_out,
    options: Bundle? = null,
    body: Intent.() -> Unit) {

    val intent = intentTo(addressableActivity)
    intent.body()

    if (options == null) {
        val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId)
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle())
    } else {
        ActivityCompat.startActivity(this, intent, options)
    }

}


fun Fragment.isAttachedToActivity(): Boolean {
    return isVisible && activity != null
}

fun Fragment.startFeature(
    addressableActivity: AddressableActivity,
    @AnimRes enterResId: Int = android.R.anim.fade_in,
    @AnimRes exitResId: Int = android.R.anim.fade_out,
    options: Bundle? = null,
    body: Intent.() -> Unit) {

    if (isAttachedToActivity()){
        val intent = intentTo(addressableActivity)
        intent.body()

        if (options == null) {
            val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(requireContext(), enterResId, exitResId)
            ActivityCompat.startActivity(requireActivity(), intent, optionsCompat.toBundle())
        } else {
            ActivityCompat.startActivity(requireActivity(), intent, options)
        }

    }

}

/**
 * An [android.app.Activity] that can be addressed by an intent.
 */
interface AddressableActivity {
    /**
     * The activity class name.
     */
    val className: String
}

/**
 * Base object for Features activities.
 */
object Activities {
    object ActivityLogin : AddressableActivity {
        override val className = "$FEATURES.auth.ActivityLogin"
    }
    object MainActivity : AddressableActivity {
        override val className = "$FEATURES.main.MainActivity"
    }
    object ActivityIndexData : AddressableActivity {
        override val className = "$FEATURES.index.ActivityIndexData"
    }
    object ActivityNotifications : AddressableActivity {
        override val className = "$FEATURES.notifications.ActivityNotification"
    }
    object ActivityDetailKolam : AddressableActivity {
        override val className = "$FEATURES.kolam.ActivityDetailKolam"
    }
    object ActivityDetailPemijahan : AddressableActivity {
        override val className = "$FEATURES.pemijahan.ActivityDetailPemijahan"
    }
    object ActivityDetailPakan : AddressableActivity {
        override val className = "$FEATURES.pakan.ActivityDetailPakan"
    }
}
/*

object FragmentName {
    object FragmentOrderType : AddressableActivity {
        override val className = "$FEATURES.orders.fragment_orders.FragmentOrderType"
    }
}

object Services {
    object UploadReview : AddressableActivity {
        override val className = "$FEATURES.review.uploadservice.UploadManagerService"
    }
}*/
