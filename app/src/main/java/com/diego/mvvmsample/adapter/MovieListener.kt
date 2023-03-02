package com.diego.mvvmsample.adapter

import android.view.View
import com.diego.mvvmsample.data.model.Movie

interface MovieListener {
    fun onMovieClicked(view: View, movie: Movie)
}