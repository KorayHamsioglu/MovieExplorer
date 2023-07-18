package com.example.day2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.day2.databinding.ActivityDetailsBinding
import com.example.day2.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesAdapter: NotesAdapter
    companion object {
         lateinit var noteList:  MutableList<NoteData>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("SharedPref", Context.MODE_PRIVATE)

        getList(sharedPreferences)

        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        notesAdapter= NotesAdapter(noteList,this.applicationContext)

        binding.recyclerView.adapter=notesAdapter

        if (noteList.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.textView.visibility=View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.textView.visibility=View.GONE
        }

        binding.imageButtonAdd.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("title", "new")
            startActivity(intent)
        }


    }



    fun getList(sharedPreferences: SharedPreferences){

        val listJson = sharedPreferences.getString("myList", null)
        if (listJson != null) {
            noteList = Gson().fromJson(listJson, Array<NoteData>::class.java)
                .toList().toMutableList()
        } else {
            noteList = mutableListOf()
        }
    }
}