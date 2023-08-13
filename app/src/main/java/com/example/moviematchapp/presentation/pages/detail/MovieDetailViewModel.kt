package com.example.moviematchapp.presentation.pages.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviematchapp.data.local.models.SavedMovieEntitiy
import com.example.moviematchapp.data.local.repositories.MovieLocalRepository
import com.example.moviematchapp.data.remote.repositories.MovieRemoteRepository
import com.example.moviematchapp.domain.models.Genre
import com.example.moviematchapp.domain.models.MovieCast
import com.example.moviematchapp.domain.models.MovieDetail
import com.example.moviematchapp.utils.Status
import com.example.moviematchapp.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRemoteRepository: MovieRemoteRepository,
    private val movieLocalRepository: MovieLocalRepository
) :
    ViewModel() {

    private var genres = mutableListOf<Genre>()

    private var movieDetail = MovieDetail(
        id = 0,
        title = "",
        voteAverage = 0.0,
        originalLanguage = "",
        backdropPath = "",
        overview = "",
        genres = genres,
        posterPath = ""

    )

    private var movieCast = mutableListOf<MovieCast>()

    val movieCastState = MutableStateFlow(
        ViewState(
            Status.LOADING,
            movieCast,
            ""
        )
    )

    val movieDetailState = MutableStateFlow(
        ViewState(
            Status.LOADING,
            movieDetail, ""
        )
    )

    fun getTrendingMovieAllDetailBy(id: Int) {

        fetchMovieDetail(id)

        fetchMovieCast(id)
    }

    private fun fetchMovieDetail(id: Int) {
        movieDetailState.value = ViewState.loading()

        viewModelScope.launch {
            movieRemoteRepository.fetchTrendingMovieDetailData(id = id)
                .catch {
                    Log.e("MovieDetailViewModel", "error : ${it.message.toString()}")
                    movieDetailState.value = ViewState.error(msg = it.message.toString())
                }
                .collect { trendingMovieDetailViewState ->
                    trendingMovieDetailViewState.data?.let { movieDetailResponse ->


                        genres = movieDetailResponse.genreResponses!!.map { genreResponse ->

                            Genre(
                                id = genreResponse.id!!,
                                name = genreResponse.name!!,
                            )


                        }.toMutableList()




                        Log.i("MovieDetailViewModel", "moviDetailResponse : $movieDetailResponse")
                        movieDetail = MovieDetail(
                            title = movieDetailResponse.title!!,
                            id = movieDetailResponse.id!!,
                            voteAverage = movieDetailResponse.voteAverage!!,
                            originalLanguage = movieDetailResponse.originalLanguage!!,
                            backdropPath = movieDetailResponse.backdropPath!!,
                            overview = movieDetailResponse.overview!!,
                            genres = genres,
                            posterPath = movieDetailResponse.posterPath!!
                        )

                        movieDetailState.value = ViewState.success(data = movieDetail)

                        Log.i("MovieDetailViewModel", "Received movie detail.")
                    } ?: run {
                        Log.e("MovieDetailViewModel", "Error: Failed to fetch movie detail.")
                    }
                }
        }
    }

    private fun fetchMovieCast(id: Int) {
        movieCastState.value = ViewState.loading()

        viewModelScope.launch {
            movieRemoteRepository.fetchMovieCast(id = id)
                .catch {
                    movieCastState.value = ViewState.error(msg = it.message.toString())
                }
                .collect { movieCastViewState ->
                    movieCastViewState.data?.let { castResponse ->
                        movieCast = castResponse.cast!!.map { singleCastResponse ->
                            MovieCast(
                                id = singleCastResponse.id!!,
                                name = singleCastResponse.name!!,
                                gender = singleCastResponse.gender!!,
                                profilePath = singleCastResponse.profilePath,
                                character = singleCastResponse.character!!,
                            )
                        }.toMutableList()

                        movieCastState.value = ViewState.success(data = movieCast)

                        Log.i("MovieDetailViewModel", "Received movie cast.")
                    } ?: run {
                        Log.e("MovieDetailViewModel", "Error: Failed to fetch movie cast.")
                    }
                }
        }
    }

    fun unsaveMovie(movieDetail: MovieDetail) {
        val savedMovieEntity = SavedMovieEntitiy(
            id = movieDetail.id,
            title = movieDetail.title,
            voteAverage = movieDetail.voteAverage,
            originalLanguage = movieDetail.originalLanguage,
            overview = movieDetail.overview
        )

        movieLocalRepository.deleteMovie(savedMovieEntity)
    }

    fun saveMovie(movieDetail: MovieDetail) {


        val savedMovieEntity = SavedMovieEntitiy(
            id = movieDetail.id,
            title = movieDetail.title,
            voteAverage = movieDetail.voteAverage,
            originalLanguage = movieDetail.originalLanguage,
            overview = movieDetail.overview,
            backdropPath = movieDetail.backdropPath,
            posterPath = movieDetail.posterPath,
        )

        movieLocalRepository.saveMovie(savedMovieEntity)

    }


    fun isMovieSaved(movieDetail: MovieDetail): Boolean {

        return movieLocalRepository.getMovie(movieDetail.id) != null
    }
}
