package ru.eababurin.pictureoftheday.viewmodel

import ru.eababurin.pictureoftheday.model.PictureOfTheDayResponseData

sealed class AppState {
    data class Success(val pictureOfTheDayResponseData: PictureOfTheDayResponseData) : AppState()
    data class Error(val error: String) : AppState()
    object Loading : AppState()
}
