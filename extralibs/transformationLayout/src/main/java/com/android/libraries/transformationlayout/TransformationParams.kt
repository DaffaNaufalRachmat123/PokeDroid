package com.android.libraries.transformationlayout

import android.graphics.Color
import com.google.android.material.transition.platform.MaterialContainerTransform
import java.io.Serializable

/** Interface definition about the [TransformationLayout]'s parameters.  */
internal interface TransformationParams : Serializable {
    var duration: Long
    var pathMotion: TransformationLayout.Motion
    var zOrder: Int
    var containerColor: Int
    var scrimColor: Int
    var direction: TransformationLayout.Direction
    var fadeMode: TransformationLayout.FadeMode
    var fitMode: TransformationLayout.FitMode
}

internal object DefaultParamValues : TransformationParams {
    override var pathMotion: TransformationLayout.Motion = TransformationLayout.Motion.ARC
    override var duration = 450L
    override var zOrder: Int = android.R.id.content
    override var containerColor: Int = Color.TRANSPARENT
    override var scrimColor: Int = Color.TRANSPARENT
    override var direction: TransformationLayout.Direction = TransformationLayout.Direction.AUTO
    override var fadeMode: TransformationLayout.FadeMode = TransformationLayout.FadeMode.IN
    override var fitMode: TransformationLayout.FitMode = TransformationLayout.FitMode.AUTO
}

internal fun TransformationParams.getMaterialContainerTransform(): MaterialContainerTransform {
    val params = this
    return MaterialContainerTransform().apply {
        addTarget(android.R.id.content)
        excludeTarget(android.R.id.statusBarBackground, true)
        excludeTarget(android.R.id.navigationBarBackground, true)
        duration = params.duration
        pathMotion = params.pathMotion.getPathMotion()
        drawingViewId = params.zOrder
        containerColor = params.containerColor
        scrimColor = params.scrimColor
        transitionDirection = params.direction.value
        fadeMode = params.fadeMode.value
        fitMode = params.fitMode.value
    }
}

internal fun TransformationParams.getMaterialFragmentTransform(): MaterialContainerTransform {
    val params = this
    return MaterialContainerTransform().apply {
        duration = params.duration
        pathMotion = params.pathMotion.getPathMotion()
        drawingViewId = params.zOrder
        containerColor = params.containerColor
        scrimColor = params.scrimColor
        transitionDirection = params.direction.value
        fadeMode = params.fadeMode.value
        fitMode = params.fitMode.value
    }
}