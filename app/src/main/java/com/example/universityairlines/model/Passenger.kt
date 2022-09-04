package com.example.universityairlines.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Passenger (
    val nome : String = "",
    val cognome : String = ""
) : Parcelable