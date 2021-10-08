package com.pokedroid.common.fonts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.pokedroid.common.R

open class CustomTextView : AppCompatTextView {
    private val fontList : HashMap<String , String> = hashMapOf(
        "reguler" to "montserrat_reguler.ttf",
        "italic" to "montserrat_italic.ttf",
        "semibold" to "montserrat_semibold.ttf",
        "bold" to "montserrat_bold.ttf"
    )
    constructor(context: Context) : super(
        context,
        null,
        R.style.MontserratTextView
    )

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        applyCustomFont(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        applyCustomFont(context, attrs)
    }

    private fun applyCustomFont(
        context: Context,
        attrs: AttributeSet?
    ) {
        val customAttrs = context.obtainStyledAttributes(attrs , R.styleable.CustomTextView)
        val styles = customAttrs.getString(R.styleable.CustomTextView_thinText)
        customAttrs.recycle()
        styles?.let {
            val customFont = selectTypeface(context, styles!!)
            typeface = customFont
        }
    }

    private fun selectTypeface(context: Context, textStyle: String): Typeface? {
        for(style in fontList.entries){
            if(style.key == textStyle)
                return FontCache.getTypeface(context , style.value)
        }
        return FontCache.getTypeface(context , "montserrat_reguler.ttf")
    }
}