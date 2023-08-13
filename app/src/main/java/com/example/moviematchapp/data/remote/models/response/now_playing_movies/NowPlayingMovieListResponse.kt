package com.example.moviematchapp.data.remote.models.response.now_playing_movies

import com.google.gson.annotations.SerializedName

data class NowPlayingMovieListResponse(
    @SerializedName("dates")
    val dates: DatesResponse? = null,

    @SerializedName("results")
    val results: List<NowPlayingMovieResponse>? = null,
)