package com.danielrotaru.petdemo

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(pattern: String = "yyyy-MM-dd'T'HH:mm:ss"): LocalDateTime? {
    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        LocalDateTime.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        null
    }
}
