package com.pokedroid.common.extension

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.caverock.androidsvg.SVG
import com.github.ajalt.timberkt.e
import com.pokedroid.common.R
import com.pokedroid.common.view.ShimmerDrawable
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
fun Drawable.copy() = constantState?.newDrawable()?.mutate()

@SuppressLint("CheckResult")
fun getGlideOptions(
    target: ImageView,
    isCenterCrop: Boolean = false,
    radius: Int = 0,
    width: Int = 0,
    height: Int = 0,
    errorDrawable: Int? = null,
    placeholder: Drawable? = ShimmerDrawable()
): RequestOptions {

    val isLowRamDevice = target.context.isLowRamDevice()

    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .format(if (isLowRamDevice) DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888)
        .downsample(DownsampleStrategy.CENTER_INSIDE)
        .priority(Priority.HIGH)
        .dontAnimate()
        .let { request ->
            if (target.drawable != null) {
                request.placeholder(target.drawable.copy())
            } else {
                request
            }
        }

    var centerCropTransformation: CenterCrop? = null
    var roundedCornersTransformation: RoundedCornersTransformation? = null


    if (width > 0 || height > 0) {
        options.override(width, height)
    }else{
        options.override(target.width, target.height)
    }

    if (isCenterCrop) {
        centerCropTransformation = CenterCrop()
    }else{
        options.optionalFitCenter()
    }

    if (radius > 0) {
        roundedCornersTransformation =
            RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL)
    }

    if (errorDrawable != null) {
        options.error(errorDrawable)
    }

    if (placeholder != null) {
        options.placeholder(placeholder)
    }

    if (centerCropTransformation != null || roundedCornersTransformation != null) {
        val transformation =
            getMultiTransformation(centerCropTransformation, roundedCornersTransformation)
        options.transform(transformation)
    }

    return options
}
private fun getMultiTransformation(vararg transformations: Transformation<Bitmap>?): MultiTransformation<Bitmap> {
    val list = ArrayList<Transformation<Bitmap>?>()

    for (i in transformations.indices) {
        if (transformations[i] != null) {
            list.add(transformations[i])
        }
    }

    return MultiTransformation(list)
}
fun ImageView.usingRounded(
    url: String?, radius: Int = 8.dp, placeholder: Drawable? = ShimmerDrawable()
) {
    try {
        Glide.with(context)
            .load(url)
            .apply(
                getGlideOptions(
                    this,
                    errorDrawable = R.drawable.ic_no_image,
                    placeholder = placeholder
                )
            )
            .dontAnimate()
            .transform(
                CenterCrop(),
                RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL)
            )
            .into(this)
            .waitForLayout()
            .clearOnDetach()
    } catch (ex: Exception) {
        e { "Error loadImageRounded : ${ex.message}" }
    }

    applyOverlay()
}
fun ImageView.applyOverlay() {
    setColorFilter(ContextCompat.getColor(
        context,
        R.color.colorOverlayImage),
        PorterDuff.Mode.SRC_OVER
    )
}
fun ImageView.usingRoundedWidthAndHeight(
    width : Int , height : Int ,
    url: String?, radius: Int = 8.dp, placeholder: Drawable? = ShimmerDrawable()
) {
    try {
        Glide.with(context)
            .load(url)
            .apply(
                getGlideOptions(
                    this,
                    errorDrawable = R.drawable.ic_no_image,
                    placeholder = placeholder
                )
            )
            .dontAnimate()
            .override(width , height)
            .transform(
                CenterCrop(),
                RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL)
            )
            .into(this)
            .waitForLayout()
            .clearOnDetach()
    } catch (ex: Exception) {
        e { "Error loadImageRounded : ${ex.message}" }
    }

    applyOverlay()
}
fun AppCompatImageView.applyOverlay() {
    setColorFilter(ContextCompat.getColor(
        context,
        R.color.colorOverlayImage),
        PorterDuff.Mode.SRC_OVER
    )
}
fun ImageView.loadImageCenterCrop(url: String?) {
    usingRounded(url.orDefaultImage(), 0)
}

fun ImageView.loadImage(url: String?, radius: Int = 8) {
    try {
        Glide.with(context)
            .load(url)
            .placeholder(ShimmerDrawable())
            .error(R.drawable.ic_no_image)
            .into(this)
    } catch (ex: IllegalArgumentException) {
        e { "Error loadImageRounded : ${ex.message}" }
    }
    applyOverlay()
}