package com.example.obssproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.obssproject.db.MovieDao
import com.example.obssproject.db.MovieDatabase
import com.example.obssproject.models.Movie
import com.example.obssproject.models.MoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SharedViewModel (
     val movieDatabase: MovieDatabase
) : ViewModel()  {
    private val _favoriteMovies = MutableLiveData<List<Movie>>()
    private val favoriteMovies: LiveData<List<Movie>> = _favoriteMovies

    init {
        getFavouriteMovies()
    }


    fun getFavouriteMovies() = movieDatabase.getMovieDAO().getFavouriteMovies()

    fun addFavourite(movie: Movie){
        viewModelScope.launch(Dispatchers.IO) {
            movieDatabase.getMovieDAO().insertMovie(movie)
        }
    }

    fun deleteFavourite(movie: Movie){
        viewModelScope.launch(Dispatchers.IO) {
            movieDatabase.getMovieDAO().deleteMovie(movie)
        }
    }

}