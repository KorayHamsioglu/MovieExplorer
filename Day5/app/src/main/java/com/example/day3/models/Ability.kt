package com.example.day3.models

import com.google.gson.annotations.SerializedName

data class Ability(@SerializedName("name") val name: String?,
                   @SerializedName("url") val url: String?)