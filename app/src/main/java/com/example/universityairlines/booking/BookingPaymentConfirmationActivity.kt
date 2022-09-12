package com.example.universityairlines.booking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.universityairlines.R
import com.example.universityairlines.databinding.ActivityBookingPaymentConfirmationBinding
import com.example.universityairlines.homepage.HomepageActivity
import com.example.universityairlines.model.Flight
import com.example.universityairlines.model.Passenger
import com.example.universityairlines.model.Reservation
import com.example.universityairlines.ui.getString
import com.example.universityairlines.ui.randomAlphanumeric
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class BookingPaymentConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingPaymentConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingPaymentConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedprefs = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE) ?: return
        binding.subPresentationTextView.text =
            resources.getString(R.string.volo_porte_placeholder, sharedprefs.getString(getString(R.string.username_shared_preferences), "utente"))
        val flight = intent.extras?.getParcelable<Flight>("flight")
        val pnr = intent.getStringExtra(EXTRAKEY_PNR).orEmpty()
        val totalToPay = intent.getStringExtra(EXTRAKEY_TTP).orEmpty()
        val passengersDetails = intent.extras?.getParcelableArrayList<Passenger>("passengers")

        if (flight != null) {
            val (data, ora) = flight.departureDate.split(" ")
            binding.prenotationCode.text = pnr

            with(binding.buyedFlight) {
                andataTextView.text = binding.getString(
                    R.string.airport_description,
                    flight.origin,
                    flight.originIata
                )
                ritornoTextView.text = binding.getString(
                    R.string.airport_description,
                    flight.destination,
                    flight.destinationIata
                )
                dataTextView.text =
                    binding.getString(R.string.booking_details_flight, "Data", data)
                oraTextView.text =
                    binding.getString(R.string.booking_details_flight, "Ora", ora)
                binding.totalPaid.text = binding.getString(
                    R.string.placeholder_price,
                    "",
                    totalToPay
                )
            }
        }

        binding.bottoneHome.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
            intent.putExtra("flight", flight)
            intent.putExtra("pnr", binding.prenotationCode.text)
            startActivity(intent)
        }

        val departureDateSplitted = flight?.departureDate?.split(" ")
        val date = departureDateSplitted?.get(0) ?: ""
        val hour = departureDateSplitted?.get(1) ?: ""
        val randomSeat = randomAlphanumeric()
        val reservation = Reservation(
            pnr,
            flight?.origin ?: "",
            flight?.destination ?: "",
            date,
            hour,
            false,
            totalToPay,
            passengersDetails?.size ?: 0,
            passengersDetails,
            randomSeat
        )

        val sharedPref =
            getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
                ?: return
        val defaultValue = ""
        val reservationListString = sharedPref.getString(
            getString(R.string.reservation_list_shared_preferences),
            defaultValue
        )
        val mapper = jacksonObjectMapper()

        if (reservationListString != "") {
            val reservationList = mapper.readValue(
                reservationListString,
                object : TypeReference<MutableList<Reservation>>() {})

            //Aggiunta nella lista
            reservationList.add(reservation)

            //Aggiunta nelle shared preferences
            with(sharedPref.edit()) {
                putString(
                    getString(R.string.reservation_list_shared_preferences),
                    mapper.writeValueAsString(reservationList)
                )
                apply()
            }
        } else {
            val reservationList: MutableList<Reservation> = mutableListOf()
            reservationList.add(reservation)

            with(sharedPref.edit()) {
                putString(
                    getString(R.string.reservation_list_shared_preferences),
                    mapper.writeValueAsString(reservationList)
                )
                apply()
            }
        }
    }

    companion object {
        const val EXTRAKEY_PNR = "pnr"
        const val EXTRAKEY_TTP = "totale_da_pagare"
        const val EXTRAKEY_CARD_NUMBER = "numero_carta"
        const val EXTRAKEY_CARD_EXPIRATION = "scadenza_carta"
        const val EXTRAKEY_CVV = "cvv_carta"
    }

}