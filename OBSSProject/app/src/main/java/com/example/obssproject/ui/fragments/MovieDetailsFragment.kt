package com.example.obssproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.example.obssproject.R
import com.example.obssproject.databinding.FragmentMovieDetailsBinding
import com.example.obssproject.databinding.FragmentMovieListBinding
import com.example.obssproject.models.Movie
import com.example.obssproject.ui.activities.MainActivity
import com.example.obssproject.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)

        sharedViewModel=(activity as MainActivity).sharedViewModel


        val args=MovieDetailsFragmentArgs.fromBundle(requireArguments())
        val currentMovie=args.movie

        listenSharedViewModel(currentMovie)
        initView(view,currentMovie)
    }

    private fun initView(view: View,movie: Movie){

        view.apply {
           binding.textViewDetailTitle.text=movie.title
            
            binding.toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    sharedViewModel.addFavourite(movie)
                }else{
                    sharedViewModel.deleteFavourite(movie)
                }
            }

        }
    }

    private fun listenSharedViewModel(movie: Movie) {
        sharedViewModel.getFavouriteMovies().observe(viewLifecycleOwner, Observer { movies ->
            binding.toggleButton.isChecked = movies.contains(movie)

        })
    }

}