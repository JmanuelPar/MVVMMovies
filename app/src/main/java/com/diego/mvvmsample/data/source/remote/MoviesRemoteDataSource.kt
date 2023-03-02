package com.diego.mvvmsample.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.diego.mvvmsample.BuildConfig
import com.diego.mvvmsample.data.source.MoviesDataSource
import com.diego.mvvmsample.network.ApiResult
import com.diego.mvvmsample.network.TmdbApiService
import com.diego.mvvmsample.utils.Constants.LANGUAGE
import com.diego.mvvmsample.utils.Constants.NETWORK_TMDB_PAGE_SIZE
import com.diego.mvvmsample.utils.asDomainModel
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MoviesRemoteDataSource internal constructor(
    private val apiService: TmdbApiService
) : MoviesDataSource {

    override fun getMovies() =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_TMDB_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TmdbPagingSourceMovies(apiService)
            }
        ).flow

    override suspend fun getMovieById(movieId: Int) =
        try {
            val movie = apiService.getMovieById(
                apiKey = BuildConfig.API_KEY,
                movieId = movieId,
                language = LANGUAGE
            ).asDomainModel()
            ApiResult.Success(data = movie)
        } catch (exception: IOException) {
            Timber.e("IOException on getMovieById : ${exception.localizedMessage}")
            ApiResult.Error(exception = exception)
        } catch (exception: HttpException) {
            Timber.e("HttpException on getMovieById : ${exception.localizedMessage}")
            ApiResult.Error(exception = exception)
        }
}