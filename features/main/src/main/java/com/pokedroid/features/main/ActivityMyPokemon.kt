package com.pokedroid.features.main

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.dylanc.viewbinding.binding
import com.github.ajalt.timberkt.d
import com.pokedroid.common.base.BaseActivity
import com.pokedroid.common.extension.hide
import com.pokedroid.common.extension.show
import com.pokedroid.common.extension.toast
import com.pokedroid.common.utils.ViewState
import com.pokedroid.common.view.statelayout.StateLayout
import com.pokedroid.features.main.adapter.PokiAdapter
import com.pokedroid.features.main.databinding.ActivityMyPokemonBinding

class ActivityMyPokemon : BaseActivity<MainViewModel>(R.layout.activity_my_pokemon) {
    private val binding by binding<ActivityMyPokemonBinding>()
    private var pokiAdapter : PokiAdapter? = null
    private val stateLayout by lazy {
        StateLayout(this)
            .wrap(binding.recyclerViewPoki)
    }
    override fun getViewModel() = MainViewModel::class
    override fun observerViewModel() {
        viewModel.myPokemonList.observe(this , Observer {
            if(it != null){
                binding.recyclerViewPoki.show()
                binding.notFoundText.hide()
                pokiAdapter?.setNewData(it)
            } else {
                binding.recyclerViewPoki.hide()
                binding.notFoundText.show()
            }
        })
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        pokiAdapter = PokiAdapter {  model ->
            MaterialDialog(this).show {
                title(text = "Perhatian !")
                message(text = "Yakin mau hapus data ini?")
                positiveButton(text = "Ya"){
                    it.dismiss()
                    viewModel.removePokemon(model)

                }
                negativeButton(text = "Tidak") {
                    it.dismiss()
                }
            }
        }
        binding.recyclerViewPoki.apply {
            setHasFixedSize(true)
            itemAnimator = null
            layoutManager = LinearLayoutManager(this@ActivityMyPokemon , RecyclerView.VERTICAL , false)
            adapter = pokiAdapter
        }
        daoProvider.pokemonDao.getMyPokemonList()
    }
}