package com.danielrotaru.petdemo.views


sealed class AnimalsUiState {
    object Loading : AnimalsUiState()
    object Loaded : AnimalsUiState()
    data class Error(val error: String) : AnimalsUiState()
}