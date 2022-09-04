package com.example.universityairlines.model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class GetAirportResponse(
    @JsonProperty("airports")
    val airports: List<Airport>
) : Parcelable