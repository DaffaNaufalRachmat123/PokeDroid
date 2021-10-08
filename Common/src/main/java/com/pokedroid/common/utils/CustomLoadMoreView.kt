package com.pokedroid.common.utils

import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.pokedroid.common.R

class CustomLoadMoreView(private val footerLayout: Int = R.layout.footer_rv) : LoadMoreView() {

    override fun getLayoutId(): Int {
        return footerLayout
    }

    override fun getLoadingViewId(): Int {
        return R.id.load_more_loading_view
    }

    override fun getLoadFailViewId(): Int {
        return R.id.load_more_load_fail_view
    }

    override fun getLoadEndViewId(): Int {
        return R.id.load_more_load_end_view
    }

}