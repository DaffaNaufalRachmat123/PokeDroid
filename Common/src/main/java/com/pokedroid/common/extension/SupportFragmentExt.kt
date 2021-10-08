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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun <reified T : Fragment> newInstance(block: Bundle.(b: Bundle) -> Unit): T {
    val instance = T::class.java.newInstance()
    val bundle = Bundle().apply {
        block(this)
    }
    instance.arguments = bundle
    return instance
}

inline val Fragment.appCompatActivity get() = requireActivity() as AppCompatActivity

inline val Fragment.asActivity get() = requireActivity() as Activity

inline fun Fragment.getContextOrThrow(): Context = requireContext()

inline fun Fragment.getActivityOrThrow(): Activity {

    return requireActivity()
}

inline fun Fragment.getAppCompatActivityOrThrow(): AppCompatActivity {
    return appCompatActivity
}

inline fun <reified T : View> Fragment.find(@IdRes id: Int): T? = view?.findViewById(id)

inline fun Fragment.hideInputMethod() {
    getActivityOrThrow().hideInputMethod()
}

inline fun Fragment.hideInputMethod(v: View) {
    val imm: InputMethodManager = v.context
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(
        v.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )

    getActivityOrThrow().hideInputMethod()
}

inline fun Fragment.showInputMethod(v: EditText) {
    getActivityOrThrow().showInputMethod(v)
}

inline fun Fragment.inflate(
    @LayoutRes resourceId: Int,
    root: ViewGroup?,
    attachToRoot: Boolean = false
): View =
    LayoutInflater.from(requireContext()).inflate(resourceId, root, attachToRoot)


@Suppress("UNCHECKED_CAST")
fun <T : Any> Fragment.argument(key: String, defaultValue: T): Lazy<T> {
    return lazy {
        arguments?.get(key) as? T ?: defaultValue
    }
}


inline fun <T> Fragment.argument(key: String): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        @Suppress("UNCHECKED_CAST")
        arguments?.get(key) as T
    }
}

inline fun <T> Fragment.argumentSerializable(key: String): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        @Suppress("UNCHECKED_CAST")
        arguments?.getSerializable(key) as T
    }
}

inline fun <T> Fragment.argumentOrNull(key: String): Lazy<T?> {
    return lazy(LazyThreadSafetyMode.NONE) {
        @Suppress("UNCHECKED_CAST")
        arguments?.get(key) as? T?
    }
}

//Delegates
fun Fragment.argInt(key: String) = argument<Int>(
    key,
    -1
)//lazy { if (arguments?.containsKey(key)!!) arguments?.getString(key) else null }

fun Fragment.argString(key: String) = argument<String>(
    key,
    ""
)//lazy { if (arguments?.containsKey(key)!!) arguments?.getString(key) else null }

fun Fragment.argBoolean(key: String) = argument<Boolean>(
    key,
    false
)//lazy { if (arguments?.containsKey(key)!!) arguments?.getBoolean(key) else null }

fun Fragment.argDouble(key: String) = argument<Double>(
    key,
    0.0
)//lazy { if (arguments?.containsKey(key)!!) arguments?.getDouble(key) else null }

fun Fragment.argLong(key: String) = argument<Long>(
    key,
    0L
)//lazy { if (arguments?.containsKey(key)!!) arguments?.getLong(key) else null }

fun Fragment.argBundle(key: String) =
    argument<Bundle>(key, Bundle())//lazy { arguments?.getBundle(key) }

fun <T : Parcelable> Fragment.argParcelable(key: String) =
    argument<Parcelable>(key)//lazy { arguments?.getParcelable<T>(key) }

fun <T> Fragment.argSerializable(key: String) =
    argumentSerializable<T>(key)// lazy { arguments?.getSerializable(key) as? T }

fun Fragment.argsHasMap(key: String) = argument<HashMap<String, String>>(key)

fun <T : Fragment> T.withArguments(vararg params: Pair<String, Any>): T {
    arguments = bundleOf(*params)
    return this
}

inline fun FragmentManager.transact(function: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        function()
    }.commit()
}

inline fun FragmentManager.transactAllowingStateLoss(function: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        function()
    }.commitAllowingStateLoss()
}


fun Fragment.resColor(@ColorRes colorRes: Int) = this.context?.getResColor(colorRes)

fun Fragment.resDrawable(@DrawableRes drawableRes: Int) = this.context?.getResDrawable(drawableRes)

fun Fragment.resDimenPx(@DimenRes dimenRes: Int) = this.context?.getResDimen(dimenRes)

fun Fragment.resInt(@IntegerRes intRes: Int) = this.context?.getResInt(intRes)

fun Fragment.resBoolean(@BoolRes boolRes: Int) = this.context?.getResBool(boolRes)

fun Fragment.resIntArray(@ArrayRes intArrRes: Int) = this.context?.getResIntArray(intArrRes)

fun Fragment.resStrArray(@ArrayRes strArrRes: Int) = this.context?.getResStringArray(strArrRes)

inline fun <reified T : Any> Fragment.extra(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    if (value is T) value else default
}

inline fun <reified T : AppCompatActivity> Fragment.startActivity() {
    this.activity?.also {
        val intent = Intent(it, T::class.java)
        ContextCompat.startActivity(it, intent, null)
    }
}

inline fun <reified T : AppCompatActivity> Fragment.startActivity(body: Intent.() -> Unit) {
    this.activity?.also {
        val intent = Intent(it, T::class.java)
        intent.body()
        ContextCompat.startActivity(it, intent, null)
    }
}

inline fun <reified T : AppCompatActivity> Fragment.startActivity(
    @AnimRes enterResId: Int = 0,
    @AnimRes exitResId: Int = 0
) {
    this.activity?.also {
        val intent = Intent(it, T::class.java)
        val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(it, enterResId, exitResId)
        ContextCompat.startActivity(it, intent, optionsCompat.toBundle())
    }
}

inline fun <reified T : AppCompatActivity> Fragment.startActivity(
    @AnimRes enterResId: Int = 0, @AnimRes exitResId: Int = 0,
    body: Intent.() -> Unit
) {
    this.activity?.also {
        val intent = Intent(it, T::class.java)
        intent.body()
        val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(it, enterResId, exitResId)
        ContextCompat.startActivity(it, intent, optionsCompat.toBundle())
    }
}

inline fun <reified T : AppCompatActivity> Fragment.startActivity(sharedElements: androidx.core.util.Pair<View, String>) {
    this.activity?.also {
        val intent = Intent(it, T::class.java)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(it, sharedElements)
        ContextCompat.startActivity(it, intent, optionsCompat.toBundle())
    }
}

inline fun <reified T : AppCompatActivity> Fragment.startActivity(
    body: Intent.() -> Unit,
    sharedElements: androidx.core.util.Pair<View, String>
) {
    this.activity?.also {
        val intent = Intent(it, T::class.java)
        intent.body()
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(it, sharedElements)
        ContextCompat.startActivity(it, intent, optionsCompat.toBundle())
    }
}

inline fun Fragment.registerStartForActivityResult(
    crossinline onResultError: (resultCode: Int) -> Unit = {},
    crossinline onResultSuccess: Intent?.() -> Unit = {}
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { actResult: ActivityResult ->
        if (Activity.RESULT_OK == actResult.resultCode) onResultSuccess.invoke(actResult.data)
        else onResultError.invoke(actResult.resultCode)
    }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun Fragment.getFragment(frameId: Int = -1): Fragment? {
    var containerId = frameId
    if (containerId != -1) {
        containerId = id
    }
    return this.activity?.supportFragmentManager?.findFragmentById(containerId)
}