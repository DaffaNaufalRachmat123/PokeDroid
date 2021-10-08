package com.pokedroid.common.fonts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.pokedroid.common.R

open class CustomEditText : TextInputEditText {
    constructor(context : Context) : super(context , null , R.style.MontserratTextView){

    }
    constructor(context : Context , attrs : AttributeSet) : super ( context , attrs ){
        applyCustomFont(context , attrs)
    }
    constructor(context : Context , attrs : AttributeSet , defStyle : Int) : super ( context , attrs , defStyle) {
        applyCustomFont(context , attrs)
    }
    private fun applyCustomFont(
        context: Context,
        attrs: AttributeSet
    ) {
        val textStyle = attrs.getAttributeIntValue(
            ANDROID_SCHEMA,
            "textStyle",
            Typeface.NORMAL
        )
        val customFont = selectTypeface(context, textStyle)
        typeface = customFont
    }

    private fun selectTypeface(context: Context, textStyle: Int): Typeface? {
        /*
         * information about the TextView textStyle:
         * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
         */
        return when (textStyle) {
            Typeface.BOLD -> FontCache.getTypeface(context, "montserrat_black.ttf")
            Typeface.ITALIC -> FontCache.getTypeface(context, "montserrat_italic.ttf")
            Typeface.NORMAL -> FontCache.getTypeface(context, "montserrat_reguler.ttf")
            else -> FontCache.getTypeface(context, "montserrat_medium.ttf")
        }
    }

    companion object {
        private const val ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android"
    }
}