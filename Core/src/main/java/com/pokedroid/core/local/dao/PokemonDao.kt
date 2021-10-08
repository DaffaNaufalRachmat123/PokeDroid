package com.pokedroid.core.local.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pokedroid.core.local.entity.PokemonEntity

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon")
    fun getMyPokemonList() : LiveData<MutableList<PokemonEntity>>

    @Insert
    fun insertMyPoke(entity: PokemonEntity)

    @Query("DELETE FROM pokemon WHERE write_name = :write_name")
    fun removePokemon(write_name : String)
}