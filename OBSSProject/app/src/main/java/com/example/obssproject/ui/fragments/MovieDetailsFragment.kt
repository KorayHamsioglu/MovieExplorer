package com.example.obssproject.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Transition
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget

import com.example.obssproject.R
import com.example.obssproject.databinding.FragmentMovieDetailsBinding
import com.example.obssproject.databinding.FragmentMovieListBinding
import com.example.obssproject.models.Movie
import com.example.obssproject.ui.activities.MainActivity
import com.example.obssproject.utils.Constants
import com.example.obssproject.viewmodel.SharedViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var sharedViewModel: SharedViewModel
    private val favouriteMovieIdList: ArrayList<Int> = arrayListOf()
    private var isToggleCheckedByCode = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailsBinding.bind(view)

        val bottomNavigationView = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
        val floatingActionButton = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        bottomNavigationView.visibility=View.GONE
        floatingActionButton.visibility=View.GONE

        sharedViewModel=(activity as MainActivity).sharedViewModel







        val args=MovieDetailsFragmentArgs.fromBundle(requireArguments())
        val currentMovie=args.movie



        listenSharedViewModel(currentMovie)


        initView(view,currentMovie)
        toggleButtonListener(currentMovie)
        topAppBarNavigation()
    }




    private fun initView(view: View,movie: Movie){

        view.apply {
            val imageUrl= Constants.IMAGE_BASE_URL +movie.poster_path
            val imageViewDetails=binding.imageViewDetailPoster
            val backGroundImage=binding.collapseBackground
            val backDropUrl= Constants.IMAGE_BASE_URL +movie.backdrop_path
            val voteCount=movie.vote_count

            val rating=movie.vote_average
            val formattedRating=String.format("%.1f",rating)



            binding.textViewDetailTitle.text=movie.title
            binding.textViewDetailLanguage.text=movie.original_language
            binding.textViewDetailDescription.text=movie.overview
            binding.textViewDetailRating.text=formattedRating+"     ($voteCount)"

            val displayMetrics = DisplayMetrics()
            val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels

            val requestOptions = RequestOptions()
                .override(screenWidth, 525) // Yeni genişlik ve yükseklik değerlerini burada belirleyin
                .centerCrop()

            Glide.with(imageViewDetails).load(imageUrl).into(imageViewDetails)
            Glide.with(backGroundImage).load(backDropUrl).apply(requestOptions).into(backGroundImage)





        }
    }

    private fun toggleButtonListener(movie: Movie){
        binding.toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if(!isToggleCheckedByCode){
                if (isChecked){
                    sharedViewModel.addFavourite(movie)
                }else{
                    sharedViewModel.deleteFavourite(movie)

                }
            }

        }
    }

    private fun topAppBarNavigation(){
        binding.topAppBarDetails.setNavigationOnClickListener {
            val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentToMovieListFragment()
            findNavController().navigate(action)


        }
    }



    private fun listenSharedViewModel(movie: Movie) {
        sharedViewModel.getFavouriteMovies().observe(viewLifecycleOwner, Observer { movies ->
            isToggleCheckedByCode=true
            movies.let {
                favouriteMovieIdList.addAll(it.map { movie -> movie.id ?: 0 })
            }

            binding.toggleButton.isChecked = favouriteMovieIdList.contains(movie.id)

            isToggleCheckedByCode = false

        })
    }

}