package com.pokedroid.features.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pokedroid.common.extension.click
import com.pokedroid.common.extension.loadImage
import com.pokedroid.core.local.entity.PokemonEntity
import com.pokedroid.features.main.R
import com.pokedroid.features.main.databinding.ItemMyPokemonBinding

class PokiAdapter ( val onItemClicked : (PokemonEntity) -> Unit , val onRemoveClicked : (PokemonEntity) -> Unit) : BaseQuickAdapter<PokemonEntity , BaseViewHolder>(R.layout.item_my_pokemon) {
    override fun convert(helper: BaseViewHolder, item: PokemonEntity?) {
        val itemBinding = ItemMyPokemonBinding.bind(helper.itemView)
        item?.let { model ->
            itemBinding.writeNameText.text = model.write_name
            itemBinding.nameText.text = model.name
            itemBinding.weightText.text = "${model.weight} Kg"
            itemBinding.heightText.text = "${model.height} m"
            itemBinding.pokeImage.loadImage(model.imageUrl)
            itemBinding.parentClicked.click {
                onItemClicked.invoke(model)
            }
            itemBinding.icRemove.click {
                onRemoveClicked.invoke(model)
            }
        }
    }
}