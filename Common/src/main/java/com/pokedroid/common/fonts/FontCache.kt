package com.pokedroid.common.fonts

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import java.util.*

object FontCache {
    private val sCachedFonts: MutableMap<String, Typeface?> =
        HashMap()

    fun getTypeface(context: Context, assetPath: String): Typeface? {
        if (!sCachedFonts.containsKey(assetPath)) {
            val tf = Typeface.createFromAsset(context.assets, assetPath)
            sCachedFonts[assetPath] = tf
        }
        return sCachedFonts[assetPath]
    }

    fun changeTabsFont(context: Context, tabLayout: TabLayout) {
        val vg = tabLayout.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildsCount = vgTab.childCount
            for (i in 0 until tabChildsCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = Typeface.createFromAsset(
                        context.assets,
                        "sodosans_semibold.otf"
                    )
                }
            }
        }
    }
}