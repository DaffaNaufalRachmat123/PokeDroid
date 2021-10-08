package com.pokedroid.common.glide

import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.BaseRequestOptions


/**
 * Created on : 8/26/20.
 * Author     : Musthofa Ali Ubaed
 * Email      : panic.inc.dev@gmail.com
 */
@GlideExtension
object PkGlideExtension {

    @GlideOption
    @JvmStatic
    fun roundedCorners(options: BaseRequestOptions<*>, radius: Int): BaseRequestOptions<*> {
        return options.transform(RoundedCorners(radius))
    }

    @GlideOption
    @JvmStatic
    fun circle(options: BaseRequestOptions<*>): BaseRequestOptions<*> {
        return options.transform(CircleCrop())
    }
}
