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
import com.pokedroid.common.extension.*
import com.pokedroid.common.utils.ViewState
import com.pokedroid.common.view.statelayout.StateLayout
import com.pokedroid.features.main.adapter.PokiAdapter
import com.pokedroid.features.main.databinding.ActivityMyPokemonBinding
import com.pokedroid.navigation.Activities

class ActivityMyPokemon : BaseActivity<MainViewModel>(R.layout.activity_my_pokemon) {
    private val binding by binding<ActivityMyPokemonBinding>()
    private var pokiAdapter : PokiAdapter? = null
    override fun getViewModel() = MainViewModel::class
    override fun observerViewModel() {
        viewModel.myPokemonList.observe(this , Observer {
            if(it != null){
                if(it.size > 0){
                    binding.recyclerViewPoki.show()
                    binding.linear.hide()
                    pokiAdapter?.setNewData(it)
                } else {
                    binding.recyclerViewPoki.hide()
                    binding.linear.show()
                    binding.btnCatch.click {
                        onBackPressed()
                    }
                }
            } else {
                binding.recyclerViewPoki.hide()
                binding.linear.show()
                binding.btnCatch.click {
                    onBackPressed()
                }
            }
        })
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        initToolbar(binding.toolbarMy , "Pokemon Saya")
        pokiAdapter = PokiAdapter(
            onItemClicked = { model ->
                startFeature(Activities.ActivityPokeDetail){
                    putExtra("name" , model.name)
                    putExtra("isCaught" , true)
                }
            },
            onRemoveClicked = { model ->
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
        )
        binding.recyclerViewPoki.apply {
            setHasFixedSize(true)
            itemAnimator = null
            layoutManager = LinearLayoutManager(this@ActivityMyPokemon , RecyclerView.VERTICAL , false)
            adapter = pokiAdapter
        }
        daoProvider.pokemonDao.getMyPokemonList()
    }
}