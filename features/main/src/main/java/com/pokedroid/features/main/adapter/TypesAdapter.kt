package com.pokedroid.features.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pokedroid.core.model.Types
import com.pokedroid.features.main.R
import com.pokedroid.features.main.databinding.ItemTypeBinding

class TypesAdapter : BaseQuickAdapter<Types , BaseViewHolder>(R.layout.item_type) {
    override fun convert(helper: BaseViewHolder, item: Types?) {
        val itemBinding = ItemTypeBinding.bind(helper.itemView)
        item?.let { model ->
            itemBinding.typeText.text = model.type.name
        }
    }
}