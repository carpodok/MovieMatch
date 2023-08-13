package com.example.moviematchapp.data.remote.models.response.now_playing_movies

import com.google.gson.annotations.SerializedName

data class DatesResponse(
    @SerializedName("maximum")
    val maximum: String?,

    @SerializedName("minimum")
    val minimum: String?
)