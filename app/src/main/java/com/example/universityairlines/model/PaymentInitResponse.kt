package com.example.universityairlines.model


import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PaymentInitResponse(
    @JsonProperty("origin_city")
    val originCity: String,
    @JsonProperty("origin_iata")
    val originIata: String,
    @JsonProperty("destination_city")
    val destinationCity: String,
    @JsonProperty("destination_iata")
    val destinationIata: String,
    @JsonProperty("departure_date")
    val departureDate: String,
    @JsonProperty("total_to_pay")
    val totalToPay: Int,
    @JsonProperty("passengers_number")
    val passengersNumber: String
) : Parcelable