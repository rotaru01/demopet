package com.danielrotaru.petdemo.model

data class Token(
    val tokenType: String,
    val expirationTime: String,
    val accessToken: String,
)