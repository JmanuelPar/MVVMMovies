package com.diego.mvvmsample.data.source.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.diego.mvvmsample.data.model.MovieDetail
import com.diego.mvvmsample.data.source.MoviesDataSource
import com.diego.mvvmsample.db.MoviesRoomDatabase
import com.diego.mvvmsample.network.ApiResult
import com.diego.mvvmsample.network.TmdbApiService
import com.diego.mvvmsample.utils.Constants.NETWORK_TMDB_PAGE_SIZE

class MoviesRemoteMediatorDataSource internal constructor(
    private val apiService: TmdbApiService,
    private val moviesRoomDatabase: MoviesRoomDatabase
) : MoviesDataSource {

    override fun getMovies() =

        @OptIn(ExperimentalPagingApi::class)
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_TMDB_PAGE_SIZE,
                enablePlaceholders = true
            ),
            remoteMediator = TmdbRemoteMediator(
                apiService = apiService,
                moviesRoomDatabase = moviesRoomDatabase
            ),
            pagingSourceFactory = { moviesRoomDatabase.moviesDao().getMovies() }
        ).flow

    override suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun insertMovieDetail(movieDetail: MovieDetail) {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieDetailById(movieDetailId: Int): MovieDetail? {
        TODO("Not yet implemented")
    }
}