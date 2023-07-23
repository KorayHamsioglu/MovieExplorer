package com.example.day3.models

import com.google.gson.annotations.SerializedName

data class PokemonResponse(@SerializedName("results") val results: List<Pokemon>?)
