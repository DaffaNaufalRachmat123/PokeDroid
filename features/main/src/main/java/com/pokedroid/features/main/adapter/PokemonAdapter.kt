package com.pokedroid.features.main.adapter

import android.util.Log
import com.android.core.BuildConfig
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pokedroid.common.extension.loadImage
import com.pokedroid.core.model.Pokemon
import com.pokedroid.features.main.R
import com.pokedroid.features.main.databinding.ItemPokemonBinding

class PokemonAdapter : BaseQuickAdapter<Pokemon , BaseViewHolder>(R.layout.item_pokemon) {
    override fun convert(helper: BaseViewHolder, item: Pokemon?) {
        val itemBinding = ItemPokemonBinding.bind(helper.itemView)
        item?.let { model ->
            itemBinding.nameText.text = model.name
            val pokeNumber = model.url.split("${BuildConfig.BASE_URL}/api/v2/pokemon/")
            val imageUrl = "${BuildConfig.IMAGE_URL}${pokeNumber[1].split("/")[0]}.svg"
            itemBinding.pokeImage.loadImage(imageUrl)
        }
    }
}