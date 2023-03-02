package com.diego.mvvmsample.ui.movieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diego.mvvmsample.data.model.MovieDetail
import com.diego.mvvmsample.network.ApiResult
import com.diego.mvvmsample.repository.MoviesRepository
import com.diego.mvvmsample.utils.UIText
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MovieDetailViewModel(
    private val repository: MoviesRepository,
    private val movieId: Int = 0,
) : ViewModel() {

    private val _movieDetail = MutableLiveData<MovieDetail>()
    val movieDetail: LiveData<MovieDetail>
        get() = _movieDetail

    private val _showLayoutResult = MutableLiveData<Boolean>()
    val showLayoutResult: LiveData<Boolean>
        get() = _showLayoutResult

    private val _showLayoutError = MutableLiveData<Boolean>()
    val showLayoutError: LiveData<Boolean>
        get() = _showLayoutError

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    private val _showErrorMessage = MutableLiveData<UIText>()
    val showErrorMessage: LiveData<UIText>
        get() = _showErrorMessage

    init {
        getMovieDetail()
    }

    private fun getMovieDetail() {
        showProgress()
        viewModelScope.launch {
            when (val response = repository.getMovieById(movieId = movieId)) {
                is ApiResult.Success -> showSuccess(response.data)
                is ApiResult.Error -> {
                    val uiText = when (val exception = response.exception) {
                        is IOException -> UIText.NoConnect
                        is HttpException -> exception.localizedMessage?.let {
                            UIText.MessageException(it)
                        } ?: UIText.UnknownError
                        else -> UIText.UnknownError
                    }

                    showError(uiText)
                }
            }
        }
    }

    private fun showProgress() {
        showLayoutResult(false)
        showLayoutError(false)
        showProgressBar(true)
    }

    private fun showSuccess(movieDetail: MovieDetail) {
        showProgressBar(false)
        showLayoutResult(true)
        showMovieDetail(movieDetail)
    }

    private fun showError(uiText: UIText) {
        showProgressBar(false)
        showErrorMessage(uiText)
        showLayoutError(true)
    }

    private fun showMovieDetail(movieDetail: MovieDetail) {
        _movieDetail.value = movieDetail
    }

    private fun showLayoutResult(visibility: Boolean) {
        _showLayoutResult.value = visibility
    }

    private fun showLayoutError(visibility: Boolean) {
        _showLayoutError.value = visibility
    }

    private fun showProgressBar(visibility: Boolean) {
        _showProgressBar.value = visibility
    }

    private fun showErrorMessage(uiText: UIText) {
        _showErrorMessage.value = uiText
    }

    fun buttonRetryClicked() {
        getMovieDetail()
    }
}