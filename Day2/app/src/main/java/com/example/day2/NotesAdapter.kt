package com.example.day2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val notesList: MutableList<NoteData>,private val context: Context): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {


        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)

        return NotesViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return notesList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote=notesList[position]

        holder.noteTitle.text=currentNote.title

        holder.itemView.setOnClickListener{

            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("title", currentNote.title)
            intent.putExtra("content", currentNote.content)
            intent.putExtra("position", position)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    class NotesViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val noteTitle: TextView=itemView.findViewById(R.id.textViewTitle)
    }


}