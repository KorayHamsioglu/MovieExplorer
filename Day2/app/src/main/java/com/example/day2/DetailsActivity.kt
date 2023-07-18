package com.example.day2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.day2.databinding.ActivityDetailsBinding
import com.google.gson.Gson

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var noteData: NoteData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titleIntent=intent.getStringExtra("title")
        val position=intent.getIntExtra("position",0)
        if (!titleIntent.equals("new")){
            val contentIntent=intent.getStringExtra("content")
            binding.editTextTitle.setText(titleIntent)
            binding.editTextContent.setText(contentIntent)
        }else{
            binding.imageButtonDelete.isEnabled=false
        }

        binding.imageButtonSave.setOnClickListener{
            var title= binding.editTextTitle.text.toString()
            var content= binding.editTextContent.text.toString()
            if (titleIntent.equals("new")){
                noteData=NoteData(title,content)
                MainActivity.noteList.add(noteData)
            }else{

                MainActivity.noteList[position].title=title
                MainActivity.noteList[position].content=content

            }
            saveList()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()


        }
        binding.imageButtonDelete.setOnClickListener {
            popUp(position)

        }
    }
    fun saveList() {
        val sharedPreferences = getSharedPreferences("SharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val newListJson = Gson().toJson(MainActivity.noteList)
        editor.putString("myList", newListJson)
        editor.apply()
    }

    fun popUp(position: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Note")
        builder.setMessage("Are you sure you want to delete the note?")


        builder.setPositiveButton("Yes") { dialog, which ->
            MainActivity.noteList.removeAt(position)
            saveList()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        builder.setNegativeButton("No") { dialog, which ->

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }
}