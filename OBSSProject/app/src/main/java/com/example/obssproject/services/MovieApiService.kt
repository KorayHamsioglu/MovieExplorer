package com.example.obssproject.services

import com.example.obssproject.models.MoviesResponse
import com.example.obssproject.utils.Constants.Companion.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key")apiKey:String=API_KEY,
        @Query("page")pageID: Int=1
    ): MoviesResponse
}