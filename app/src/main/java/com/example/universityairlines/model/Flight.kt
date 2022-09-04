package com.example.universityairlines.model

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize

@Parcelize
data class Flight(
    @JsonProperty("origin")
    val origin: String,
    @JsonProperty("destination")
    val destination: String,
    @JsonProperty("origin_iata")
    val originIata: String,
    @JsonProperty("destination_iata")
    val destinationIata: String,
    @JsonProperty("departure_date")
    val departureDate: String,
    @JsonProperty("return_date")
    val returnDate: String,
    @JsonProperty("price")
    val price: Int,
    @JsonProperty("currency")
    val currency: String
) : Parcelable