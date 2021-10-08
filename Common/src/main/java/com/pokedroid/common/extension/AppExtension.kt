package com.pokedroid.common.extension

import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.pokedroid.common.base.BaseActivity
import com.pokedroid.common.utils.ViewState
import com.pokedroid.navigation.AddressableActivity
import com.pokedroid.navigation.intentTo
import com.pokedroid.navigation.isAttachedToActivity


fun Context.startFeature(
    addressableActivity: AddressableActivity,
    intentAction: Intent.() -> Unit = {}
) {

    val intent = intentTo(addressableActivity).apply(intentAction)
    startActivity(intent)

}

fun Fragment.startFeature(
    addressableActivity: AddressableActivity,
    intentAction: Intent.() -> Unit = {}
) {
    if (isAttachedToActivity()){
        val intent = intentTo(addressableActivity).apply(intentAction)
        startActivity(intent)
    }
}

fun <T> BaseActivity<*>.parseState(
    viewState: ViewState<T>,
    onSuccess: (T) -> Unit,
    onError: ((String?) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (viewState) {
        is ViewState.Loading -> {
            onLoading?.run { this }
        }
        is ViewState.Success -> {
            onSuccess(viewState.data)
        }
        is ViewState.Failed -> {
            onError?.run { this(viewState.message) }
        }
    }
}
