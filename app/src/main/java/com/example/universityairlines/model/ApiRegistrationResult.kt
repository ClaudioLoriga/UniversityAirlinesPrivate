package com.example.universityairlines.model

sealed class ApiRegistrationResult<out T : Any> {
    data class Success<out T : Any>(val value: T) : ApiRegistrationResult<T>()
    data class Failure<out T : Any>(val value: T) : ApiRegistrationResult<Nothing>()
}