package com.example.obssproject.viewmodel

import android.util.Log
import android.widget.Toast
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
     val _favoriteMovies = MutableLiveData<List<Movie>>()
     val favoriteMovies: LiveData<List<Movie>> = _favoriteMovies

    init {
        getFavouriteMovies()
        setFavouriteMovies()
    }

    fun getFavouriteMovies() = movieDatabase.getMovieDAO().getFavouriteMovies()

    fun setFavouriteMovies() {
        _favoriteMovies.postValue(movieDatabase.getMovieDAO().getFavouriteMovies().value)
    }


    fun addFavourite(movie: Movie){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieDatabase.getMovieDAO().insertMovie(movie)
            }catch (exception: Exception){
                Log.e("ADDED","Movie has already added")
            }

        }
    }

    fun deleteFavourite(movie: Movie){
        viewModelScope.launch(Dispatchers.IO) {
            movieDatabase.getMovieDAO().deleteMovie(movie)
        }
    }

}