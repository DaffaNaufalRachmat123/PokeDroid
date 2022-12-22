package com.pokedroid.features.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.core.BuildConfig
import com.dylanc.viewbinding.binding
import com.github.ajalt.timberkt.d
import com.pokedroid.common.base.BaseActivity
import com.pokedroid.common.extension.singleItemClick
import com.pokedroid.common.extension.startFeature
import com.pokedroid.common.extension.toast
import com.pokedroid.common.utils.CustomLoadMoreView
import com.pokedroid.common.utils.ViewState
import com.pokedroid.common.view.statelayout.StateLayout
import com.pokedroid.features.main.adapter.PokemonAdapter
import com.pokedroid.features.main.databinding.ActivityMainBinding
import com.pokedroid.navigation.Activities

class MainActivity : BaseActivity<MainViewModel>(R.layout.activity_main) {
    private val binding by binding<ActivityMainBinding>()
    private var pokeAdapter : PokemonAdapter? = null
    private val stateLayout by lazy {
        StateLayout(this)
            .wrap(binding.recyclerViewPoke)
            .showLoading()
    }
    private val limit = 25
    private var offset = 0
    override fun getViewModel() = MainViewModel::class
    override fun observerViewModel() {
        viewModel.pokeResponse.onResult { state ->
            when (state) {
                is ViewState.Loading -> {
                    stateLayout.showLoading()
                }
                is ViewState.Success -> {
                    if(state.data.results.size > 0){
                        stateLayout.showContent()
                        pokeAdapter?.setNewData(state.data.results)
                        canLoadMore(state.data.next)
                    } else {
                        stateLayout.showEmpty()
                    }
                }
                is ViewState.Failed -> {
                    stateLayout.showError()
                    stateLayout.onRetry {
                        viewModel.getPokemonList(limit , offset)
                    }
                }
            }
        }
        viewModel.pokeMoreResponse.onResult { state ->
            when (state) {
                is ViewState.Loading -> {}
                is ViewState.Success -> {
                    if(state.data.results.size > 0){
                        pokeAdapter?.addData(state.data.results)
                        canLoadMore(state.data.next)
                    }
                }
                is ViewState.Failed -> {}
            }
        }
    }

    private fun canLoadMore(next_page : String?){
        if(next_page == null){
            pokeAdapter?.setEnableLoadMore(false)
        } else {
            pokeAdapter?.setEnableLoadMore(true)
            pokeAdapter?.setLoadMoreView(CustomLoadMoreView())
            pokeAdapter?.setOnLoadMoreListener({
                if(next_page == null){
                    pokeAdapter?.loadMoreEnd()
                } else {
                    viewModel.getPokemonMoreList(next_page)
                }
            } , binding.recyclerViewPoke)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main , menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.myPokemon -> {
                startFeature(Activities.ActivityMyPokemon){}
            }
        }
        return true
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        initToolbar(binding.toolbarMain , "PokeDroid" , false)
        pokeAdapter = PokemonAdapter()
        binding.recyclerViewPoke.apply {
            setHasFixedSize(true)
            itemAnimator = null
            layoutManager = LinearLayoutManager(this@MainActivity  , RecyclerView.VERTICAL , false)
            adapter = pokeAdapter
            singleItemClick { position ->
                pokeAdapter?.let {
                    val pokeNumber = it.data[position].url.split("${BuildConfig.BASE_URL}/api/v2/pokemon/")
                    val imageUrl = "${BuildConfig.IMAGE_URL}${pokeNumber[1].split("/")[0]}.svg"
                    startFeature(Activities.ActivityPokeDetail){
                        putExtra("name" , it.data[position].name ?: "")
                        putExtra("imageUrl" , imageUrl)
                    }
                }
            }
        }
        viewModel.getPokemonList(limit , offset)
    }
}