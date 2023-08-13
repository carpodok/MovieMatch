package com.example.moviematchapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviematchapp.R
import com.example.moviematchapp.domain.models.Genre

class GenresAdapter(private val genres: List<Genre>) :
    RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val genreName: TextView = itemView.findViewById(R.id.genresItemName)

        fun bind(item: Genre) {
            genreName.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount(): Int = genres.size

}
