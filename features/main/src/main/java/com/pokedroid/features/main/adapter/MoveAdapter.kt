package com.pokedroid.features.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pokedroid.core.model.Moves
import com.pokedroid.features.main.R
import com.pokedroid.features.main.databinding.ItemMoveBinding

class MoveAdapter : BaseQuickAdapter<Moves , BaseViewHolder>(R.layout.item_move) {
    override fun convert(helper: BaseViewHolder, item: Moves?) {
        val itemBinding = ItemMoveBinding.bind(helper.itemView)
        item?.let { model ->
            itemBinding.moveText.text = model.move.name
        }
    }
}