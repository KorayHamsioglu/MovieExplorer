package com.example.day2

import com.google.gson.annotations.SerializedName

data class NoteData(@SerializedName("title") var title: String,
                    @SerializedName("content") var content: String)
