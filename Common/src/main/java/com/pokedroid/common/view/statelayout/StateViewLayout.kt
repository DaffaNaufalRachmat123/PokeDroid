package com.pokedroid.common.view.statelayout

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingParent
import androidx.core.view.ScrollingView
import androidx.core.view.ViewCompat
import com.pokedroid.common.R

class StateViewLayout : View {

    @IntDef(EMPTY, RETRY, LOADING)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ViewType

    var emptyResource = 0
    var retryResource = 0
    var loadingResource = 0
    var blankResource = 0

    var emptyView: View? = null
    var retryView: View? = null
    var loadingView: View? = null
    var blankView : View? = null

    var inflater: LayoutInflater? = null
    private var mRetryAction: ((errView: View?) -> Unit)? = null
    var onInflateListener: OnInflateListener? = null

    var animatorProvider: AnimatorProvider? = null
        set(value) {
            field = value
            reset(emptyView)
            reset(blankView)
            reset(loadingView)
            reset(retryView)
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StateViewLayout)
        emptyResource = a.getResourceId(R.styleable.StateViewLayout_emptyResource, 0)
        blankResource = a.getResourceId(R.styleable.StateViewLayout_blankResource , 0)
        retryResource = a.getResourceId(R.styleable.StateViewLayout_retryResource, 0)
        loadingResource = a.getResourceId(R.styleable.StateViewLayout_loadingResource, 0)
        a.recycle()

        if (emptyResource == 0) {
            emptyResource = R.layout.sl_empty
        }
        if (retryResource == 0) {
            retryResource = R.layout.sl_error
        }
        if (loadingResource == 0) {
            loadingResource = R.layout.sl_loading
        }

        if(blankResource == 0){
            blankResource = R.layout.sl_blank
        }

        visibility = GONE
        setWillNotDraw(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(0, 0)
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas?) {
    }

    override fun dispatchDraw(canvas: Canvas?) {}

    override fun setVisibility(visibility: Int) {
        setVisibility(emptyView, visibility)
        setVisibility(retryView, visibility)
        setVisibility(loadingView, visibility)
        setVisibility(blankView , visibility)
    }

    private fun setVisibility(view: View?, visibility: Int) {
        if (view != null && visibility != view.visibility) {
            if (animatorProvider != null) {
                startAnimation(view)
            } else {
                view.visibility = visibility
            }
        }
    }

    fun showContent(): StateViewLayout{
        visibility = GONE
        return this
    }

    fun setEmptyLayout(layoutId: Int): StateViewLayout {
        emptyResource = layoutId
        return this
    }

    fun showEmpty(noDataText: String = "Belum ada data saat ini", noDataIconRes: Int = 0): View {
        if (emptyView == null) {
            emptyView = inflate(emptyResource, EMPTY)
        }
        showView(emptyView!!)
        val textView = emptyView?.findViewById<TextView?>(R.id.tvNoDataText)
        textView?.text = noDataText
        val imageView = emptyView?.findViewById<ImageView?>(R.id.ivNoDataIcon)
        imageView?.setImageResource(noDataIconRes)
        return emptyView!!
    }

    fun showBlank() : View {
        if (blankView == null)
            blankView = inflate(blankResource , BLANK)
        showView(blankView!!)
        return blankView!!
    }

    fun showError(): View {
        if (retryView == null) {
            retryView = inflate(retryResource, RETRY)
            retryView?.findViewById<View>(R.id.btn_retry)?.setOnClickListener { retry() }
        }
        showView(retryView!!)
        return retryView!!
    }

    private fun retry() {
        showLoading()
        handler.postDelayed({
            mRetryAction?.invoke(retryView)
        }, 400)
    }

    fun onRetry(retryAction: ((errView: View?) -> Unit)? = null): StateViewLayout {
        mRetryAction = retryAction
        return this
    }

    fun showLoading(showText: Boolean = false, loadingText: String = "Loading..."): StateViewLayout {
        if (loadingView == null) {
            loadingView = inflate(loadingResource, LOADING)
        }
        showView(loadingView!!)
        val textView = loadingView?.findViewById<TextView>(R.id.tvLoading)
        textView?.text = loadingText
        textView?.visibility = if (showText) VISIBLE else GONE
        return this
    }

    /**
     * show the state view
     */
    private fun showView(view: View) {
        setVisibility(view, VISIBLE)
        hideViews(view)
    }

    /**
     * hide other views after show view
     */
    private fun hideViews(showView: View) {
        when {
            emptyView === showView -> {
                setVisibility(loadingView, GONE)
                setVisibility(retryView, GONE)
            }
            loadingView === showView -> {
                setVisibility(emptyView, GONE)
                setVisibility(retryView, GONE)
            }
            else -> {
                setVisibility(emptyView, GONE)
                setVisibility(loadingView, GONE)
            }
        }
    }

    private fun startAnimation(view: View) {
        val toShow = view.visibility == GONE
        val animator: Animator? = if (toShow) animatorProvider!!.showAnimation(view) else animatorProvider!!.hideAnimation(view)
        if (animator == null) {
            view.visibility = if (toShow) VISIBLE else GONE
            return
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (!toShow) {
                    view.visibility = GONE
                }
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                if (toShow) {
                    view.visibility = VISIBLE
                }
            }
        })
        animator.start()
    }

    /**
     * reset view's property
     */
    private fun reset(view: View?) {
        if (view != null) {
            view.translationX = 0f
            view.translationY = 0f
            view.alpha = 1f
            view.rotation = 0f
            view.scaleX = 1f
            view.scaleY = 1f
        }
    }

    private fun inflate(@LayoutRes layoutResource: Int, @ViewType viewType: Int): View {
        val viewParent = parent
        return if (viewParent != null && viewParent is ViewGroup) {
            if (layoutResource != 0) {
                val factory: LayoutInflater = inflater ?: LayoutInflater.from(context)
                val view = factory.inflate(layoutResource, viewParent, false)
                val index = viewParent.indexOfChild(this)
                view.isClickable = true
                view.visibility = GONE
                ViewCompat.setZ(view, ViewCompat.getZ(this))
                if (layoutParams != null) {
                    when (viewParent) {
                        is RelativeLayout -> {
                            val lp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                RelativeLayout.LayoutParams(layoutParams as RelativeLayout.LayoutParams)
                            } else {
                                RelativeLayout.LayoutParams(layoutParams)
                            }
                            viewParent.addView(view, index, lp)
                        }
                        is ConstraintLayout -> {
                            val source = layoutParams as ConstraintLayout.LayoutParams
                            val lp = ConstraintLayout.LayoutParams(layoutParams as ViewGroup.LayoutParams)
                            lp.leftToLeft = source.leftToLeft
                            lp.rightToRight = source.rightToRight
                            lp.topToTop = source.topToTop
                            lp.bottomToBottom = source.bottomToBottom
                            viewParent.addView(view, index, lp)
                        }
                        else -> {
                            viewParent.addView(view, index, layoutParams)
                        }
                    }
                } else {
                    viewParent.addView(view, index)
                }
                if (loadingView != null && retryView != null && emptyView != null) {
                    viewParent.removeViewInLayout(this)
                }
                onInflateListener?.onInflate(viewType, view)
                view
            } else {
                throw IllegalArgumentException("StateView must have a valid layoutResource")
            }
        } else {
            throw IllegalStateException("StateView must have a non-null ViewGroup viewParent")
        }
    }

    /**
     * Listener used to receive a notification after a StateView has successfully
     * inflated its layout resource.
     *
     * @see onInflateListener
     */
    interface OnInflateListener {
        /**
         * @param view The inflated View.
         */
        fun onInflate(@ViewType viewType: Int, view: View?)
    }

    companion object {
        const val EMPTY = 0x00000000
        const val RETRY = 0x00000001
        const val LOADING = 0x00000002
        const val BLANK = 0x00000003

        internal val TAG = StateViewLayout::class.java.simpleName

        @JvmStatic
        fun inject(activity: Activity): StateViewLayout {
            return inject(activity.window.decorView.findViewById<View>(android.R.id.content))
        }

        @JvmStatic
        fun inject(view: View): StateViewLayout {
            return when (view) {
                is ViewGroup -> inject(view)
                else -> wrap(view)
            }
        }

        @JvmStatic
        fun inject(viewGroup: ViewGroup): StateViewLayout {
            if (viewGroup is LinearLayout || viewGroup is ScrollView || viewGroup is AdapterView<*> ||
                    (viewGroup is ScrollingView && viewGroup is NestedScrollingChild) ||
                    (viewGroup is NestedScrollingParent && viewGroup is NestedScrollingChild)) {
                return Injector.wrapChild(viewGroup)
            }

            // match the viewGroup
            val stateView = StateViewLayout(viewGroup.context)
            viewGroup.addView(stateView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return stateView
        }

        @JvmStatic
        fun wrap(view: View): StateViewLayout {
            val parent = view.parent
            if (parent is ViewGroup) {
                if (parent is ConstraintLayout) {
                    return Injector.matchViewIfParentIsConstraintLayout(parent, view)
                }
                if (parent is RelativeLayout) {
                    return Injector.matchViewIfParentIsRelativeLayout(parent, view)
                }

                // will increase the layout level
                parent.removeView(view)
                val wrap = FrameLayout(view.context)
                parent.addView(wrap, view.layoutParams)
                wrap.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                val stateView = StateViewLayout(view.context)
                wrap.addView(stateView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                Injector.setStateListAnimator(stateView, view)
                return stateView
            }
            throw ClassCastException("view.getParent() must be ViewGroup")
        }

    }
}