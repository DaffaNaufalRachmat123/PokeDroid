package com.android.libraries.transformationlayout

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.transition.Fade
import android.util.TypedValue
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.ajalt.timberkt.w
import com.google.android.material.transition.Hold
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback


/** gets an activity from a context. */
internal fun Context.getActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}


/** Sets the view's background as a ripple rectangle */
@RequiresApi(Build.VERSION_CODES.M)
public fun View.setRectangleRippleBackground(): Unit = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    foreground = getDrawable(context, resourceId)
}

/** Sets the view's background as a ripple circle */
@RequiresApi(Build.VERSION_CODES.M)
public fun View.setCircleRippleBackground(): Unit = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, this, true)
    foreground = getDrawable(context, resourceId)
}

/** makes visible or invisible a View align the value parameter. */
internal fun View.visible(value: Boolean) {
    if (value) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

/** sets an exit shared element callback to activity for implementing shared element transition. */
fun Activity.onTransformationStartContainer() {
    window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    window.sharedElementsUseOverlay = false
}

/** sets an enter shared element callback to activity for implementing shared element transition. */
fun Activity.onTransformationEndContainer(
    params: TransformationLayout.Params?
) {
    if (params != null) {

        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)


        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        ViewCompat.setTransitionName(
            findViewById<View>(android.R.id.content),
            params.transitionName
        )

        SharedElementFixHelper.removeActivityFromTransitionManager(this)


//        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
//        setExitSharedElementCallback(object : MaterialContainerTransformSharedElementCallback() {
//            override fun onSharedElementEnd(
//                sharedElementNames: MutableList<String>,
//                sharedElements: MutableList<View>,
//                sharedElementSnapshots: MutableList<View>
//            ) {
//                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
//                sharedElements.filterIsInstance<ImageView>()
//                    .forEach { it.visibility = View.VISIBLE }
//            }
//        })
        window.sharedElementEnterTransition = params.getMaterialContainerTransform()
        window.sharedElementReturnTransition = params.getMaterialContainerTransform()
        window.sharedElementsUseOverlay = false;
        val fade = Fade()
        fade.duration = 150
        window.returnTransition = fade
        window.enterTransition = fade
//        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    } else {
        w { "TransformationLayout.Params null. check your intent key value is correct." }
    }
}

/** sets an exit shared element callback to fragment for implementing shared element transition. */
fun Fragment.onTransformationStartContainer() {
    exitTransition = Hold()
}

/** sets an enter shared element callback to fragment for implementing shared element transition. */
fun Fragment.onTransformationEndContainer(
    params: TransformationLayout.Params?
) {
    if (params != null) {
        sharedElementEnterTransition = params.getMaterialFragmentTransform()
    }
}

/** adds a shared element transformation to FragmentTransaction. */
fun FragmentTransaction.addTransformation(
    transformationLayout: TransformationLayout,
    transitionName: String? = null
): FragmentTransaction {
    if (transitionName != null && transformationLayout.transitionName == null) {
        ViewCompat.setTransitionName(transformationLayout, transitionName)
    }
    addSharedElement(transformationLayout, transformationLayout.transitionName)
    return this
}