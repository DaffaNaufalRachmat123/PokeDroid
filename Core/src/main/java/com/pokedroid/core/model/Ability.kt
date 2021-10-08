package com.pokedroid.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Ability(
    @Expose
    @SerializedName("name")
    var name : String ,
    @Expose
    @SerializedName("url")
    var url : String
)