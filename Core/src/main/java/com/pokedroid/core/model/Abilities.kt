package com.pokedroid.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Abilities(
    @Expose
    @SerializedName("ability")
    var ability: Ability,
    @Expose
    @SerializedName("is_hidden")
    var is_hidden : Boolean
)