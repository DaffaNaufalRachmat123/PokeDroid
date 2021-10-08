package com.android.libraries.transformationlayout

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat

/** Helper for accessing features in starting activities with transformation animation. */
object TransformationCompat {

    /** A common definition of the activity's transition name. */
    internal const val activityTransitionName: String = "com.androidpk.transformationlayout"

    /** Invalidate the activity's options menu, if able. */
    fun startActivity(
        transformationLayout: TransformationLayout,
        intent: Intent
    ) {
        transformationLayout.startActivityWithBundleOptions(intent) { workedIntent, bundle ->
            ActivityCompat.startActivity(transformationLayout.context, workedIntent, bundle)
        }
    }

    /**
     * Start new activity with options, if able, for which you would like a
     * result when it finished.
     */
    fun startActivityForResult(
        transformationLayout: TransformationLayout,
        intent: Intent,
        requestCode: Int
    ) {
        val activity = transformationLayout.context.getActivity()
        if (activity != null) {
            transformationLayout.startActivityWithBundleOptions(intent) { workedIntent, bundle ->
                ActivityCompat.startActivityForResult(activity, workedIntent, requestCode, bundle)
            }
        }
    }

    private inline fun TransformationLayout.startActivityWithBundleOptions(
        intent: Intent,
        block: (Intent, Bundle) -> Unit
    ) {
        val bundle = withView(this, activityTransitionName)
        intent.putExtra(activityTransitionName, getParcelableParams())
        block(intent, bundle)
    }
}