package com.example.moviematchapp.domain.models

data class MovieDetail(

    val id: Int,
    val title: String,
    val voteAverage: Double,
    val originalLanguage: String,
    val overview: String,
    val backdropPath: String,
    val genres : List<Genre>,
    val posterPath: String,
)