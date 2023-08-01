package com.example.obssproject.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obssproject.R
import com.example.obssproject.adapters.MoviesAdapter
import com.example.obssproject.databinding.FragmentMovieListBinding
import com.example.obssproject.models.MoviesResponse
import com.example.obssproject.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment(R.layout.fragment_movie_list),MoviesAdapter.ItemClickListener {

    private val viewModel by viewModels<ListViewModel>()

    private lateinit var binding: FragmentMovieListBinding
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentMovieListBinding.bind(view)

        initView(view)
        listenViewModel()
    }

    private fun initView(view: View) {
        view.apply {
            moviesAdapter=MoviesAdapter(viewModel.isGridMode.value ?: false)
            val layoutManager=LinearLayoutManager(view.context)
            binding.recyclerViewMovies.layoutManager=layoutManager
            binding.recyclerViewMovies.adapter=moviesAdapter

            moviesAdapter.setItemClickListener(this@MovieListFragment)


            binding.imageViewGrid.setOnClickListener {
                viewModel.switchView()
            }

            binding.searchViewPopular.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
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

    private fun listenViewModel(){
        viewModel.apply {
            liveDataMovieList.observe(viewLifecycleOwner){
                moviesAdapter.updateList(it)
            }
            filteredLiveDataMovieList.observe(viewLifecycleOwner){
                moviesAdapter.updateList(it)
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
        }
    }

    override fun onItemClick(itemPosition: Int) {
        val movieList= viewModel.liveDataMovieList.value
        val currentMovie= movieList!![itemPosition-1]

        val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(currentMovie)

        findNavController().navigate(action)
    }

}