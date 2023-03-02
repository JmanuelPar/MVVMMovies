package com.diego.mvvmsample.utils

import com.diego.mvvmsample.data.source.remote.MoviesRemoteDataSource
import com.diego.mvvmsample.network.TmdbApi
import com.diego.mvvmsample.repository.DefaultMoviesRepository
import com.diego.mvvmsample.repository.MoviesRepository

object ServiceLocator {

    @Volatile
    var moviesRepository: MoviesRepository? = null

    fun provideMoviesRepository(): MoviesRepository {
        synchronized(this) {
            return moviesRepository ?: createMoviesRepository()
        }
    }

    private fun createMoviesRepository(): MoviesRepository {
        val newRepo = DefaultMoviesRepository(
            moviesRemoteDataSource = createMoviesRemoteDataSource()
        )
        moviesRepository = newRepo
        return newRepo
    }

    private fun createMoviesRemoteDataSource() = MoviesRemoteDataSource(provideApiService())

    private fun provideApiService() = TmdbApi.retrofitService
}