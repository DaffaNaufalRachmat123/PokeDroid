@file:Suppress("NOTHING_TO_INLINE")

package com.pokedroid.common.extension

import android.animation.LayoutTransition
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Outline
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.method.TransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.core.view.*
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import com.pokedroid.common.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


/**
 * Transforms static java function SnackBar.make() to an extension function on View.
 */
fun Fragment.showSnackBar(snackBarText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
    activity?.let { Snackbar.make(it.findViewById<View>(android.R.id.content), snackBarText, timeLength).show() }
}

inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById(id)
inline fun <reified T : View> Activity.find(@IdRes id: Int): T = findViewById(id)
inline fun <reified T : View> Dialog.find(@IdRes id: Int): T = findViewById(id)

const val INVALID_RESOURCE_ID = -1

// region View
/**
 * Set `this` visibility to [View.GONE].
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Set `this` visibility to [View.GONE] if the [predicate] is true.
 *
 * @param predicate whether the visibility will change
 */
fun View.goneIf(predicate: Boolean) {
    if (predicate) gone()
    else show()
}

fun List<View>.visible()   = apply {
    forEach { it.visible() }
}
fun List<View>.invisible() = apply {
    forEach { it.invisible() }
}
fun List<View>.gone()      = apply {
    forEach { it.gone() }
}

inline fun View.animateVisible(
    isVisible: Boolean,
    startDelay: Long = 0,
    duration: Long = 30
) {
    animate()
        .setStartDelay(startDelay)
        .setDuration(duration)
        .alpha(if (isVisible) 1f else 0f)
        .withEndAction { this.isVisible = isVisible }
}

inline fun View.animateInvisible(
    isInvisible: Boolean,
    startDelay: Long = 0,
    duration: Long = 30
) {
    animate()
        .setStartDelay(startDelay)
        .setDuration(duration)
        .alpha(if (isInvisible) 0f else 1f)
        .withEndAction { this.isInvisible = isInvisible }
}

inline fun View.animateGone(
    isGone: Boolean,
    startDelay: Long = 0,
    duration: Long = 300
) {
    animate()
        .setStartDelay(startDelay)
        .setDuration(duration)
        .alpha(if (isGone) 0f else 1f)
        .withEndAction { this.isGone = isGone }
}

inline fun View.visibleIf(condition: () -> Boolean)   = apply {
    if (condition()) visible()
}
inline fun View.invisibleIf(condition: () -> Boolean) = apply {
    if (condition()) invisible()
}
inline fun View.goneIf(condition: () -> Boolean)      = apply {
    if (condition()) gone()
}

inline fun List<View>.visibleIf(condition: () -> Boolean)   = apply {
    if (condition()) visible()
}
inline fun List<View>.invisibleIf(condition: () -> Boolean) = apply {
    if (condition()) invisible()
}
inline fun List<View>.goneIf(condition: () -> Boolean)      = apply {
    if (condition()) gone()
}

fun View.visibleOrInvisible(visibleIf: () -> Boolean) = apply {
    if (visibleIf()) visible() else invisible()
}
fun View.visibleOrGone(visibleIf: () -> Boolean)      = apply {
    if (visibleIf()) visible() else gone()
}

fun List<View>.visibleOrInvisible(visibleIf: () -> Boolean) = apply {
    if (visibleIf()) visible() else invisible()
}
fun List<View>.visibleOrGone(visibleIf: () -> Boolean)      = apply {
    if (visibleIf()) visible() else gone()
}

//endregion
//region Availability

inline val List<View>.areAllEnabled: Boolean
    get() = all { it.isEnabled }

inline val List<View>.areAllDisabled: Boolean
    get() = all { !it.isEnabled }

fun List<View>.enabled(isEnabled: Boolean) {
    forEach { it.isEnabled = isEnabled }
}

/**
 * Set `this` visibility to [View.INVISIBLE]
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * @return true if `this` visibility is [View.INVISIBLE], false otherwise
 */
fun View.isInvisible(): Boolean = visibility == View.INVISIBLE

/**
 * Set `this` visibility to [View.VISIBLE].
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Set `this` visibility to [View.VISIBLE] if the [predicate] is true.
 *
 * @param predicate whether the visibility will change
 */
fun View.visibleIf(predicate: Boolean) {
    if (predicate) visibility = View.VISIBLE
}

/**
 * Set `this` visibility to [View.VISIBLE] if the [predicate] is true,
 * to [otherwiseVisibility] if false.
 *
 * @param predicate whether the visibility will change
 * @param otherwiseVisibility value to be set if [predicate] is false, should be either
 * [View.INVISIBLE] or [View.GONE]
 */
fun View.visibleIf(predicate: Boolean, otherwiseVisibility: Int) {
    visibility = if (predicate) View.VISIBLE else otherwiseVisibility
}

/**
 * @return true if `this` visibility is [View.VISIBLE], false otherwise
 */
fun View.isVisible(): Boolean = visibility == View.VISIBLE

/**
 * Set `this` height to 0.
 */
fun View.heightCollapse() {
    val params = layoutParams
    params.height = 0
    layoutParams = params
    requestLayout()
}

/**
 * @return true if `this` height is 0, false otherwise
 */
fun View.heightIsCollapsed(): Boolean = layoutParams.height == 0

/**
 * Set `this` height to [ViewGroup.LayoutParams.WRAP_CONTENT]. It handles [LinearLayout],
 * [RelativeLayout] and [FrameLayout] specifically and tries to default to [ViewGroup] otherwise.
 */
fun View.heightWrapContent() {
    val params = layoutParams
    params.height = when (layoutParams) {
        is LinearLayout -> LinearLayout.LayoutParams.WRAP_CONTENT
        is RelativeLayout -> RelativeLayout.LayoutParams.WRAP_CONTENT
        is FrameLayout -> FrameLayout.LayoutParams.WRAP_CONTENT
        else -> ViewGroup.LayoutParams.WRAP_CONTENT
    }

    layoutParams = params
    requestLayout()
}

fun View.setPaddingRes(@DimenRes paddingRes: Int) {
    setPadding(resources.getDimensionPixelSize(paddingRes))
}

// endregion

// region ViewGroup
/**
 * Creates a [LayoutInflater] from `this` context and calls [LayoutInflater.inflate], with `this`
 * as the inflated view root.
 *
 * @param layoutResource [LayoutRes] of the layout to be inflated
 * @param attachToRoot if the view should be attached to root or not, default is false
 */
@JvmOverloads
fun ViewGroup.inflate(@LayoutRes layoutResource: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutResource, this, attachToRoot)

/**
 * @return `this` children as an [Iterable] of [View]
 */
val ViewGroup.children: Iterable<View> get() = (0 until childCount).map(::getChildAt)

/**
 * Updates `this` margin values. It attempts to cast `this` layoutParams to
 * [ViewGroup.MarginLayoutParams] and will only update the values if the cast is succesful.
 *
 * @param left [DimenRes] of left margin
 * @param top [DimenRes] of top margin
 * @param right [DimenRes] of right margin
 * @param bottom [DimenRes] of bottom margin
 */
fun ViewGroup.setMargin(
        @DimenRes left: Int? = null,
        @DimenRes top: Int? = null,
        @DimenRes right: Int? = null,
        @DimenRes bottom: Int? = null
) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
        leftMargin = left?.let(resources::getDimensionPixelSize).orZero()
        topMargin = top?.let(resources::getDimensionPixelSize).orZero()
        rightMargin = right?.let(resources::getDimensionPixelSize).orZero()
        bottomMargin = bottom?.let(resources::getDimensionPixelSize).orZero()
    }
}

/**
 * Updates `this` layoutTransition to allow [LayoutTransition.CHANGING] transition types,
 * which is needed to animate height changes, for instance.
 */
fun ViewGroup.animateChangingTransitions() {
    layoutTransition = LayoutTransition().apply { enableTransitionType(LayoutTransition.CHANGING) }
}
// endregion

// region TextView
/**
 * Set `this` text to [textId] and `this` visibility to [View.VISIBLE].
 *
 * @param textId [StringRes] to be set
 */
fun TextView.visible(@StringRes textId: Int) {
    visible()
    setText(textId)
}

/**
 * Set `this` text to [text] and `this` visibility to [View.VISIBLE].
 *
 * @param text value to be set
 */
fun TextView.visible(text: String) {
    visible()
    this.text = text
}

/**
 * Set `this` text to [text] and `this` visibility to [View.VISIBLE] if [predicate] is true.
 *
 * @param predicate whether `this` visibility and text will be updated
 * @param text value to be set
 */
fun TextView.visibleIf(predicate: Boolean, text: String) {
    visibleIf(predicate)
    if (predicate) this.text = text
}

/**
 * Set `this` text to [text] if [predicate] is true and `this` visibility to [View.VISIBLE] if
 * [predicate] is true, to [otherwiseVisibility] if false.
 *
 * @param predicate whether `this` visibility and text will be updated
 * @param text value to be set
 * @param otherwiseVisibility value to be set if [predicate] is false, should be either
 * [View.INVISIBLE] or [View.GONE]
 */
fun TextView.visibleIf(predicate: Boolean, text: String, otherwiseVisibility: Int = View.GONE) {
    visibleIf(predicate, otherwiseVisibility = otherwiseVisibility)
    if (predicate) this.text = text
}

/**
 * Shorthand extension to make `this` clickable and focusable, while updating its text and click listener.
 *
 * @param textId the new text [StringRes]
 * @param clickListener function to be invoked on click
 */
fun TextView.setOnClickListener(@StringRes textId: Int, clickListener: () -> Unit) {
    setOnClickListener(context.getString(textId), clickListener)
}

/**
 * Shorthand extension to make `this` clickable and focusable, while updating its text and click listener.
 *
 * @param text the new text value
 * @param clickListener function to be invoked on click
 */
fun TextView.setOnClickListener(text: String, clickListener: () -> Unit) {
    isClickable = true
    isFocusable = true
    this.text = text
    setOnClickListener { clickListener() }
}

/**
 * Calls [TextView.setCompoundDrawablesWithIntrinsicBounds] with null as values for all parameters.
 */
fun TextView.clearDrawables() {
    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
}

/**
 * Shorthand function to set compound drawables in a [TextView].
 *
 * @param drawableLeftRes the [DrawableRes] id to be set as a left drawable
 * @param drawableTopRes the [DrawableRes] id to be set as a top drawable
 * @param drawableRightRes the [DrawableRes] id to be set as a right drawable
 * @param drawableBottomRes the [DrawableRes] id to be set as a bottom drawable
 */
fun TextView.setDrawables(
        @DrawableRes drawableLeftRes: Int? = null,
        @DrawableRes drawableTopRes: Int? = null,
        @DrawableRes drawableRightRes: Int? = null,
        @DrawableRes drawableBottomRes: Int? = null
) {
    setCompoundDrawablesWithIntrinsicBounds(
            drawableLeftRes?.let { AppCompatResources.getDrawable(context, it) },
            drawableTopRes?.let { AppCompatResources.getDrawable(context, it) },
            drawableRightRes?.let { AppCompatResources.getDrawable(context, it) },
            drawableBottomRes?.let { AppCompatResources.getDrawable(context, it) })
}

/**
 * Shorthand function to linkify all urls in `this`, setting the movement method to [LinkMovementMethod] and the
 * transformation method to [transformationMethod].
 *
 * @param [transformationMethod] an optional [TransformationMethod] to be set as `this` transformationMethod
 */
fun TextView.setupLinks(transformationMethod: TransformationMethod? = null) {
    Linkify.addLinks(this, Linkify.ALL)
    movementMethod = LinkMovementMethod.getInstance()
    transformationMethod?.let(::setTransformationMethod)
}
// endregion

// region EditText
/**
 * Sets `this` text to an empty string.
 */
fun EditText.clearText() {
    setText("")
}

inline val EditText.value get() = text.toString()

inline val TextInputEditText.value get() = text.toString()

fun TextInputLayout.setWidgetError(error: String?) {
    if (error == null) {
        this.isErrorEnabled = false
        this.error = null
    } else {
        this.isErrorEnabled = true
        this.error = error
    }
}

/**
 * Shorthand function to set text listeners to an [EditText].
 */
inline fun EditText.addTextChangedListener(
        crossinline beforeTextChanged: (charSequence: CharSequence, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> },
        crossinline onTextChanged: (charSequence: CharSequence, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> },
        crossinline afterTextChanged: (text: String) -> Unit = {}
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
            beforeTextChanged(charSequence, start, count, after)
        }

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            onTextChanged(charSequence, start, before, count)
        }

        override fun afterTextChanged(editable: Editable) {
            afterTextChanged(editable.toString())
        }
    })
}
// endregion

// region TextInputLayout
/**
 * Shorthand function to display an error message in a [TextInputLayout].
 *
 * @param errorMessage the message to be displayed
 */
fun TextInputLayout.showError(errorMessage: String) {
    error = errorMessage
    if (childCount == 1 && (getChildAt(0) is TextInputEditText || getChildAt(0) is EditText)) {
        getChildAt(0).requestFocus()
    }
}

/**
 * Shorthand function to reset the error state of a [TextInputLayout].
 */
fun TextInputLayout.clearError() {
    error = null
}
// endregion

// region EditText
/**
 * @return `this` text as string, or an empty string if text was null
 */
fun EditText.textAsString(): String = this.text?.toString().orEmpty()

// region ImageView
/**
 * Shorthand function to set a [DrawableRes] to an [ImageView] while also making it visible.
 *
 * @param imageRes [DrawableRes] resource to be set
 */
fun ImageView.visible(@DrawableRes imageRes: Int) {
    visible()
    setImageResource(imageRes)
}

/**
 * Shorthand function to set a [Uri] to an [ImageView] while also making it visible.
 *
 * @param imageUri [Uri] to be set
 */
fun ImageView.visible(imageUri: Uri) {
    visible()
    setImageURI(imageUri)
}
// endregion

// region Toolbar
fun AppCompatActivity.setupToolbar(
        toolbar: Toolbar,
        displayHomeAsUpEnabled: Boolean = true,
        displayShowHomeEnabled: Boolean = true,
        displayShowTitleEnabled: Boolean = true,
        showUpArrowAsCloseIcon: Boolean = false,
        @DrawableRes closeIconDrawableRes: Int? = null
) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled)
        setDisplayShowHomeEnabled(displayShowHomeEnabled)
        setDisplayShowTitleEnabled(displayShowTitleEnabled)

        if (showUpArrowAsCloseIcon && closeIconDrawableRes != null) {
            setHomeAsUpIndicator(AppCompatResources.getDrawable(this@setupToolbar, closeIconDrawableRes))
        }
    }
}

fun AppCompatActivity.onSupportNavigateUpGoBack(): Boolean {
    onBackPressed()
    return true
}
// endregion

// region Keyboard
/**
 * Shorthand function to check if given editor action is equivalent to "submitting a form", that is:
 *   - [actionId] is either [EditorInfo.IME_ACTION_GO] or [EditorInfo.IME_ACTION_DONE]
 *   - [event] is not null and [KeyEvent.getAction] is equal to [KeyEvent.ACTION_UP] and [KeyEvent.getKeyCode] is
 *     equal to [KeyEvent.KEYCODE_ENTER]
 *
 * @return true if all the conditions are met, false otherwise
 */
fun isKeyboardSubmit(actionId: Int, event: KeyEvent?): Boolean =
        actionId == EditorInfo.IME_ACTION_GO ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_ENTER)

fun View.showKeyboard() {
    requestFocus()
    context.getSystemService<InputMethodManager>()?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    context.getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(windowToken, 0)
}
// endregion

// region Resources
/**
 * Checks if a given [resourceId] is valid.
 *
 * @return true if [resourceId] is valid, false otherwise
 */
fun TypedArray.isResourceIdInvalid(resourceId: Int): Boolean =
        getResourceId(resourceId, INVALID_RESOURCE_ID) == INVALID_RESOURCE_ID

/**
 * Returns a Drawable obtained from the resource id.
 *
 * @param context context to inflate against
 * @param id [StyleableRes] id of the resource to be inflated
 *
 * @return the inflated [Drawable] if the [id] was valid and the inflation successful, null otherwise
 */
fun TypedArray.resolveResource(context: Context, @StyleableRes id: Int): Drawable? =
        try {
            getResourceId(id, INVALID_RESOURCE_ID).takeIf { it != INVALID_RESOURCE_ID }
                    ?.let { AppCompatResources.getDrawable(context, it) }
        } catch (exception: Exception) {
            null
        }
// endregion

fun <T : View> T.click(delay: Long = 1000): T {
    triggerDelay = delay
    return this
}

fun <T : View> T.click(time: Long = 1000, block: (T) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            @Suppress("UNCHECKED_CAST")
            block(it as T)
        }
    }
}

fun View.changeBackground(context : Context , @DrawableRes resource : Int){
    this.background = ContextCompat.getDrawable(context , resource)
}

fun ImageView.changeSource(resource : Int){
    this.setImageResource(resource)
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        triggerLastTime = currentClickTime
        flag = true
    }

    return flag
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.setVisible(visible: Boolean) {
    if (visible) {
        animate()
                .alpha(1f)
                .setDuration(200)
                .start()
        val handler = Handler()
        handler.postDelayed({ show() }, 200)
    } else {
        animate()
                .alpha(0f)
                .setDuration(200)
                .start()
        val handler = Handler()
        handler.postDelayed({ gone() }, 200)
    }
}

fun View.showFade(startAlpha: Float = 0f, endAlpha: Float = 1f, duration: Int = 300) {
    if (visibility == View.GONE || visibility == View.INVISIBLE) {
        isEnabled = true
        val animation = AlphaAnimation(startAlpha, endAlpha)
        animation.duration = duration.toLong()
        startAnimation(animation)
        visibility = View.VISIBLE
    }
}

fun <T : View> T.tint(@ColorRes colourRes: Int, alpha: Float = 1.0f): T {
    val tintColour = context.getResColor(colourRes).alpha(alpha)

    when (this) {
        is AppCompatCheckBox -> {
            val colorStateList = ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_enabled)),
                    intArrayOf(tintColour)
            )

            supportButtonTintList = colorStateList
        }
        is ProgressBar -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                indeterminateTintList = ColorStateList.valueOf(tintColour)
                progressTintList = ColorStateList.valueOf(tintColour)
                secondaryProgressTintList = ColorStateList.valueOf(tintColour)
                progressBackgroundTintList = ColorStateList.valueOf(tintColour.alpha(alpha * 0.5f))
            }
        }

        is ImageView -> drawable?.tint(tintColour)
        is TextView -> compoundDrawables.tint(tintColour)
        else -> {
            if (background == null) background = ColorDrawable(tintColour) else background.tint(tintColour)
        }
    }

    return this
}

fun ImageView.setImageTintList(@ColorRes @AttrRes tintRes: Int) {
    val tintList = ColorStateList.valueOf(context.resolveColor(tintRes))
    ImageViewCompat.setImageTintList(this, tintList)
}

fun Array<Drawable?>.tint(@ColorInt tintColour: Int) = forEach { drawable -> drawable?.tint(tintColour) }

fun Int.alpha(alpha: Float): Int {
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)

    return Color.argb((alpha * 255.0f).toInt(), red, green, blue)
}

fun View.hideFade(startAlpha: Float = 1f, endAlpha: Float = 0f, duration: Int = 300) {
    if (visibility == View.VISIBLE) {
        isEnabled = false
        val animation = AlphaAnimation(startAlpha, endAlpha)
        animation.duration = duration.toLong()
        startAnimation(animation)
        visibility = View.GONE
    }
}

fun Window.fullScreen() {
    setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}

fun View.useAttributes(attrs: AttributeSet?, @StyleableRes styleable: IntArray, defStyleAttr: Int = 0, defStyleRes: Int = 0, actions: TypedArray.() -> Unit) {
    attrs?.let {
        with(context.obtainStyledAttributes(it, styleable, defStyleAttr, defStyleRes)) {
            actions()
            recycle()
        }
    }
}

fun View.setMargins(start: Int = marginStart, top: Int = marginTop, end: Int = marginEnd, bottom: Int = marginBottom) =
        layoutParams?.run {
            updateLayoutParams<ViewGroup.MarginLayoutParams> {
                this.setMargins(start, top, end, bottom)
            }
        }

fun <T : ViewGroup.MarginLayoutParams> T.setMargins(
        start: Int = marginStart,
        top: Int = topMargin,
        end: Int = marginEnd,
        bottom: Int = bottomMargin
) {
    this.marginStart = start
    this.topMargin = top
    this.marginEnd = end
    this.bottomMargin = bottom
}


fun <T> View.findViewsWithTag(id: Int, value: T? = null): List<View> {
    val childrenWithTag = mutableListOf<View>()

    fun addIfHasTag(view: View) {
        val tag = view.getTag(id)
        val areValuesEqual = value != null && tag == value
        if (tag != null || areValuesEqual) childrenWithTag += view
    }

    if (this is ViewGroup) {
        children.forEach {
            if (it is ViewGroup) {
                childrenWithTag += it.findViewsWithTag(id, value)
            }
            addIfHasTag(it)
        }
    } else {
        addIfHasTag(this)
    }

    return childrenWithTag
}

fun View.doOnGlobalLayout(block: () -> Unit): OneShotGlobalLayoutListener =
    OneShotGlobalLayoutListener.add(this, block)

/** @see androidx.core.view.OneShotPreDrawListener */
class OneShotGlobalLayoutListener private constructor(
    private val view: View,
    private val block: () -> Unit
) : ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {
    private var viewTreeObserver = view.viewTreeObserver

    override fun onPreDraw(): Boolean {
        removeListener()
        block()
        return true
    }

    override fun onViewAttachedToWindow(view: View) {
        viewTreeObserver = view.viewTreeObserver
    }

    override fun onViewDetachedFromWindow(view: View) {
        removeListener()
    }

    fun removeListener() {
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.removeOnPreDrawListener(this)
        } else {
            view.viewTreeObserver.removeOnPreDrawListener(this)
        }
        view.removeOnAttachStateChangeListener(this)
    }

    companion object {
        fun add(view: View, block: () -> Unit): OneShotGlobalLayoutListener =
            OneShotGlobalLayoutListener(view, block).also {
                view.viewTreeObserver.addOnPreDrawListener(it)
                view.addOnAttachStateChangeListener(it)
            }
    }
}


inline val Resources.navBarHeightX: Int
    get() = with(getIdentifier("navigation_bar_height", "dimen", "android")) {
        if (this > 0) getDimensionPixelSize(this) else 0
    }

fun View.addNavigationBarPadding() {
    setPadding(
            paddingLeft,
            paddingTop,
            paddingRight,
            paddingBottom + resources.navBarHeightX
    )
}

fun View.setBackgroundColorRes(@ColorRes @AttrRes colorRes: Int) {
    setBackgroundColor(context.resolveThemeColor(colorRes))
}

//endregion
//region Shape

fun View.makeRoundedCornerOnTop(@DimenRes radiusRes: Int) {
    val radius = resources.getDimensionPixelSize(radiusRes)
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(
                    0, 0,
                    view.width, view.height + radius,
                    radius.toFloat()
            )
        }
    }
    clipToOutline = true
}

//endregion

fun ImageView.setImageTintListRes(@ColorRes @AttrRes tintRes: Int) {
    val tintList = ColorStateList.valueOf(context.resolveThemeColor(tintRes))
    ImageViewCompat.setImageTintList(this, tintList)
}

/**
 * Changes title color by given [color] for all menu items in this `receiver` isa.
 *
 * @see MenuItem.setTitleColor
 * @see setItemsIconTint
 */
fun Menu.setItemsTitleColor(@ColorInt color: Int) = forEach {
    it.setTitleColor(color)
}

/** Changes menu item's title color according to given [color] isa. */
fun MenuItem.setTitleColor(@ColorInt color: Int) {
    val spannableString = SpannableString(title)
    spannableString += ForegroundColorSpan(color)

    title = spannableString
}

operator fun Spannable.plusAssign(span: Any) {
    this[0..length] = span
}

fun View.slideDown(alpha: Float) {
    ViewCompat.animate(this)
            .alpha(alpha)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(300).start()
}

fun View.slideUp(alpha: Float) {
    ViewCompat.animate(this)
            .alpha(alpha)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(300).start()
}

fun View.slideDown() {
    ViewCompat.animate(this)
        .translationY(height.toFloat())
        .alpha(0f)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction {
            visibility = View.GONE
            alpha = 1f
            translationY = 0f
        }
        .start()
}

fun View.slideUp() {
    visibility = View.VISIBLE
    alpha = 0f

    if (height > 0) {
        slideUpNow(this)
    } else {
        // wait till height is measured
        post { slideUpNow(this) }
    }
}

fun View.slideDownReverse(){
    visibility = View.VISIBLE
    ViewCompat.animate(this)
            .translationY(height.toFloat())
            .alpha(0f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                visibility = View.GONE
                alpha = 1f
                translationY = 0f
            }
            .start()
}

fun View.slideUpReverse(){
    alpha = 0f

    if (height > 0) {
        slideUpNow(this)
    } else {
        // wait till height is measured
        post { slideUpNow(this) }
    }
}

private fun slideUpNow(view: View) {
    view.translationY = view.height.toFloat()
    ViewCompat.animate(view)
        .translationY(0f)
        .alpha(1f)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction {
            view.visibility = View.VISIBLE
            view.alpha = 1f
        }
        .start()
}

fun TextInputEditText.setReadOnly(value: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}

/**
 * Both sets the [EditText.setImeOptions] to "Send" and listens for the IME action.
 */
inline fun EditText.onSend(crossinline action: (text: String) -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_SEND
    onImeAction {
        hideKeyboard()
        action(text.toString())
    }
}


/**
 * Listens for either the enter key to be pressed or the soft keyboard's editor action to activate.
 */
inline fun EditText.onImeAction(crossinline action: (text: String) -> Unit) {
    setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
        if ((event?.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            action(text.toString())
            return@OnKeyListener true
        }
        false
    })
    setOnEditorActionListener { _, _, _ ->
        action(text.toString())
        true
    }
}

fun View.copyTextToClipboard(context : Context , text : String){
    val clipboard : android.content.ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = ClipData.newPlainText("label" , text)
    clipboard.setPrimaryClip(clip)
}