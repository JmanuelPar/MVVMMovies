package com.diego.mvvmsample.utils

import com.diego.mvvmsample.data.model.*

// Convert NetworkMovieDetail to MovieDetail
fun NetworkMovieDetail.asDomainModel(): MovieDetail {
    val genres = this.genres?.let { list ->
        list.joinToString(separator = " - ") { genre -> "${genre.name}" }
    } ?: ""

    return MovieDetail(
        title = this.title ?: "",
        releaseDate = this.releaseDate ?: "",
        genres = genres,
        tagLine = this.tagline ?: "",
        overview = this.overview ?: "",
        rating = this.voteAverage ?: -1.0,
        backdropPath = this.backdropPath ?: ""
    )
}

// Convert NetworkMovies to List Movie
fun NetworkMovies.asDomainModel() =
    results?.map { result ->
        processingItems(result)
    } ?: emptyList()

private fun processingItems(result: Result) =
    Movie(
        idMovie = result.id!!,
        title = result.title ?: "",
        posterPath = result.posterPath ?: "",
        releaseDate = result.releaseDate ?: "",
        rating = result.voteAverage ?: -1.0
    )

