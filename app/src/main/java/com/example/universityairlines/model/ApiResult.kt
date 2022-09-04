package com.example.universityairlines.model

sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val value: T) : ApiResult<T>()
    data class Failure(val errorResponse: ErrorResponse? = null) : ApiResult<Nothing>()
}