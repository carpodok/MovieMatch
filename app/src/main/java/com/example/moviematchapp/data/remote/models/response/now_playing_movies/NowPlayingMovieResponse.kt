package com.example.moviematchapp.data.remote.models.response.now_playing_movies

import com.google.gson.annotations.SerializedName

data class NowPlayingMovieResponse(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("poster_path")
    val poster_path: String? = null,
)
