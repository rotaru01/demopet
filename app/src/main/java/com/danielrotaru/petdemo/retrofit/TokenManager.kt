package com.danielrotaru.petdemo.retrofit

import com.danielrotaru.petdemo.model.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object TokenManager {
    private val _token = MutableStateFlow<Token?>(null)
    val token: StateFlow<Token?> = _token

    fun updateTokenValue(token: Token){
        _token.value = token
    }
}