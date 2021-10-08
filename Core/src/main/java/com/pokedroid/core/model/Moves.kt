package com.pokedroid.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Moves(
    @Expose
    @SerializedName("move")
    var move : Move,
    @Expose
    @SerializedName("version_group_details")
    var version_group_details : MutableList<VersionGroupDetails>
)

data class Move(
    @Expose
    @SerializedName("name")
    var name : String ,
    @Expose
    @SerializedName("url")
    var url : String
)

data class VersionGroupDetails(
    @Expose
    @SerializedName("level_learned_at")
    var level_learned_at : Int,
    @Expose
    @SerializedName("move_learn_method")
    var move_learn_method : MoveLearnMethod,
    @Expose
    @SerializedName("version_group")
    var version_group : VersionGroup
)

data class MoveLearnMethod(
    @Expose
    @SerializedName("name")
    var name : String ,
    @Expose
    @SerializedName("url")
    var url : String
)

data class VersionGroup(
    @Expose
    @SerializedName("name")
    var name : String ,
    @Expose
    @SerializedName("url")
    var url : String
)