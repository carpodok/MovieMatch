package com.example.moviematchapp.presentation.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.moviematchapp.R
import com.example.moviematchapp.domain.models.TrendingMovie
import com.example.moviematchapp.utils.Constants

@SuppressLint("NotifyDataSetChanged")
class TrendingMoviesAdapter(
    var movies: List<TrendingMovie>,
    val onItemClickListener: (TrendingMovie) -> Unit,
    val onSaveBtnClickedListener: (TrendingMovie) -> Unit,
) :
    RecyclerView.Adapter<TrendingMoviesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.trendingMovieLinearListItemTitle)
        var rate: TextView = itemView.findViewById(R.id.trendingMovieLinearListItemRate)
        var poster: ImageView = itemView.findViewById(R.id.trendingMovieLinearListItemPoster)
        var saveBtn: ImageView = itemView.findViewById(R.id.trendingMovieListSaveBtn)

        init {


            itemView.setOnClickListener {
                val position = adapterPosition
                onItemClickListener.invoke(movies[position])
            }
            saveBtn.setOnClickListener {
                val position = adapterPosition
                onSaveBtnClickedListener.invoke(movies[position])
            }
        }

        fun bind(item: TrendingMovie) {
            title.text = item.title
            rate.text = item.voteAverage.toString()

            var url = ""
            var requestOptions = RequestOptions()


            if (item.isSaved) {
                saveBtn.setImageResource(R.drawable.ic_saved)
            } else {
                saveBtn.setImageResource(R.drawable.ic_unsaved)
            }


            when (currentLayoutMode) {
                1 -> {

                    url = "${Constants.BASE_URL_IMAGE_PATH}${item.posterPath}"

                    requestOptions = RequestOptions().transform(CircleCrop(), RoundedCorners(50))
                }

                2 -> {
                    url = "${Constants.BASE_URL_IMAGE_PATH}${item.backdropPath}"

                    requestOptions = RequestOptions().transform(CenterCrop())
                }
            }

            Glide
                .with(poster.context)
                .load(url)
                .apply(requestOptions)
                .placeholder(R.drawable.ic_film_reel)
                .error(R.drawable.ic_film_reel)
                .into(poster)

        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)



        return if (viewType == VIEW_TYPE_GRID) {
            Log.i("Adapter", "VIEW_TYPE_LINEAR")

            val linearView =
                layoutInflater.inflate(R.layout.item_linear_trending_movie, parent, false)
            ViewHolder(linearView)
        } else {
            Log.i("Adapter", "VIEW_TYPE_GRID")

            val gridView = layoutInflater.inflate(R.layout.item_grid_trending_movie, parent, false)
            ViewHolder(gridView)
        }
    }

    override fun getItemCount(): Int = movies.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun updateMovieSavedStatus(movieId: Int, isSaved: Boolean) {
        val position = movies.indexOfFirst {
            it.id == movieId
        }
        if (position != -1) {
            movies[position].isSaved = isSaved
            notifyItemChanged(position)
        }
    }



    private companion object {
        const val VIEW_TYPE_GRID = 1
        const val VIEW_TYPE_LINEAR = 2
    }


    private var currentLayoutMode = VIEW_TYPE_GRID // Default to Linear Layout

    override fun getItemViewType(position: Int): Int {
        return currentLayoutMode
    }

}