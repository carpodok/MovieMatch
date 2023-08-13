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
import com.example.moviematchapp.domain.models.NowPlayingMovie
import com.example.moviematchapp.utils.Constants

class NowPlayingMoviesAdapter (
    val nowPlayingMovies: List<NowPlayingMovie>,
    val onItemClickListener: (NowPlayingMovie) -> Unit,
) :
    RecyclerView.Adapter<NowPlayingMoviesAdapter.ViewHolder>() {



    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        val poster : ImageView = itemView.findViewById(R.id.nowPlayingMoviePoster)


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                onItemClickListener.invoke(nowPlayingMovies[position])
            }
        }

        fun bind(nowPlayingMovie: NowPlayingMovie) {

            val requestOptions = RequestOptions().transform(
                CenterInside(),
                RoundedCorners(30)
            )

            val posterUrl = "${Constants.BASE_URL_IMAGE_PATH}${nowPlayingMovie.poster}"

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
        val view = layoutInflater.inflate(R.layout.item_now_playing_movie, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = nowPlayingMovies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(nowPlayingMovies[position])
    }
}