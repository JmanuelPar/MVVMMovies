package com.diego.mvvmsample

import android.app.Application
import com.diego.mvvmsample.repository.MoviesRepository
import com.diego.mvvmsample.utils.ServiceLocator
import timber.log.Timber

class MyApplication : Application() {

    val moviesRepository: MoviesRepository
        get() = ServiceLocator.provideMoviesRepository()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}