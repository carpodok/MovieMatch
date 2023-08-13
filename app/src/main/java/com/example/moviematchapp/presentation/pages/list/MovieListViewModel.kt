package com.example.moviematchapp.presentation.pages.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.moviematchapp.data.local.models.SavedMovieEntitiy
import com.example.moviematchapp.data.local.repositories.MovieLocalRepository
import com.example.moviematchapp.data.remote.repositories.MovieRemoteRepository
import com.example.moviematchapp.domain.models.NowPlayingMovie
import com.example.moviematchapp.domain.models.TrendingMovie
import com.example.moviematchapp.domain.models.UpcomingMovie
import com.example.moviematchapp.utils.PagerMovieListState
import com.example.moviematchapp.utils.Status
import com.example.moviematchapp.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRemoteRepository: MovieRemoteRepository,
    private val movieLocalRepository: MovieLocalRepository
) :
    ViewModel() {

    private var searchedTrendingMovieList = mutableListOf<TrendingMovie>()
    private var savedMovieList = mutableListOf<TrendingMovie>()
    private var upcomingMovieList = mutableListOf<UpcomingMovie>()
    private var nowPlayingMovieList = mutableListOf<NowPlayingMovie>()


    private val _Pager_movieListState =
        MutableStateFlow<PagerMovieListState>(PagerMovieListState.Loading)
    val pagerMovieListState: StateFlow<PagerMovieListState> = _Pager_movieListState


    val savedMovieListState = MutableStateFlow(
        ViewState(
            Status.LOADING,
            savedMovieList,
            "",

            )
    )

    val nowPlayingMovieListState = MutableStateFlow(
        ViewState(
            Status.LOADING,
            nowPlayingMovieList,
            "",
        )
    )

    val searchedTrendingMovieListState = MutableStateFlow(
        ViewState(
            Status.LOADING,
            searchedTrendingMovieList,
            ""
        )
    )

    val upcomingMoviesState = MutableStateFlow(
        ViewState(
            Status.LOADING,
            upcomingMovieList,
            "",
        )
    )

    init {
        getAllUpcomingMovies()
        getAllNowPlayingMovies()
        loadTrendingMovieList()
    }


    fun loadTrendingMovieList() {
        _Pager_movieListState.value = PagerMovieListState.Loading

        viewModelScope.launch {
            val pagerFlow = movieRemoteRepository.getPagerMovies()
                .catch {
                    Log.i("error", "loadTrendingMovieList Error")
                    _Pager_movieListState.value = PagerMovieListState.Error(it)

                }
                .map { pagingData ->

                    pagingData.map { trendingMovieResponse ->


                        Log.i(
                            "trendingMovieResponse",
                            "trendingMovieResponse : ${trendingMovieResponse.backdropPath}"
                        )

                        val isSaved = isTrendingMovieSaved(trendingMovieResponse.id!!)
                        TrendingMovie(
                            id = trendingMovieResponse.id,
                            title = trendingMovieResponse.title!!,
                            voteAverage = trendingMovieResponse.voteAverage!!,
                            originalLanguage = trendingMovieResponse.originalLanguage!!,
                            posterPath = trendingMovieResponse.posterPath,
                            backdropPath = trendingMovieResponse.backdropPath,
                            overview = trendingMovieResponse.overview!!,
                            isSaved = isSaved
                        )
                    }


                }
                .cachedIn(viewModelScope)


            _Pager_movieListState.value = PagerMovieListState.Success(pagerFlow)


        }
    }

    suspend fun searchMovie(queryText: String) {

        searchedTrendingMovieListState.value = ViewState.loading()

        viewModelScope.launch {
            movieRemoteRepository.searchMovie(queryText)
                .catch {
                    searchedTrendingMovieListState.value =
                        ViewState.error(it.message.toString())
                }
                .collect { movieListViewState ->

                    movieListViewState.data?.let { trendingMovieListResponse ->
                        searchedTrendingMovieList =
                            trendingMovieListResponse.results!!.map { trendingMovieResponse ->


                                val isSaved = isTrendingMovieSaved(trendingMovieResponse.id!!)

                                TrendingMovie(
                                    title = trendingMovieResponse.title!!,
                                    id = trendingMovieResponse.id,
                                    posterPath = trendingMovieResponse.posterPath,
                                    voteAverage = trendingMovieResponse.voteAverage!!,
                                    originalLanguage = trendingMovieResponse.originalLanguage!!,
                                    backdropPath = trendingMovieResponse.backdropPath,
                                    overview = trendingMovieResponse.overview!!,
                                    isSaved = isSaved,

                                    )
                            }.toMutableList()

                        searchedTrendingMovieListState.value =
                            ViewState.success(searchedTrendingMovieList)


                        Log.i("MovieListFragment", "Received searched list.")

                    }
                        ?: run {

                            Log.e("MovieListFragment", "Error: Failed to fetch searched list.")
                        }

                }
        }

    }

    fun getAllUpcomingMovies() {
        upcomingMoviesState.value = ViewState.loading()

        viewModelScope.launch {
            movieRemoteRepository.fetchUpcomingMovies()
                .catch {
                    upcomingMoviesState.value =
                        ViewState.error(it.message.toString())
                }
                .collect { upcomingMoviesViewState ->
                    upcomingMoviesViewState.data?.let { upcomingMovielistResponse ->

                        upcomingMovieList =
                            upcomingMovielistResponse.results!!.map { upcomingMovieResponse ->

                                UpcomingMovie(
                                    id = upcomingMovieResponse.id!!,
                                    poster = upcomingMovieResponse.poster_path!!,
                                )


                            }.toMutableList()

                        upcomingMoviesState.value = ViewState.success(data = upcomingMovieList)

                    }

                }
        }
    }

    fun getAllNowPlayingMovies() {

        nowPlayingMovieListState.value = ViewState.loading()

        viewModelScope.launch {
            movieRemoteRepository.fetchNowPlayingMovies()
                .catch {
                    nowPlayingMovieListState.value =
                        ViewState.error(it.message.toString())
                }
                .collect { nowPlayingMovieListViewState ->

                    nowPlayingMovieListViewState.data?.let { nowPlayingMovieListResponse ->

                        nowPlayingMovieList =
                            nowPlayingMovieListResponse.results!!.map { nowPlaingMovieResponse ->

                                NowPlayingMovie(
                                    id = nowPlaingMovieResponse.id!!,
                                    poster = nowPlaingMovieResponse.poster_path!!,
                                )

                            }.toMutableList()

                        nowPlayingMovieListState.value =
                            ViewState.success(data = nowPlayingMovieList)
                    }
                }
        }
    }

    private fun isTrendingMovieSaved(trendingMovieId: Int): Boolean {

        Log.i(
            "TrendingMovieListViewModel",
            "${movieLocalRepository.getMovie(trendingMovieId) == null}"
        )
        return movieLocalRepository.getMovie(trendingMovieId) != null
    }

    fun unsaveMovie(trendingMovie: TrendingMovie) {
        val savedMovieEntity = SavedMovieEntitiy(
            id = trendingMovie.id,
            title = trendingMovie.title,
            voteAverage = trendingMovie.voteAverage,
            originalLanguage = trendingMovie.originalLanguage,
            overview = trendingMovie.overview
        )

        movieLocalRepository.deleteMovie(savedMovieEntity)
    }

    fun saveMovie(trendingMovie: TrendingMovie) {


        val savedMovieEntity = SavedMovieEntitiy(
            id = trendingMovie.id,
            title = trendingMovie.title,
            voteAverage = trendingMovie.voteAverage,
            originalLanguage = trendingMovie.originalLanguage,
            overview = trendingMovie.overview,
            backdropPath = trendingMovie.backdropPath!!,
            posterPath = trendingMovie.posterPath!!,
        )

        movieLocalRepository.saveMovie(savedMovieEntity)

    }

    fun getAllSavedMovies() {


        val savedList = movieLocalRepository.getAllSavedMovies().map { savedMovieEntity ->

            TrendingMovie(
                id = savedMovieEntity.id,
                title = savedMovieEntity.title,
                voteAverage = savedMovieEntity.voteAverage,
                originalLanguage = savedMovieEntity.originalLanguage,
                overview = savedMovieEntity.overview,
                backdropPath = savedMovieEntity.backdropPath,
                posterPath = savedMovieEntity.posterPath,
                isSaved = true,
            )

        }.toMutableList()

        savedMovieListState.value = ViewState.success(savedList)

    }
}