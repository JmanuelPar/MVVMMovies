package com.diego.mvvmsample.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.diego.mvvmsample.data.model.MovieDetail

@Database(
    entities = [MovieDatabase::class, RemoteKeys::class, MovieDetail::class],
    version = 1,
    exportSchema = false
)
abstract class MoviesRoomDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun movieDetailDao(): MovieDetailDao
}