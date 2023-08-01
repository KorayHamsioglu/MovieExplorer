package com.example.obssproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obssproject.R
import com.example.obssproject.models.Movie
import com.example.obssproject.utils.Constants.Companion.BASE_URL
import com.example.obssproject.utils.Constants.Companion.IMAGE_BASE_URL
import java.lang.IllegalArgumentException

class MoviesAdapter(private var isGridMode: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val movieList: ArrayList<Movie> = arrayListOf()

    interface ItemClickListener {
        fun onItemClick(itemPosition: Int)
    }

    private var itemClickListener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

    companion object {
        private const val VIEW_TYPE_GRID = 1
        private const val VIEW_TYPE_LIST = 2
    }

    fun updateList(items: List<Movie>?){
        movieList.clear()
        items?.let { movieList.addAll(it) }
        notifyDataSetChanged()
    }

    fun updateViewMode(isGridMode: Boolean) {
        this.isGridMode = isGridMode
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType== VIEW_TYPE_LIST){
            val itemView= LayoutInflater.from(parent.context).inflate(R.layout.list_recycler_item,parent,false)
            ListViewHolder(itemView)
        }else{
             val itemView= LayoutInflater.from(parent.context).inflate(R.layout.grid_recycler_item,parent,false)
             GridViewHolder(itemView)
         }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie=movieList[position]
        when(holder){
            is ListViewHolder-> holder.bind(movie)
            is GridViewHolder-> holder.bind(movie)
            else -> throw IllegalArgumentException("Unknown ViewHolder")
        }

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position+1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridMode){
            VIEW_TYPE_GRID
        }else {
            VIEW_TYPE_LIST
        }
    }



    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val movieTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val movieImage: ImageView=itemView.findViewById(R.id.imageViewList)

        fun bind(item: Movie) {
            val imageUrl= IMAGE_BASE_URL+item.poster_path

            movieTitle.text = item.title ?: "empty"
            Glide.with(movieImage).load(imageUrl).into(movieImage)
            Log.i("IMAGEE",imageUrl ?: "empty")
        }
    }

        class GridViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val movieTitle: TextView=itemView.findViewById(R.id.textViewTitleGrid)
            private val movieImage: ImageView=itemView.findViewById(R.id.imageViewGrid)


            fun bind(item: Movie){
                val imageUrl=IMAGE_BASE_URL+item.poster_path

                movieTitle.text=item.title ?: "empty"
                Glide.with(movieImage).load(imageUrl).into(movieImage)
            }

    }
}