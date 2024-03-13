package com.danielrotaru.petdemo.model

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

data class TokenRemote(
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("expires_in")
    val expiresIn: Long?,
    @SerializedName("access_token")
    val accessToken: String?,
)

@Suppress("NewApi")
fun TokenRemote?.toDomain() = Token(
    tokenType = this?.tokenType.orEmpty(),
    expirationTime = this?.expiresIn?.let { calculateExpirationTime(it) } ?: LocalDateTime.now().toString(),
    accessToken = this?.accessToken.orEmpty(),
)

@Suppress("NewApi")
private fun calculateExpirationTime(expiresIn: Long): String {
    val currentTimestamp = Instant.now().epochSecond
    val expirationTimestamp = currentTimestamp + expiresIn
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(expirationTimestamp), ZoneOffset.UTC).toString()
}