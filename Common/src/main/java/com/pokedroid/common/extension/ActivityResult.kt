@file:Suppress("unused", "NOTHING_TO_INLINE", "FunctionName")

package com.pokedroid.common.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

/**
 * @author Dylan Cai
 */

val ActivityResult.isResultOK: Boolean get() = Activity.RESULT_OK == this.resultCode

fun ComponentActivity.ActivityResultLauncher() =
    ActivityResultLauncher({ this }, this, this::registerForActivityResult)

fun Fragment.ActivityResultLauncher() =
    ActivityResultLauncher({ requireContext() }, this, this::registerForActivityResult)

fun ComponentActivity.PermissionResultLauncher() =
    PermissionResultLauncher(this, this::registerForActivityResult)

fun Fragment.PermissionResultLauncher() =
    PermissionResultLauncher(this, this::registerForActivityResult)

fun ComponentActivity.MultiplePermissionsResultLauncher() =
    MultiplePermissionsResultLauncher(this, this::registerForActivityResult)

fun Fragment.MultiplePermissionsResultLauncher() =
    MultiplePermissionsResultLauncher(this, this::registerForActivityResult)

typealias RegisterForActivityResult<I, O> =
            (ActivityResultContract<I, O>, ActivityResultCallback<O>) -> androidx.activity.result.ActivityResultLauncher<I>

open class BaseLauncher<I, O>(
    private val owner: LifecycleOwner,
    registerForActivityResult: RegisterForActivityResult<I, O>,
    activityResultContract: ActivityResultContract<I, O>
) {
    private val result = MutableLiveData<O>()
    private var observer: ((O) -> Unit)? = null

    private val launcher: androidx.activity.result.ActivityResultLauncher<I> =
        registerForActivityResult(activityResultContract) { result ->
            this.result.value = result
            observer?.let { it -> this.result.removeObserver(it) }
        }

    fun launch(input: I, observer: (O) -> Unit) {
        launcher.launch(input)
        result.observe(owner, observer)
        this.observer = observer
    }
}

class ActivityResultLauncher(
    private val context: () -> Context,
    owner: LifecycleOwner,
    registerForActivityResult: RegisterForActivityResult<Intent, ActivityResult>
) :
    BaseLauncher<Intent, ActivityResult>(
        owner,
        registerForActivityResult,
        ActivityResultContracts.StartActivityForResult()
    ) {

    fun <T : Activity> launch(
        clazz: Class<T>,
        block: (ActivityResult) -> Unit,
        vararg params: Pair<String, Any?> = emptyArray(),
    ) {
        val intent = Internals.createIntent(context(), clazz, params = params)
        launch(intent, block)
    }

    inline fun <reified T : Activity> launch(
        vararg p: Pair<String, Any?> = emptyArray(),
        noinline block: (ActivityResult) -> Unit,
    ) {
        launch(clazz = T::class.java, block = block, params = p)
    }
}

class PermissionResultLauncher(
    owner: LifecycleOwner,
    registerForActivityResult: RegisterForActivityResult<String, Boolean>
) :
    BaseLauncher<String, Boolean>(
        owner,
        registerForActivityResult,
        ActivityResultContracts.RequestPermission()
    )

class MultiplePermissionsResultLauncher(
    owner: LifecycleOwner,
    registerForActivityResult: RegisterForActivityResult<Array<String>, Map<String, Boolean>>
) :
    BaseLauncher<Array<String>, Map<String, Boolean>>(
        owner,
        registerForActivityResult,
        ActivityResultContracts.RequestMultiplePermissions()
    )
