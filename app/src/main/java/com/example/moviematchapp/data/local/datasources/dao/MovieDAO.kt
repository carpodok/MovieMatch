package com.example.moviematchapp.data.local.datasources.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviematchapp.data.local.models.SavedMovieEntitiy

@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: SavedMovieEntitiy)

    @Delete
    fun delete(movie: SavedMovieEntitiy)

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: Int): SavedMovieEntitiy?

    @Query("SELECT*FROM movies")
    fun getAllSavedMovies(): List<SavedMovieEntitiy>
}