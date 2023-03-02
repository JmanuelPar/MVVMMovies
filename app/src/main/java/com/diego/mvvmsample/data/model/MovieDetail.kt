package com.diego.mvvmsample.data.model

data class MovieDetail(
    val title: String,
    val releaseDate: String,
    val genres: String,
    val tagLine: String,
    val overview: String,
    val rating: Double,
    val backdropPath: String,
) {
    override fun toString(): String {
        return "\n MovieDetail ----> " +
                "\n title : $title" +
                "\n releaseDate : $releaseDate" +
                "\n genres : $genres" +
                "\n tagLine : $tagLine" +
                "\n overview : $overview" +
                "\n rating : $rating" +
                "\n backdropPath : $backdropPath"
    }
}