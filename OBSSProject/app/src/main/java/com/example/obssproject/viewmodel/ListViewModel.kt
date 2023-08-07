package com.example.obssproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.obssproject.db.MovieDatabase
import com.example.obssproject.models.Movie
import com.example.obssproject.models.MoviesResponse
import com.example.obssproject.services.MovieApiService
import com.example.obssproject.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    private val movieApiService: MovieApiService
):ViewModel() {


    private val _filteredLiveDataMovieList=MutableLiveData<List<Movie>>()
    val filteredLiveDataMovieList: LiveData<List<Movie>> =_filteredLiveDataMovieList

    private val _isGridMode = MutableLiveData<Boolean>(true)
    val isGridMode: LiveData<Boolean> = _isGridMode

    private val _queryLiveData = MutableLiveData<String>()

    val movies: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData()
    var moviesPage=1
    var moviesResponse: MoviesResponse? = null

    init {
        callMovieList()
        _isGridMode.value = false
    }

    fun filterMovies(query: String){

        _queryLiveData.value=query


        val filteredMovies= movies.value?.data?.results?.filter {
            it.original_title?.contains(query,ignoreCase = true) ?: false
        }

        _filteredLiveDataMovieList.value=filteredMovies ?: emptyList()

    }


    fun callMovieList(){
        viewModelScope.launch(Dispatchers.IO) {
            movies.postValue(Resource.Loading())
            val response= movieApiService.getPopularMovies(pageID = moviesPage)
            movies.postValue(handleMoviesResponse(response))
           /* val response: MoviesResponse?=try {
                movieApiService.getPopularMovies()
            }catch (exception: Exception){
                null
            }*/

        }
    }

    fun switchView(){
        _isGridMode.value = !_isGridMode.value!!
    }

    private fun handleMoviesResponse(response: Response<MoviesResponse>) : Resource<MoviesResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
               moviesPage++
                if(moviesResponse==null){
                    moviesResponse=resultResponse
                }else{
                    val oldMovies=moviesResponse?.results
                    val newMovies=resultResponse.results
                    if (newMovies != null) {
                        oldMovies?.addAll(newMovies)
                    }
                }
                return Resource.Success(moviesResponse ?: resultResponse)
            }

        }
        return Resource.Error(response.message())
    }
}