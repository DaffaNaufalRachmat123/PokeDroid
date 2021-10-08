package com.pokedroid.features.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pokedroid.common.base.BaseViewModel
import com.pokedroid.common.extension.errorMessage
import com.pokedroid.common.utils.ViewState
import com.pokedroid.common.utils.setError
import com.pokedroid.common.utils.setLoading
import com.pokedroid.common.utils.setSuccess
import com.pokedroid.core.AppDispatchers
import com.pokedroid.core.local.entity.PokemonEntity
import com.pokedroid.core.model.PokeDetailResponse
import com.pokedroid.core.model.PokeResponse
import com.pokedroid.features.main.api.MainApi
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MainViewModel ( val api : MainApi , val dispatchers: AppDispatchers) : BaseViewModel() , KoinComponent {
    val pokeResponse = MutableLiveData<ViewState<PokeResponse>>()
    val pokeMoreResponse = MutableLiveData<ViewState<PokeResponse>>()
    val pokeDetailResponse = MutableLiveData<ViewState<PokeDetailResponse>>()
    val myPokemonList = daoProvider.pokemonDao.getMyPokemonList()

    fun catchPokemon(pokemonEntity : PokemonEntity) = viewModelScope.launch(dispatchers.main){
        daoProvider.pokemonDao.insertMyPoke(pokemonEntity)
    }

    fun removePokemon(pokemonEntity: PokemonEntity) = viewModelScope.launch(dispatchers.main){
        daoProvider.pokemonDao.removePokemon(pokemonEntity.write_name)
    }

    fun getPokemonList(limit : Int , offset : Int) = viewModelScope.launch(dispatchers.main){
        runCatching {
            pokeResponse.setLoading()
            api.getPokemonList(limit , offset)
        }.onSuccess {
            pokeResponse.setSuccess(it)
        }.onFailure {
            pokeResponse.setError(it.errorMessage())
        }
    }

    fun getPokemonMoreList(next : String) = viewModelScope.launch(dispatchers.main){
        runCatching {
            pokeMoreResponse.setLoading()
            api.getPokemonMoreList(next)
        }.onSuccess {
            pokeMoreResponse.setSuccess(it)
        }.onFailure {
            pokeMoreResponse.setError(it.errorMessage())
        }
    }

    fun getDetailPokemon(name : String) = viewModelScope.launch(dispatchers.main){
        runCatching {
            pokeDetailResponse.setLoading()
            api.getPokemonDetail(name)
        }.onSuccess {
            pokeDetailResponse.setSuccess(it)
        }.onFailure {
            pokeDetailResponse.setError(it.errorMessage())
        }
    }

}