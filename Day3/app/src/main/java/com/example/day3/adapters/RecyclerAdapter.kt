package com.example.day3.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.day3.R
import com.example.day3.models.Pokemon

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>(){


    private val list: ArrayList<Pokemon> = arrayListOf()

    interface ItemClickListener {
        fun onItemClick(itemPosition: Int)
    }
    private var itemClickListener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

    fun updateList(items: List<Pokemon>?){
        list.clear()
        items?.let { list.addAll(it) }
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {


        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.recycler_item,parent,false)

        return RecyclerViewHolder(itemView)

    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.bind(list[position])

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position+1)
        }
    }

    class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView= itemView.findViewById(R.id.textView)

        fun bind(item: Pokemon){
            name.text=item.name ?: "empty"
        }



    }
}