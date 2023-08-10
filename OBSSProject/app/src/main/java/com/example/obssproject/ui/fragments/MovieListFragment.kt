package com.example.obssproject.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.LOG_TAG
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obssproject.R
import com.example.obssproject.adapters.MoviesAdapter
import com.example.obssproject.databinding.FragmentMovieListBinding
import com.example.obssproject.models.MoviesResponse
import com.example.obssproject.ui.activities.MainActivity
import com.example.obssproject.utils.Constants.Companion.PAGE_SIZE
import com.example.obssproject.utils.Resource
import com.example.obssproject.viewmodel.ListViewModel
import com.example.obssproject.viewmodel.SharedViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment(R.layout.fragment_movie_list),MoviesAdapter.ItemClickListener {

    private val viewModel by viewModels<ListViewModel>()
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var binding: FragmentMovieListBinding
    private lateinit var moviesAdapter: MoviesAdapter

    var isLoading=false
    var isScrolling=false
    var isLastPage=false

    val scrollListener= object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (recyclerView.layoutManager==GridLayoutManager(requireContext(),2)){
                val layoutManager=recyclerView.layoutManager as GridLayoutManager
                val firstItemPosition=layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount=layoutManager.childCount
                val totalItemCount=layoutManager.itemCount

                val isNotLoadingAndIsNotLastPage= !isLoading && !isLastPage
                val isAtLastItem = firstItemPosition + visibleItemCount >= totalItemCount
                val isNotAtBeginning = firstItemPosition >= 0
                val isTotalMoreThanVisible = totalItemCount >= 20
                val shouldPaginate = isNotLoadingAndIsNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling
                if(shouldPaginate) {
                    viewModel.callMovieList()
                    isScrolling = false
                }
            }else{
                val layoutManager=recyclerView.layoutManager as LinearLayoutManager
                val firstItemPosition=layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount=layoutManager.childCount
                val totalItemCount=layoutManager.itemCount

                val isNotLoadingAndIsNotLastPage= !isLoading && !isLastPage
                val isAtLastItem = firstItemPosition + visibleItemCount >= totalItemCount
                val isNotAtBeginning = firstItemPosition >= 0
                val isTotalMoreThanVisible = totalItemCount >= 20
                val shouldPaginate = isNotLoadingAndIsNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling
                if(shouldPaginate) {
                    viewModel.callMovieList()
                    isScrolling = false
                }
            }


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentMovieListBinding.bind(view)

        val bottomNavigationView = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        val floatingActionButton = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        bottomNavigationView.visibility=View.VISIBLE
        floatingActionButton.visibility=View.VISIBLE

        sharedViewModel=(activity as MainActivity).sharedViewModel
        println(sharedViewModel.getFavouriteMovies().value)

        listenSharedViewModel()
        initView(view)
        listenViewModel()
        switchListener()
        searchListener()



    }



    private fun initView(view: View) {
        view.apply {
            moviesAdapter=MoviesAdapter(viewModel.isGridMode.value ?: false)
            val layoutManager=LinearLayoutManager(view.context)
            binding.recyclerViewMovies.layoutManager=layoutManager
            binding.recyclerViewMovies.adapter=moviesAdapter
            binding.recyclerViewMovies.addOnScrollListener(scrollListener)

            moviesAdapter.setItemClickListener(this@MovieListFragment)



        }
    }

    private fun listenViewModel(){
        viewModel.apply {
            filteredLiveDataMovieList.observe(viewLifecycleOwner){
                moviesAdapter.differ.submitList(it.toList())
            }
            isGridMode.observe(viewLifecycleOwner){
                val layoutManager = if (it) {
                    GridLayoutManager(requireContext(), 2)
                } else {
                    LinearLayoutManager(requireContext())
                }
                binding.recyclerViewMovies.layoutManager = layoutManager
                moviesAdapter.updateViewMode(it)

            }
            movies.observe(viewLifecycleOwner, Observer { response ->
                when(response){
                    is Resource.Success -> {
                        hideLoadingBar()
                        response.data?.let { moviesResponse ->
                            moviesAdapter.differ.submitList(moviesResponse.results?.toList())
                            val totalPages= moviesResponse.total_pages
                            isLastPage= viewModel.moviesPage == totalPages
                            if (isLastPage){
                                binding.recyclerViewMovies.setPadding(0,0,0,0)
                            }
                        }
                    }
                    is Resource.Error -> {
                        hideLoadingBar()
                        response.message?.let { message ->
                            Log.e(LOG_TAG,"An error occured: $message")
                        }
                    }
                    is Resource.Loading -> {
                        showLoadingBar()
                    }
                }

            })
        }
    }

    override fun onItemClick(itemPosition: Int) {
        val movieList= viewModel.movies.value?.data?.results
        val currentMovie= movieList!![itemPosition-1]

        val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(currentMovie)

        findNavController().navigate(action)
    }

    fun listenSharedViewModel(){
        sharedViewModel.getFavouriteMovies().observe(viewLifecycleOwner, Observer { movies ->
            moviesAdapter.setFavouriteMovies(movies)
        })
    }

    private fun switchListener() {
        val fabButton = (requireActivity() as MainActivity).getFloatingActionButton()

        fabButton.setOnClickListener {
            viewModel.switchView()

            if (viewModel.isGridMode.value == false){
                fabButton.setImageResource(R.drawable.round_grid_view_24)

            }else{
                fabButton.setImageResource(R.drawable.round_format_list_bulleted_24)

            }
        }

    }

    private fun hideLoadingBar(){
        binding.progressBar.visibility=View.INVISIBLE
        isLoading=false
    }
    private fun showLoadingBar(){
        binding.progressBar.visibility=View.VISIBLE
        isLoading=true
    }


    fun searchListener(){

                    val searchView=binding.searchViewPopular
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if (!TextUtils.isEmpty(newText)) {
                                viewModel.filterMovies(newText!!)
                            }else {
                                viewModel.filterMovies("")
                            }
                            return true
                        }

                    })

        }

    }

