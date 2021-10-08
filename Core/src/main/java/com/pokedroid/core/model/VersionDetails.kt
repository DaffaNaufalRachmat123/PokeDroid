package com.pokedroid.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VersionDetails(
    @Expose
    @SerializedName("rarity")
    var rarity : Int ,
    @Expose
    @SerializedName("version")
    var version : Version
)

data class Version(
    @Expose
    @SerializedName("name")
    var name : String ,
    @Expose
    @SerializedName("url")
    var url : String
)