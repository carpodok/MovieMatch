package com.example.moviematchapp.data.local.repositories

import com.example.moviematchapp.data.local.datasources.dao.MovieDAO
import com.example.moviematchapp.data.local.models.SavedMovieEntitiy
import javax.inject.Inject

class MovieLocalRepository @Inject constructor(private val dao: MovieDAO) {

    fun saveMovie(movie: SavedMovieEntitiy) = dao.insert(movie)
    fun deleteMovie(movie: SavedMovieEntitiy) = dao.delete(movie)
    fun getMovie(id: Int): SavedMovieEntitiy? = dao.getMovieById(id)
    fun getAllSavedMovies() = dao.getAllSavedMovies()


}