package com.android.libraries.transformationlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.android.libraries.transformationlayout.TransformationCompat.activityTransitionName

/** An abstract activity extending [ComponentActivity] with registering transformation automatically. */
abstract class TransformationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra(activityTransitionName))
        super.onCreate(savedInstanceState)
    }
}