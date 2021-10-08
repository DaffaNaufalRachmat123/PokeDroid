package com.pokedroid.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Types(
    @Expose
    @SerializedName("slot")
    var slot : Int ,
    @Expose
    @SerializedName("type")
    var type : Type
)

data class Type(
    @Expose
    @SerializedName("name")
    var name : String ,
    @Expose
    @SerializedName("url")
    var url : String
)