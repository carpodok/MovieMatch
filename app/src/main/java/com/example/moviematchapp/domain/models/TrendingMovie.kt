package com.example.moviematchapp.domain.models

data class TrendingMovie(

    val id: Int,
    val title: String,
    val voteAverage: Double,
    val originalLanguage: String,
    val posterPath: String?,
    val backdropPath : String?,
    val overview : String,
    var isSaved : Boolean,
)
