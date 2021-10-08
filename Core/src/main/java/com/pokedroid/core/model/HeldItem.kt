package com.pokedroid.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HeldItem(
    @Expose
    @SerializedName("item")
    var item : Item,
    @Expose
    @SerializedName("version_details")
    var version_details : MutableList<VersionDetails>
)