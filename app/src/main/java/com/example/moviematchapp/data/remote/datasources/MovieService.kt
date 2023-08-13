package com.example.moviematchapp.data.remote.datasources

import com.example.moviematchapp.data.remote.models.response.MovieDetailResponse
import com.example.moviematchapp.data.remote.models.response.TrendingMovieListResponse
import com.example.moviematchapp.data.remote.models.response.cast.CastsResponse
import com.example.moviematchapp.data.remote.models.response.now_playing_movies.NowPlayingMovieListResponse
import com.example.moviematchapp.data.remote.models.response.upcoming_movies.UpcomingMovieListResponse
import com.example.moviematchapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {


    @GET(Constants.END_POINT_TRENDING_MOVIES)
    suspend fun fetchAllTrendingnMovie(@Query("page") page: Int): TrendingMovieListResponse

    @GET("${Constants.END_POINT_MOVIE_DETAIL}{movie_id}")
    suspend fun fetchMovieDetailById(@Path("movie_id") movieId: Int): MovieDetailResponse

    @GET(Constants.END_POINT_MOVIE_CAST)
    suspend fun fetchMovieCast(@Path("movie_id") movieId: Int): CastsResponse

    @GET(Constants.END_POINT_UPCOMING_MOVIES)
    suspend fun fetchUpcomingMovies(): UpcomingMovieListResponse

    @GET(Constants.END_POINT_NOW_PLAYING_MOVIES)
    suspend fun fetchNowPlayingMovies(): NowPlayingMovieListResponse

    @GET(Constants.END_POINT_SEARCH_MOVIE)
    suspend fun searchMovie(@Query("query") queryText: String): TrendingMovieListResponse


}