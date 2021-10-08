package com.pokedroid.core.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.github.ajalt.timberkt.i
import com.google.gson.Gson

class DatabaseProvider(private val context: Context) {

    init {
        initKotPref()
    }

    private fun initKotPref() {
        Kotpref.init(context)
        Kotpref.gson = Gson()
    }

    private var database: AppDatabase? = null

    /**
     * Gets an instance of [AppDatabase].
     *
     * @return an instance of [AppDatabase]
     */
    fun getInstance(): AppDatabase =
        database ?: synchronized(this) {
            database ?: buildDatabase().also { database = it }
        }

    private fun buildDatabase(): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "pokemon_db")
            .addCallback(onCreateDatabase())
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    private fun onCreateDatabase() = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }
    }

    suspend fun clearDatabase() {
        getInstance().clearTables()
    }
}
