package com.example.moviematchapp.data.remote.models.response.upcoming_movies

import com.google.gson.annotations.SerializedName

data class UpcomingMovieListResponse(

    @SerializedName("dates")
    val dates: DatesResponse? = null,

    @SerializedName("results")
    val results: List<UpcomingMovieResponse>? = null,
)