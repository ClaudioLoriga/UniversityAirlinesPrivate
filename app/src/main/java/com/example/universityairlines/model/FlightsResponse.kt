package com.example.universityairlines.model

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlightsResponse(
    @JsonProperty("airports")
    val flights: List<Flight>,
    @JsonProperty("passengers_number")
    val passengersNumber: String
) : Parcelable