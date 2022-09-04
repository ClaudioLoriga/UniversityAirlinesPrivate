package com.example.universityairlines.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
    @JsonProperty("status")
    val status: String,
    @JsonProperty("message")
    val message: String
)