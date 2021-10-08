package com.pokedroid.core.local

import com.pokedroid.core.local.dao.PokemonDao

class DaoProvider(database : DatabaseProvider) {
    val pokemonDao : PokemonDao = database.getInstance().pokemonDao()
}