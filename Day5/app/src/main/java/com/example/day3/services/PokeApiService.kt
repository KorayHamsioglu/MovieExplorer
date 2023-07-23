package com.example.day3.services

import com.example.day3.models.AbilityResponse
import com.example.day3.models.Pokemon
import com.example.day3.models.PokemonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon/")
    suspend fun getPokemonInfo(@Query("limit")  limit: Int=20,
                               @Query("offset")  offset:Int=0): PokemonResponse
    @GET("pokemon/{id}")
    suspend fun getPokemonAbilities(@Path("id") id: String): AbilityResponse
}