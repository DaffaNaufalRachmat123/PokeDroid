package com.pokedroid.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokeStatObject(
    @Expose
    @SerializedName("base_stat")
    val baseStat: Int,

    @Expose
    @SerializedName("effort")
    val effort: Int,

    @Expose
    @SerializedName("stat")
    val stat: PokemonStat
)
data class PokemonStat(
    @Expose
    @SerializedName("name")
    var name : String
)