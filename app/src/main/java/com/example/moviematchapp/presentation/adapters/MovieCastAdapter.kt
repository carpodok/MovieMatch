package com.example.moviematchapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.moviematchapp.R
import com.example.moviematchapp.domain.models.MovieCast
import com.example.moviematchapp.utils.Constants
import com.example.moviematchapp.utils.RoundedCornersTransform

class MovieCastAdapter(val movieCast: List<MovieCast>) :
    RecyclerView.Adapter<MovieCastAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val castName: TextView = itemView.findViewById(R.id.movieCastName)
        val castCharacter: TextView = itemView.findViewById(R.id.movieCastCharacter)
        val castprofile: ImageView = itemView.findViewById(R.id.movieCastProfile)

        fun bind(item: MovieCast) {

            castName.text = item.name
            castCharacter.text = item.character

            val posterUrl = "${Constants.BASE_URL_IMAGE_PATH}${item.profilePath}"

            val requestOptions = RequestOptions().transform(
                CenterCrop(),
                RoundedCornersTransform(
                    radiusTopLeft = 15f,
                    radiusTopRight = 15f,
                    radiusBottomRight = 0f,
                    radiusBottomLeft = 0f,
                )
            )

            var emptyProfileResourceId = 0

            if (item.gender == 1) {

                emptyProfileResourceId = R.drawable.ic_profile_female

            } else if (item.gender == 2) {
                emptyProfileResourceId = R.drawable.ic_profile_male
            } else {
                emptyProfileResourceId = R.drawable.ic_film_reel
            }


            Glide
                .with(castprofile.context)
                .load(posterUrl)
                .apply(requestOptions)
                .placeholder(emptyProfileResourceId)
                .error(emptyProfileResourceId)
                .into(castprofile)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_movie_cast, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = movieCast.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movieCast[position])
    }
}