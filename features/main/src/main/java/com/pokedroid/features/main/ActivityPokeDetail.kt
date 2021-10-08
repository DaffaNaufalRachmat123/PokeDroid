package com.pokedroid.features.main

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.dylanc.viewbinding.binding
import com.pokedroid.common.base.BaseActivity
import com.pokedroid.common.extension.click
import com.pokedroid.common.extension.extra
import com.pokedroid.common.extension.extraOrNull
import com.pokedroid.common.extension.toast
import com.pokedroid.common.utils.ViewState
import com.pokedroid.common.view.statelayout.StateLayout
import com.pokedroid.core.local.entity.PokemonEntity
import com.pokedroid.core.model.PokeDetailResponse
import com.pokedroid.features.main.adapter.MoveAdapter
import com.pokedroid.features.main.adapter.StatAdapter
import com.pokedroid.features.main.adapter.TypesAdapter
import com.pokedroid.features.main.databinding.ActivityPokeDetailBinding
import com.pokedroid.features.main.databinding.InputNameDialogBinding
import java.util.*

class ActivityPokeDetail : BaseActivity<MainViewModel>(R.layout.activity_poke_detail) {
    private val binding by binding<ActivityPokeDetailBinding>()
    private val name by extra<String>("name")
    private val isCaught by extraOrNull<Boolean>("isCaught")
    private val stateLayout by lazy {
        StateLayout(this)
            .wrap(binding.parentContainer)
            .showLoading()
    }
    private var statAdapter : StatAdapter? = null
    private var moveAdapter : MoveAdapter? = null
    private var typeAdapter : TypesAdapter? = null
    private var pokeDetail : PokeDetailResponse? = null
    override fun getViewModel() = MainViewModel::class
    override fun observerViewModel() {
        viewModel.pokeDetailResponse.onResult { state ->
            when (state) {
                is ViewState.Loading -> {
                    stateLayout.showLoading()
                }
                is ViewState.Success -> {
                    if(state.data != null){
                        pokeDetail = state.data
                        initToolbar(binding.toolbarDetail , state.data.name)
                        stateLayout.showContent()
                        binding.nameText.text = state.data.name
                        binding.heightText.text = "Height : ${state.data.height} m"
                        binding.weightText.text = "Weight : ${state.data.weight} kg"
                        if(state.data.abilities.size > 1){
                            binding.firstText.text = state.data.abilities[0].ability.name ?: ""
                            binding.secondText.text = state.data.abilities[1].ability.name ?: ""
                        } else {
                            binding.firstText.text = state.data.abilities[0].ability.name ?: ""
                        }
                        statAdapter?.setNewData(state.data.stats)
                        moveAdapter?.setNewData(state.data.moves)
                        typeAdapter?.setNewData(state.data.types)

                        viewModel.isCaughtPokemon(state.data.name).observe(this , androidx.lifecycle.Observer {
                            if(it != null){
                                binding.btnCatch.text = "You've Caught This"
                                binding.btnCatch.background = ContextCompat.getDrawable(this@ActivityPokeDetail , R.drawable.bg_top_rounded_disabled)
                                binding.btnCatch.isEnabled = false
                                binding.btnCatch.isClickable = false
                            } else {
                                binding.btnCatch.text = "Catch This!"
                                binding.btnCatch.background = ContextCompat.getDrawable(this@ActivityPokeDetail , R.drawable.bg_top_rounded)
                                binding.btnCatch.isEnabled = true
                                binding.btnCatch.isClickable = true
                            }
                        })
                    } else {
                        stateLayout.showError()
                        stateLayout.onRetry {
                            viewModel.getDetailPokemon(name)
                        }
                    }
                }
                is ViewState.Failed -> {
                    stateLayout.showError()
                    stateLayout.onRetry {
                        viewModel.getDetailPokemon(name)
                    }
                }
            }
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        statAdapter = StatAdapter()
        moveAdapter = MoveAdapter()
        typeAdapter = TypesAdapter()
        binding.recyclerViewStat.apply {
            setHasFixedSize(true)
            itemAnimator = null
            layoutManager = LinearLayoutManager(this@ActivityPokeDetail , RecyclerView.VERTICAL , false)
            adapter = statAdapter
        }
        binding.recyclerViewMoves.apply {
            setHasFixedSize(true)
            itemAnimator = null
            layoutManager = LinearLayoutManager(this@ActivityPokeDetail , RecyclerView.VERTICAL , false)
            adapter = moveAdapter
        }
        binding.recyclerViewTypes.apply {
            setHasFixedSize(true)
            itemAnimator = null
            layoutManager = LinearLayoutManager(this@ActivityPokeDetail , RecyclerView.VERTICAL , false)
            adapter = typeAdapter
        }
        binding.btnCatch.click {
            if(Random().nextInt(2) == 0){
                toast("Maaf , kamu gagal dapetin Pokemon nya")
            } else {
                toast("Hore , kamu dapet Pokemon nya nih")
                val dialog = MaterialDialog(this).show {
                    customView(R.layout.input_name_dialog , noVerticalPadding = true)
                    cornerRadius(16f)
                }
                val itemBinding = InputNameDialogBinding.bind(dialog.getCustomView())
                itemBinding.btnSave.click {
                    dialog.dismiss()
                    pokeDetail?.let { model ->
                        val pokeEntity = PokemonEntity(
                            model.id,
                            model.name,
                            itemBinding.nameText.text.toString(),
                            model.height.toString(),
                            model.weight.toString()
                        )
                        toast("You Caught It!")
                        viewModel.catchPokemon(pokeEntity)
                    }
                }
            }
        }
        viewModel.getDetailPokemon(name)
    }
}