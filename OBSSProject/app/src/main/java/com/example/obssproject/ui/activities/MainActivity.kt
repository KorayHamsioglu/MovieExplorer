package com.example.obssproject.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.obssproject.R
import com.example.obssproject.databinding.ActivityMainBinding
import com.example.obssproject.databinding.FragmentMovieDetailsBinding
import com.example.obssproject.db.MovieDatabase
import com.example.obssproject.viewmodel.SharedViewModel
import com.example.obssproject.viewmodel.SharedViewModelProviderFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var sharedViewModel: SharedViewModel

    private lateinit var fabButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_OBSSProject)
        super.onCreate(savedInstanceState)

        var binding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fabButton=binding.fab

        val sharedViewModelProviderFactory=SharedViewModelProviderFactory(MovieDatabase(this))
        sharedViewModel=ViewModelProvider(this,sharedViewModelProviderFactory).get(SharedViewModel::class.java)

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController=navHostFragment.navController




    }

    fun getFloatingActionButton(): FloatingActionButton {
        return fabButton
    }
}