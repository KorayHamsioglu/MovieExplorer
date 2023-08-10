package com.example.obssproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.obssproject.R
import com.example.obssproject.adapters.MoviesAdapter
import com.example.obssproject.databinding.FragmentFavouritesBinding
import com.example.obssproject.databinding.FragmentMovieListBinding
import com.example.obssproject.db.MovieDatabase
import com.example.obssproject.models.Movie
import com.example.obssproject.ui.activities.MainActivity
import com.example.obssproject.viewmodel.ListViewModel
import com.example.obssproject.viewmodel.SharedViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites), MoviesAdapter.ItemClickListener {

    private val viewModel by viewModels<ListViewModel>()
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var moviesAdapter: MoviesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentFavouritesBinding.bind(view)

        val bottomNavigationView = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomNavigationView.visibility=View.VISIBLE

        sharedViewModel=(activity as MainActivity).sharedViewModel




        initView(view)
        listenSharedViewModel()

    }

    private fun initView(view: View) {
        view.apply {
            moviesAdapter=MoviesAdapter(viewModel.isGridMode.value ?: false)
            val layoutManager= LinearLayoutManager(view.context)
            println(sharedViewModel.getFavouriteMovies().value)
            binding.recyclerViewFavouriteMovies.layoutManager=layoutManager
            binding.recyclerViewFavouriteMovies.adapter=moviesAdapter



            moviesAdapter.setItemClickListener(this@FavouritesFragment)



        }
    }

    override fun onItemClick(itemPosition: Int) {
        val favouriteMovieList= sharedViewModel.getFavouriteMovies().value
        val currentMovie= favouriteMovieList!![itemPosition-1]

        val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(currentMovie)
        findNavController().navigate(action)

    }

    fun listenSharedViewModel(){
        sharedViewModel.getFavouriteMovies().observe(viewLifecycleOwner, Observer { movies ->
            moviesAdapter.updateList(movies)
        })
    }





}