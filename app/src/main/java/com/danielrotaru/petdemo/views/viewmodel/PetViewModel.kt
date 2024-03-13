package com.danielrotaru.petdemo.views.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielrotaru.petdemo.BuildConfig
import com.danielrotaru.petdemo.model.AnimalObject
import com.danielrotaru.petdemo.model.Animals
import com.danielrotaru.petdemo.model.Pagination
import com.danielrotaru.petdemo.repository.PetRepository
import com.danielrotaru.petdemo.retrofit.TokenManager
import com.danielrotaru.petdemo.toLocalDateTime
import com.danielrotaru.petdemo.views.AnimalsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<AnimalsUiState> =
        MutableStateFlow(AnimalsUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _animals = MutableStateFlow(Animals(emptyList(), Pagination(20, 20, 1, 20)))
    val totalAnimals: StateFlow<Animals> = _animals
    private val _currentAnimal = MutableStateFlow(AnimalObject(emptyList()))
    val currentAnimal: StateFlow<AnimalObject> = _currentAnimal

    val tokenManager = TokenManager

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAnimals(page: Int = 1) {
        _uiState.update {
            AnimalsUiState.Loading
        }
        viewModelScope.launch {
            try {
                val token = tokenManager.token.value
                val isExpired = tokenManager.token.value?.expirationTime?.toLocalDateTime()
                    ?.isAfter(LocalDateTime.now()) ?: false

                if (token == null || isExpired) {
                    val response = petRepository.getToken(
                        BuildConfig.API_KEY,
                        BuildConfig.API_SECRET
                    )
                    if (response.accessToken.isNotEmpty()) {
                        tokenManager.updateTokenValue(response)
                    }
                }

                val response2 = petRepository.getAnimals(page)
                _uiState.update {
                    addAnimals(response2)
                    AnimalsUiState.Loaded
                }

            } catch (e: Throwable) {
                handleErrors(e)
            }
        }
    }

    private fun addAnimals(newAnimals: Animals) {
        val currentAnimals = _animals.value
        val combinedAnimals =
            Animals(currentAnimals.animals + newAnimals.animals, _animals.value.pagination)
        _animals.value = combinedAnimals
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAnimal(id: Int) {
        _uiState.update {
            AnimalsUiState.Loading
        }
        viewModelScope.launch {
            try {
                val token = tokenManager.token.value
                val isExpired = tokenManager.token.value?.expirationTime?.toLocalDateTime()
                    ?.isAfter(LocalDateTime.now()) ?: false

                if (token == null || isExpired) {
                    val response = petRepository.getToken(
                        BuildConfig.API_KEY,
                        BuildConfig.API_SECRET
                    )
                    if (response.accessToken.isNotEmpty()) {
                        tokenManager.updateTokenValue(response)
                    }
                }

                val response2 = petRepository.getAnimal(id)

                _currentAnimal.value = response2
                _uiState.update {
                    AnimalsUiState.Loaded
                }
            } catch (e: Throwable) {
                handleErrors(e)
            }
        }
    }

    private fun handleErrors(e: Throwable) {

        when (e.message) {
            "HTTP 401 " -> _uiState.update { AnimalsUiState.Error("Bad Credentials") }
            "HTTP 403 " -> _uiState.update { AnimalsUiState.Error("Access denied due to insufficient access.") }
            "HTTP 404 " -> _uiState.update { AnimalsUiState.Error("The requested resource was not found.") }
            "HTTP 500 " -> _uiState.update { AnimalsUiState.Error("The request ran into an unexpected error. If the problem persists, please contact support.") }
            "ERR-00001 " -> _uiState.update { AnimalsUiState.Error("The request has missing parameters.") }
            "ERR-00002 " -> _uiState.update { AnimalsUiState.Error("Your request contains invalid parameters") }

            else -> _uiState.update { AnimalsUiState.Error("Technical error") }
        }
    }
}