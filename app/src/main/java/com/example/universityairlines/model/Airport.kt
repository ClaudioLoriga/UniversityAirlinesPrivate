package com.example.universityairlines.model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Airport(
    @JsonProperty("code")
    val code: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("citycode")
    val citycode: String,
    @JsonProperty("city")
    val city: String,
    @JsonProperty("countrycode")
    val countrycode: String,
    @JsonProperty("country")
    val country: String,
    @JsonProperty("continent")
    val continent: String
) : Parcelable