package com.example.moviematchapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.moviematchapp.R
import com.example.moviematchapp.domain.models.UpcomingMovie
import com.example.moviematchapp.utils.Constants

class UpcomingMoviesAdapter(
    val upcomingMovies: List<UpcomingMovie>,
    val onItemClickListener: (UpcomingMovie) -> Unit,
) :
    RecyclerView.Adapter<UpcomingMoviesAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val poster: ImageView = itemView.findViewById(R.id.upcomingMoviePoster)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                onItemClickListener.invoke(upcomingMovies[position])
            }
        }

        fun bind(upcomingMovie: UpcomingMovie) {

            val requestOptions = RequestOptions().transform(
                CenterInside(),
                RoundedCorners(30)
            )

            val posterUrl = "${Constants.BASE_URL_IMAGE_PATH}${upcomingMovie.poster}"

            Glide
                .with(poster.context)
                .load(posterUrl)
                .apply(requestOptions)
                .placeholder(R.drawable.ic_film_reel2)
                .error(R.drawable.ic_film_reel2)
                .into(poster)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_upcoming_movie_list, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = upcomingMovies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(upcomingMovies[position])
    }
}