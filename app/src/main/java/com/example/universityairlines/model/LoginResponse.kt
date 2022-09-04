package com.example.universityairlines.model

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginResponse(
    @JsonProperty("first_name")
    val firstName: String,
    @JsonProperty("last_name")
    val lastName: String,
    @JsonProperty("mail")
    val mail: String,
    @JsonProperty("status")
    val status: String
)