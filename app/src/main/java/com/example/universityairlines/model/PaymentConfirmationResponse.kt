package com.example.universityairlines.model


import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PaymentConfirmationResponse(
    @JsonProperty("status")
    val status: String,
    @JsonProperty("total_paid")
    val totalPaid: String,
    @JsonProperty("currency")
    val currency: String,
    @JsonProperty("pnr")
    val pnr: String,
    @JsonProperty("origin_city")
    val originCity: String,
    @JsonProperty("origin_iata")
    val originIata: String,
    @JsonProperty("destination_city")
    val destinationCity: String,
    @JsonProperty("destination_iata")
    val destinationIata: String,
    @JsonProperty("departure_date")
    val departureDate: String
) : Parcelable