package com.pokedroid.features.main.api

import com.pokedroid.core.model.PokeDetailResponse
import com.pokedroid.core.model.PokeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface MainApi {
    @GET("/api/v2/pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit : Int ,
        @Query("offset") offset : Int
    ) : PokeResponse
    @GET
    suspend fun getPokemonMoreList(
        @Url url : String
    ) : PokeResponse
    @GET("/api/v2/pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name : String
    ) : PokeDetailResponse
}