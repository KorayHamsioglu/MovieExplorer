package com.example.obssproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.obssproject.R
import com.example.obssproject.databinding.FragmentMovieDetailsBinding
import com.example.obssproject.databinding.FragmentMovieListBinding
import com.example.obssproject.models.Movie
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private lateinit var binding: FragmentMovieDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)

         val args=MovieDetailsFragmentArgs.fromBundle(requireArguments())
         val currentMovie=args.movie

        initView(view,currentMovie)
    }

    private fun initView(view: View,movie: Movie){

        view.apply {
           binding.textViewDetailTitle.text=movie.title


        }
    }

}