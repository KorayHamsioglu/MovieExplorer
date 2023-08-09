package com.example.obssproject.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.obssproject.R
import com.example.obssproject.db.MovieDatabase
import com.example.obssproject.viewmodel.SharedViewModel
import com.example.obssproject.viewmodel.SharedViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_OBSSProject)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedViewModelProviderFactory=SharedViewModelProviderFactory(MovieDatabase(this))
        sharedViewModel=ViewModelProvider(this,sharedViewModelProviderFactory).get(SharedViewModel::class.java)
    }
}