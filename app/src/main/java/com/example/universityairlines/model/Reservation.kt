package com.example.universityairlines.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.ArrayList

@Parcelize
data class Reservation(
   val code: String,
   val origin: String,
   val destination: String,
   val date: String,
   val hour: String,
   var checkin: Boolean,
   var totalPrice: String,
   var passengerNumber: Int,
   var passengerDetails: ArrayList<Passenger>?
): Parcelable
