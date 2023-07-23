package com.example.day3.models

import com.google.gson.annotations.SerializedName

data class AbilityResponse(@SerializedName("abilities") val abilities: List<Abilities>?)
