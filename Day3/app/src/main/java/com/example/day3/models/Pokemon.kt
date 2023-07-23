package com.example.day3.models

import com.google.gson.annotations.SerializedName

data class Pokemon(@SerializedName("name") val name: String?,
                   @SerializedName("url")val url:String?)
