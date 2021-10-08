package com.pokedroid.features.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pokedroid.core.model.PokeStatObject
import com.pokedroid.features.main.R
import com.pokedroid.features.main.databinding.ItemStatBinding

class StatAdapter : BaseQuickAdapter<PokeStatObject , BaseViewHolder>(R.layout.item_stat) {
    override fun convert(helper: BaseViewHolder, item: PokeStatObject?) {
        val itemBinding = ItemStatBinding.bind(helper.itemView)
        item?.let { model ->
            itemBinding.progressIndicator.max = 100
            itemBinding.progressIndicator.progress = model.baseStat
            itemBinding.nameText.text = model.stat.name
            itemBinding.effortText.text = model.baseStat.toString()
        }
    }
}