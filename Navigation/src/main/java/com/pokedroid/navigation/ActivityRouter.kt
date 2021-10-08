@file:JvmName("ActivityRouter")

package com.pokedroid.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment


private const val PACKAGE_NAME = "com.android.pokedroid"
private const val FEATURES = "com.pokedroid.features"

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
    object ActivityPokeDetail : AddressableActivity {
        override val className = "$FEATURES.main.ActivityPokeDetail"
    }
    object ActivityMyPokemon : AddressableActivity {
        override val className = "$FEATURES.main.ActivityMyPokemon"
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
