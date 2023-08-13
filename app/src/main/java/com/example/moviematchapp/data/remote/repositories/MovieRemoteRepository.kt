package com.example.moviematchapp.data.remote.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviematchapp.data.remote.datasources.MovieService
import com.example.moviematchapp.data.remote.datasources.paging.MoviePagingSource
import com.example.moviematchapp.data.remote.models.response.MovieDetailResponse
import com.example.moviematchapp.data.remote.models.response.TrendingMovieListResponse
import com.example.moviematchapp.data.remote.models.response.TrendingMovieResponse
import com.example.moviematchapp.data.remote.models.response.cast.CastsResponse
import com.example.moviematchapp.data.remote.models.response.now_playing_movies.NowPlayingMovieListResponse
import com.example.moviematchapp.data.remote.models.response.upcoming_movies.UpcomingMovieListResponse
import com.example.moviematchapp.utils.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieRemoteRepository @Inject constructor(
    private val movieService: MovieService,
) {

    suspend fun fetchTrendingMovieListData(): Flow<ViewState<TrendingMovieListResponse>> {
        return flow {

            val trendingMovieListResponse = movieService.fetchAllTrendingnMovie(1)

            emit(ViewState.success(trendingMovieListResponse))
        }.flowOn(Dispatchers.IO)
    }

    fun getPagerMovies(): Flow<PagingData<TrendingMovieResponse>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { MoviePagingSource(movieService) }
        ).flow.flowOn(Dispatchers.IO)
    }


    suspend fun fetchTrendingMovieDetailData(id: Int): Flow<ViewState<MovieDetailResponse>> {
        return flow {
            val movieDetailResponse = movieService.fetchMovieDetailById(movieId = id)

            Log.i("MovieRepository", "movieDetailResponse : $movieDetailResponse")

            emit(ViewState.success(movieDetailResponse))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchMovieCast(id: Int): Flow<ViewState<CastsResponse>> {
        return flow {
            val castsResponse = movieService.fetchMovieCast(movieId = id)

            emit(ViewState.success(castsResponse))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchUpcomingMovies(): Flow<ViewState<UpcomingMovieListResponse>> {
        return flow {
            val upcomingMoviesResponse = movieService.fetchUpcomingMovies()

            emit(ViewState.success(upcomingMoviesResponse))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchNowPlayingMovies(): Flow<ViewState<NowPlayingMovieListResponse>> {
        return flow {
            val nowPlayingMovieListResponse = movieService.fetchNowPlayingMovies()

            emit(ViewState.success(nowPlayingMovieListResponse))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun searchMovie(queryText : String) : Flow<ViewState<TrendingMovieListResponse>> {

        return flow {
            val searchedTrendingMovieResponse = movieService.searchMovie(queryText = queryText)

            emit(ViewState.success(searchedTrendingMovieResponse))
        }.flowOn(Dispatchers.IO)
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}