package com.pokedroid.common.view.statelayout

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.pokedroid.common.R
import com.pokedroid.common.extension.hide
import com.github.ajalt.timberkt.i
import com.google.android.material.button.MaterialButton


/**
 * Created on : 4/30/20.
 * Author     : Musthofa Ali Ubaed
 * Email      : panic.inc.dev@gmail.com
 */
class StateView @JvmOverloads constructor(context: Context,
                                          attrs: AttributeSet? = null,
                                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val errorView = -1
        const val loadingView = 0
        const val defaultView = 1
    }

    var activeView = defaultView
        set(value) {
            if (this.activeView == value) {
                return
            }
            if (!viewsMap.containsKey(value)) {
                throw IllegalStateException("Invalid view id: $value")
            }
            for (key in viewsMap.keys) {
                viewsMap[key]!!.visibility = if (key == value) View.VISIBLE else View.GONE
            }

            field = value
        }

    private var viewsMap = HashMap<Int, View>()

    private var loadingResId = R.layout.sv_loading_view
    private var errorResId = R.layout.sv_empty_view

    init {
        val attrsStateLayout = context.obtainStyledAttributes(attrs, R.styleable.StateView)
        loadingResId = attrsStateLayout.getResourceId(R.styleable.StateView_sv_loading_view, R.layout.sv_loading_view)
        errorResId = attrsStateLayout.getResourceId(R.styleable.StateView_sv_error_view, R.layout.sv_empty_view)
        attrsStateLayout.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        i { "onFinishInflate()" }

        if (childCount > 1) {
            throw IllegalArgumentException("You must have only one content view.")
        }
        if (childCount == 1) {
            val contentView = getChildAt(0)
            viewsMap[defaultView] = contentView
        }

        if (loadingResId != -1) {
            setViewForState(loadingView, loadingResId)
        }

        if (errorResId != -1) {
            setViewForState(errorView, errorResId)
        }
    }

    private fun setViewForState(viewTypeId: Int, @LayoutRes resId: Int) {
        val view = LayoutInflater.from(context).inflate(resId, this, false)
        setViewForState(viewTypeId, view)
    }

    private fun setViewForState(state: Int, view: View) {
        if (viewsMap.containsKey(state)) {
            removeView(viewsMap[state])
        }

        addView(view)
        view.visibility = View.GONE


        i { "setViewForState = $state" }

        viewsMap[state] = view
    }

    fun getView(state: Int): View? {
        return viewsMap[state]
    }

    fun showLoading(): StateView {
        activeView = loadingView
        return this
    }

    fun showError(): StateView {
        activeView = errorView
        return this
    }

    fun showContent(): StateView {
        activeView = defaultView
        return this
    }

    fun showEmpty(noDataText: String = "Belum ada data saat ini", noDataIconRes: Int = 0): StateView {
        activeView = errorView
        val textView = getView(errorView)?.findViewById<TextView?>(R.id.emptyText)
        val imageView = getView(errorView)?.findViewById<ImageView?>(R.id.emptyIcon)
        getView(errorView)?.findViewById<MaterialButton?>(R.id.btnRetry)?.hide()

        textView?.text = noDataText
        imageView?.setImageResource(noDataIconRes)
        return this
    }

    fun onRetry(action: (View) -> Unit) {
        getView(errorView)
                ?.findViewById<MaterialButton>(R.id.btnRetry)
                ?.setOnClickListener {
                    action.invoke(it)
                    activeView = loadingView
                }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return if (superState == null) {
            superState
        } else {
            SavedState(superState, activeView)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            activeView = state.state
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    internal class SavedState : BaseSavedState {

        var state: Int

        constructor(superState: Parcelable, state: Int) : super(superState) {
            this.state = state
        }

        constructor(source: Parcel) : super(source) {
            state = source.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(state)
        }
    }
}