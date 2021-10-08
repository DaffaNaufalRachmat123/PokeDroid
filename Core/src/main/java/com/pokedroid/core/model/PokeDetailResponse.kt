package com.pokedroid.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokeDetailResponse(
    @Expose
    @SerializedName("abilities")
    var abilities : MutableList<Abilities>,
    @Expose
    @SerializedName("height")
    var height : Int ,
    @Expose
    @SerializedName("weight")
    var weight : Int ,
    @Expose
    @SerializedName("types")
    var types : MutableList<Types> ,
    @Expose
    @SerializedName("name")
    var name : String ,
    @Expose
    @SerializedName("id")
    var id : Int ,
    @Expose
    @SerializedName("is_default")
    var is_default : Boolean ,
    @Expose
    @SerializedName("stats")
    var stats : MutableList<PokeStatObject>,
    @Expose
    @SerializedName("location_area_encounters")
    var location_area_encounters : String ,
    @Expose
    @SerializedName("moves")
    var moves : MutableList<Moves>
)