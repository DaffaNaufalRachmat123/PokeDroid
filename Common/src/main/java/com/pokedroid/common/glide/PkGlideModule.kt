package com.pokedroid.common.glide

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.annotation.Keep
import com.pokedroid.common.base.BaseApp
import com.pokedroid.common.extension.isLowRamDevice
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.OkHttpClient
import java.io.File
import java.io.InputStream
import java.util.concurrent.TimeUnit


/**
 * Created on : 8/26/20.
 * Author     : Musthofa Ali Ubaed
 * Email      : panic.inc.dev@gmail.com
 */
@Keep
@GlideModule
class PkGlideModule : AppGlideModule() {

    //disk cache external storage size
    private val diskCacheSize = 250 * 1024 * 1024L

    //cache size
    private val memoryCacheSize = 250 * 1024 * 1024L

    //bitmap pool size
    private val bitmapPoolSize = 250 * 1024 * 1024L

    private var path: String = "/ImageCache/GlideImage"
    private var mContextWrapper = ContextWrapper(BaseApp.instance)
    private var diskCacheFolder: File = mContextWrapper.getExternalFilesDir(path)!!

    override fun isManifestParsingEnabled(): Boolean = false

    override fun applyOptions(context: Context, builder: GlideBuilder) {

        //Disk cache configuration (default cache size is 250 M, saved in internal storage by default)
        //Set the disk cache to be stored in external storage, and specify the cache size
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, diskCacheSize))

        //Set the disk cache to be saved in the directory you specify, and specify the cache size
        builder.setDiskCache(
            DiskLruCacheFactory(
                DiskLruCacheFactory.CacheDirectoryGetter { diskCacheFolder },
                diskCacheSize
            )
        )

        //Memory cache configuration (configuration is not recommended,
        // Glide will automatically allocate according to the phone configuration)
        //Set memory cache size
        builder.setMemoryCache(LruResourceCache(memoryCacheSize))

        //set bitmap pool size
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSize))


        /**
         * DiskCacheStrategy.NONE： Indicates that nothing will be cached.
         * DiskCacheStrategy.DATA： Indicates that only the original picture is cached.
         * DiskCacheStrategy.RESOURCE： Indicates that only the converted pictures are cached.
         * DiskCacheStrategy.ALL ： Indicates that both the original picture and the converted picture are cached.
         * DiskCacheStrategy.AUTOMATIC： Indicates to let Glide intelligently choose which caching strategy to use based on image resources (default option).
         */

        // Setting default params for glide
        val options = RequestOptions()
            .format(getBitmapQuality(context.isLowRamDevice()))
            .disallowHardwareConfig()
            .timeout(10000)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        builder.setDefaultRequestOptions(options)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        val factory = OkHttpUrlLoader.Factory(client)
        glide.registry.replace(
            GlideUrl::class.java,
            InputStream::class.java, factory
        )
//        registry.append(String::class.java, InputStream::class.java, PkGlideUrlLoader.Factory())
    }

    private fun getBitmapQuality(hasLowRam: Boolean): DecodeFormat {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || hasLowRam) {
            // return worse bitmap quality for low end devices
            DecodeFormat.PREFER_RGB_565
        } else {
            DecodeFormat.PREFER_ARGB_8888
        }
    }
}
