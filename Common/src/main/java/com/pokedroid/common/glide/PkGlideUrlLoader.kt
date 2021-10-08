package com.pokedroid.common.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.*
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import java.io.InputStream
import java.util.regex.Pattern

/**
 * Created on : 8/26/20.
 * Author     : Musthofa Ali Ubaed
 * Email      : panic.inc.dev@gmail.com
 */
class PkGlideUrlLoader(concreteLoader: ModelLoader<GlideUrl, InputStream>, modelCache: ModelCache<String, GlideUrl>)
    : BaseGlideUrlLoader<String>(concreteLoader, modelCache) {

    override fun getUrl(model: String, width: Int, height: Int, options: Options): String {
        val m = PATTERN.matcher(model)
        var bestBucket: Int
        if (m.find()) {
            val found = m.group(1).orEmpty().split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (bucketStr in found) {
                bestBucket = Integer.parseInt(bucketStr)
                if (bestBucket >= width) {
                    // the best bucket is the first immediately bigger than the requested width
                    break
                }
            }

        }
        return model
    }

    override fun handles(s: String): Boolean {
        return true
    }

    class Factory : ModelLoaderFactory<String, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
            return PkGlideUrlLoader(multiFactory.build(GlideUrl::class.java, InputStream::class.java), urlCache)
        }

        override fun teardown() {

        }
    }

    companion object {

        private val urlCache = ModelCache<String, GlideUrl>(150)
        private val PATTERN = Pattern.compile("__w-((?:-?\\d+)+)__")
    }
}