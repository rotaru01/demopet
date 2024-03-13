package com.danielrotaru.petdemo.model

sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val code: Int?, val message: String?) : ApiResult<Nothing>()
}