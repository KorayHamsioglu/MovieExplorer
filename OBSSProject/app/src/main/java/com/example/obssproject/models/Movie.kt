package com.example.obssproject.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Entity(
    tableName = "favorite_movies"
)
@Parcelize
data class Movie(
    val adult: Boolean?,
    val backdrop_path: String?,
    val genre_ids: List<Int>?,
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?
): Parcelable