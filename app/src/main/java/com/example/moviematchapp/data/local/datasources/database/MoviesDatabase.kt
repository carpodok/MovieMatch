package com.example.moviematchapp.data.local.datasources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviematchapp.data.local.datasources.dao.MovieDAO
import com.example.moviematchapp.data.local.models.SavedMovieEntitiy

@Database(entities = [SavedMovieEntitiy::class], version = 1)
abstract class MoviesDatabase : RoomDatabase(){
    abstract  fun noteDoa(): MovieDAO
}