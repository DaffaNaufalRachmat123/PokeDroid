package com.pokedroid.core.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pokedroid.core.model.Abilities
import com.pokedroid.core.model.Moves
import com.pokedroid.core.model.PokeStatObject
import com.pokedroid.core.model.Types

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey
    var id : Int = 0,
    var name : String = "",
    var write_name : String ,
    var height : String ,
    var weight : String
)