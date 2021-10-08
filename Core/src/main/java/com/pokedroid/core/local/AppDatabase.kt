package com.pokedroid.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidpk.local.converter.RoomConverters
import com.pokedroid.core.local.dao.PokemonDao
import com.pokedroid.core.local.entity.PokemonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Database(
    entities = [
        PokemonEntity::class
    ], version = 1
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "pokemon-db"
    }

    abstract fun pokemonDao() : PokemonDao
    suspend fun clearTables() = withContext(Dispatchers.IO) {
        this@AppDatabase.clearAllTables()
    }
}
