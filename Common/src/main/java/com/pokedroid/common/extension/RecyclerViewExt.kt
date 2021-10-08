package com.pokedroid.common.extension

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*
import com.pokedroid.common.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnItemLongClickListener

fun RecyclerView.addLayoutManager(context: Context, spanCount: Int = 1) {
    layoutManager = if (spanCount > 1) {
        GridLayoutManager(context, spanCount)
    } else {
        LinearLayoutManager(context)
    }
}

fun RecyclerView.addDivider(
        context: Context,
        dividerOrientation: Int = RecyclerView.VERTICAL
) {
    addItemDecoration(DividerItemDecoration(context, dividerOrientation))
}

/**
 * Set a [GridLayoutManager] as `this` layoutManager.
 *
 * @param spanCount number of grid columns
 *
 * @return `this`
 */
fun RecyclerView.withGridLayoutManager(spanCount: Int): RecyclerView = apply {
    layoutManager = GridLayoutManager(context, spanCount)
}

/**
 * Set a [LinearLayoutManager] as `this` layoutManager.
 *
 * @param vertical whether `this` layout should be vertical, default is true
 * @param reversed whether `this` layout should be reverted, default is false
 *
 * @return `this`
 */
fun RecyclerView.withLinearLayoutManager(
        vertical: Boolean = true,
        reversed: Boolean = false
): RecyclerView = apply {
    layoutManager =
            LinearLayoutManager(context, if (vertical) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL, reversed)
}

/**
 * Creates a [PagerSnapHelper] and attaches to `this`.
 *
 * @return `this`
 */
fun RecyclerView.withPagerSnapHelper(): RecyclerView = apply {
    PagerSnapHelper().attachToRecyclerView(this)
}
// endregion

private object QuickAdapterClick {
    var delayTime = 0L
    var lastClickTime = 0L
}

fun RecyclerView.singleItemClick(time: Long = 1000, block: (position: Int) -> Unit) {
    QuickAdapterClick.delayTime = time
    addOnItemTouchListener(object : OnItemClickListener() {
        override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, p: Int) {
            if (clickEnable()) {
                block(p)
            }
        }
    })
}

fun RecyclerView.singleItemLongClick(l: (position: Int) -> Unit) {
    this.addOnItemTouchListener(object : OnItemLongClickListener() {
        override fun onSimpleItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, p: Int) {
            l(p)
        }
    })
}

fun <T> BaseQuickAdapter<T, BaseViewHolder>.singleChildItemClick(time: Long = 1000, block: (position: Int, viewId: Int) -> Unit) {
    QuickAdapterClick.delayTime = time
    setOnItemChildClickListener { _, view, position ->
        if (clickEnable()) {
            block(position, view.id)
        }
    }
}

private fun clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - QuickAdapterClick.lastClickTime >= QuickAdapterClick.delayTime) {
        QuickAdapterClick.lastClickTime = currentClickTime
        flag = true
    }
    return flag
}

fun <T> buildAdapter(
        @LayoutRes id: Int,
        data: List<T> = mutableListOf(),
        action: BaseViewHolder.(T) -> Unit
): BaseQuickAdapter<T, BaseViewHolder> {
    return object : BaseQuickAdapter<T, BaseViewHolder>(id, data) {
        override fun convert(helper: BaseViewHolder, item: T) {
            action(helper, item)
        }
    }
}


fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}


fun RecyclerView.linearLayoutManager(orientation: Int = RecyclerView.VERTICAL) = run {
    this.layoutManager = LinearLayoutManager(this.context, orientation, false)
    this
}

fun RecyclerView.gridLayoutManager(spanCount: Int) = run {
    this.layoutManager = GridLayoutManager(this.context, spanCount)
    this
}

fun RecyclerView.setRecyclerViewClickable(clickable: Boolean) {
    isEnabled = clickable
    if (!clickable) {
        val itemTouchListener = object : RecyclerView.OnItemTouchListener {

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return !rv.isEnabled
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }

        }
        addOnItemTouchListener(itemTouchListener)
        tag = itemTouchListener
    } else {
        (tag as? RecyclerView.OnItemTouchListener)?.let {
            requestDisallowInterceptTouchEvent(true)
            removeOnItemTouchListener(it)
        }
    }
}

fun RecyclerView.indicatorOutsideRecyclerView(
    context : Context ,
    dot_selected : Int ,
    dot_unselected : Int ,
    dot_linear_layout : LinearLayout,
    dotModelList: MutableList<DotModel>
) {
    this.setOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val currentCompletelyVisible = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            bottomIndicatorTransaction(currentCompletelyVisible , dot_selected , dot_unselected , dot_linear_layout)
        }
    })
    addIndicator(context , dot_linear_layout , dot_selected , dot_unselected , dotModelList)
}

private fun addIndicator(context : Context , dot_linear_layout: LinearLayout , dot_selected : Int , dot_unselected: Int , dotModelList : MutableList<DotModel>){
    var imageIndicator : ImageView
    if(dot_linear_layout.childCount > 0)
        dot_linear_layout.removeAllViews()
    for(index in 0 until dotModelList.size){
        imageIndicator = ImageView(context)
        imageIndicator.setImageResource(dot_unselected)
        imageIndicator.setPadding(5 , 15 , 10 , 15)
        dot_linear_layout.addView(imageIndicator)
    }
    bottomIndicatorTransaction(0 , dot_selected , dot_unselected , dot_linear_layout)
}

private fun bottomIndicatorTransaction(position_selected : Int , dot_selected: Int , dot_unselected: Int , dot_linear_layout: LinearLayout){
    for(index in 0 until dot_linear_layout.childCount){
        if (dot_linear_layout.getChildAt(index) is ImageView){
            (dot_linear_layout.getChildAt(index) as ImageView).setImageResource(dot_unselected)
        }
    }
    (dot_linear_layout.getChildAt(position_selected) as ImageView).setImageResource(dot_selected)
}

data class DotModel (
    var index : Int
)