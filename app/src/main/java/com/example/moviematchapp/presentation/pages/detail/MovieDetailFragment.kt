package com.example.moviematchapp.presentation.pages.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.moviematchapp.R
import com.example.moviematchapp.databinding.FragmentMovieDetailBinding
import com.example.moviematchapp.domain.models.MovieDetail
import com.example.moviematchapp.presentation.adapters.GenresAdapter
import com.example.moviematchapp.presentation.adapters.MovieCastAdapter
import com.example.moviematchapp.utils.Constants
import com.example.moviematchapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private val movieDetailViewModel: MovieDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = requireArguments().getInt(BUNDLE_KEY_MOVIE_ID)

        movieDetailViewModel.getTrendingMovieAllDetailBy(movieId)
        observeData()
    }

    private fun observeData() {

        viewLifecycleOwner.lifecycleScope.launch {
            movieDetailViewModel.movieDetailState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBarMovieDetail.visibility = View.VISIBLE
                        binding.movieTitleTW.visibility = View.GONE
                        Log.i("MovieDetailFragment", "Loading...")
                    }

                    Status.SUCCESS -> {
                        binding.progressBarMovieDetail.visibility = View.GONE
                        binding.movieTitleTW.visibility = View.VISIBLE

                        it.data?.let { movieDetail ->
                            binding.movieTitleTW.text = movieDetail.title
                            binding.movieOverview.text = movieDetail.overview

                            setBackdropImage(imagePath = movieDetail.backdropPath)

                            setSavedImageIcon(movieDetail)

                            binding.movieDetailSaveBtn.setOnClickListener {
                                onSaveBtnClicked(movieDetail)
                            }

                            // setSavedImageIcon(movieDetail)

                            val adapter = GenresAdapter(movieDetail.genres)

                            binding.genresRecyclerView.adapter = adapter
                            binding.genresRecyclerView.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )


                        } ?: run {
                            Log.e("MovieDetailFragment", "Error: Failed to fetch movie detail.")
                        }
                    }

                    Status.ERROR -> {
                        binding.progressBarMovieDetail.visibility = View.GONE
                        binding.movieTitleTW.visibility = View.GONE

                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()

                        Log.e("MovieDetailFragment", it.message.toString())
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            movieDetailViewModel.movieCastState.collect { movieCastViewState ->
                when (movieCastViewState.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        movieCastViewState.data?.let { movieCastList ->

                            val movieCast = movieCastList

                            val adapter = MovieCastAdapter(movieCast = movieCast)

                            binding.movieCastRecyclerView.adapter = adapter
                            binding.movieCastRecyclerView.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )


                        } ?: run {
                            Log.e("MovieDetailFragment", "Error: Failed to fetch movie cast.")
                        }
                    }

                    Status.ERROR -> {

                    }
                }
            }
        }
    }

    private fun onSaveBtnClicked(movieDetail: MovieDetail) {
        if (movieDetailViewModel.isMovieSaved(movieDetail)) {
            binding.movieDetailSaveBtn.setImageResource(R.drawable.ic_unsaved)
            movieDetailViewModel.unsaveMovie(movieDetail)
        } else {
            binding.movieDetailSaveBtn.setImageResource(R.drawable.ic_saved)
            movieDetailViewModel.saveMovie(movieDetail)
        }
    }

    private fun setSavedImageIcon(movieDetail: MovieDetail) {
        if (movieDetailViewModel.isMovieSaved(movieDetail)) {
            binding.movieDetailSaveBtn.setImageResource(R.drawable.ic_saved)
        } else {
            binding.movieDetailSaveBtn.setImageResource(R.drawable.ic_unsaved)
        }
    }

    private fun setBackdropImage(imagePath: String) {

        val url =
            "${Constants.BASE_URL_IMAGE_PATH}$imagePath"

        val requestOptions = RequestOptions().transform(
            CenterCrop(),

            )

        Glide
            .with(requireContext())
            .load(url)
            .apply(requestOptions)
            .placeholder(R.drawable.ic_film_reel)
            .error(R.drawable.ic_film_reel)
            .into(binding.movieBackdropImage)
    }


    companion object {
        const val BUNDLE_KEY_MOVIE_ID = "movie_id"
    }
}
