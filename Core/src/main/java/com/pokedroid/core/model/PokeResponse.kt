package com.pokedroid.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokeResponse(
    @Expose
    @SerializedName("count")
    var count : Int ,
    @Expose
    @SerializedName("next")
    var next : String ,
    @Expose
    @SerializedName("previous")
    var previous : String ,
    @Expose
    @SerializedName("results")
    var results : MutableList<Pokemon>
)