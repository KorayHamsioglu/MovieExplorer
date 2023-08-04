package com.example.obssproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.obssproject.db.MovieDatabase
import com.example.obssproject.models.Movie
import com.example.obssproject.models.MoviesResponse
import com.example.obssproject.services.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    private val movieApiService: MovieApiService
):ViewModel() {
    private val _liveDataMovieList=MutableLiveData<List<Movie>>()
    val liveDataMovieList: LiveData<List<Movie>> = _liveDataMovieList

    private val _filteredLiveDataMovieList=MutableLiveData<List<Movie>>()
    val filteredLiveDataMovieList: LiveData<List<Movie>> =_filteredLiveDataMovieList

    private val _isGridMode = MutableLiveData<Boolean>(true)
    val isGridMode: LiveData<Boolean> = _isGridMode

    private val _queryLiveData = MutableLiveData<String>()
    val queryLiveData: LiveData<String> = _queryLiveData

    init {
        callMovieList()
        _isGridMode.value = false
    }

    fun filterMovies(query: String){

        _queryLiveData.value=query

        val filteredMovies= liveDataMovieList.value?.filter {
            it.original_title?.contains(query,ignoreCase = true) ?: false
        }

        _filteredLiveDataMovieList.value=filteredMovies ?: emptyList()

    }


    private fun callMovieList(){
        viewModelScope.launch(Dispatchers.IO) {
            val moviesResponse: MoviesResponse?=try {
                movieApiService.getPopularMovies()
            }catch (exception: Exception){
                null
            }

            val moviesList=moviesResponse?.results?: emptyList()
            _liveDataMovieList.postValue(moviesList)
        }
    }

    fun switchView(){
        _isGridMode.value = !_isGridMode.value!!
    }
}