package com.example.obssproject.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.obssproject.models.Movie
import com.example.obssproject.models.MoviesResponse
import com.example.obssproject.services.MovieApiService
import com.example.obssproject.utils.Constants.Companion.API_KEY

class Paging(private val movieApiService: MovieApiService): PagingSource<Int, Movie>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage=params.key ?: 1
            val response= movieApiService.getPopularMovies(API_KEY,currentPage)
            val data= response.results
            val responseData= mutableListOf<Movie>()

            if (data != null) {
                responseData.addAll(data)
            }

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage==1) null else -1,
                nextKey = currentPage.plus(1)
            )
        }catch (exception: Exception){
            LoadResult.Error(exception)

        }

    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }
}