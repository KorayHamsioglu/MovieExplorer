package com.example.obssproject.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.obssproject.models.Movie

@Dao
interface MovieDao {


    @Query("SELECT * FROM favorite_movies")
    fun getFavouriteMovies(): LiveData<List<Movie>>

    @Insert
    suspend fun insertMovie(movie: Movie)
    
    @Delete
    suspend fun deleteMovie(movie: Movie)


}