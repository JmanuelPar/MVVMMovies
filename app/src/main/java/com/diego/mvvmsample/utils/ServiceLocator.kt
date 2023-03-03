package com.diego.mvvmsample.utils

import android.content.Context
import androidx.room.Room
import com.diego.mvvmsample.data.source.local.MoviesLocalDataSource
import com.diego.mvvmsample.data.source.remote.MoviesRemoteDataSource
import com.diego.mvvmsample.data.source.remoteMediator.MoviesRemoteMediatorDataSource
import com.diego.mvvmsample.db.MovieDetailDao
import com.diego.mvvmsample.db.MoviesRoomDatabase
import com.diego.mvvmsample.network.TmdbApi
import com.diego.mvvmsample.repository.DefaultMoviesRepository
import com.diego.mvvmsample.repository.MoviesRepository

object ServiceLocator {

    private var database: MoviesRoomDatabase? = null

    @Volatile
    var moviesRepository: MoviesRepository? = null

    fun provideMoviesRepository(context: Context): MoviesRepository {
        synchronized(this) {
            return moviesRepository ?: createMoviesRepository(context)
        }
    }

    private fun createMoviesRepository(context: Context): MoviesRepository {
        val moviesRoomDatabase = database ?: createMoviesDatabase(context)
        val newRepo = DefaultMoviesRepository(
            moviesLocalDataSource = createMoviesLocalDataSource(moviesRoomDatabase.movieDetailDao()),
            moviesRemoteDataSource = createMoviesRemoteDataSource(),
            moviesRemoteMediatorDataSource = createMoviesRemoteMediatorDataSource(moviesRoomDatabase)
        )
        moviesRepository = newRepo
        return newRepo
    }

    private fun createMoviesLocalDataSource(movieDetailDao: MovieDetailDao) =
        MoviesLocalDataSource(movieDetailDao)

    private fun createMoviesRemoteDataSource(): MoviesRemoteDataSource {
        return MoviesRemoteDataSource(apiService = createApiService())
    }

    private fun createMoviesRemoteMediatorDataSource(moviesRoomDatabase: MoviesRoomDatabase) =
        MoviesRemoteMediatorDataSource(
            apiService = createApiService(),
            moviesRoomDatabase = moviesRoomDatabase
        )

    private fun createApiService() = TmdbApi.retrofitService

    private fun createMoviesDatabase(context: Context): MoviesRoomDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            MoviesRoomDatabase::class.java,
            "movies_database"
        ).build()
        database = result
        return result
    }
}