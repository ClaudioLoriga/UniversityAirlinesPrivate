package com.example.universityairlines.model

import com.fasterxml.jackson.annotation.JsonProperty

data class RegistrationResponse(
    @JsonProperty("mail_availability")
    val mail_availability: String,
    @JsonProperty("message")
    val message: String,
    @JsonProperty("status")
    val status: String
)