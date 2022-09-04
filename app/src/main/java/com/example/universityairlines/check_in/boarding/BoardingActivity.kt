package com.example.universityairlines.check_in.boarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.universityairlines.R
import com.example.universityairlines.databinding.ActivityCheckInConfirmedBinding
import com.example.universityairlines.databinding.BoardingCardBinding
import com.example.universityairlines.homepage.HomepageActivity
import com.example.universityairlines.model.Reservation
import com.example.universityairlines.ui.getString
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.random.Random

class BoardingActivity : AppCompatActivity() {

    private lateinit var binding: BoardingCardBinding
    private val allowedChars = ('A'..'F')

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardingCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        val codeForCheckin = intent.getStringExtra("code").toString()

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

            reservationList.forEach { reservation ->
                if (reservation.code.uppercase() == codeForCheckin.uppercase()) {

                    with(binding.buyedFlight) {
                        andataTextView.text = binding.buyedFlight.getString(
                            com.example.universityairlines.R.string.booking_details_flight,
                            "Origine",
                            reservation.origin
                        )
                        ritornoTextView.text = binding.buyedFlight.getString(
                            com.example.universityairlines.R.string.booking_details_flight,
                            "Destinazione",
                            reservation.destination
                        )
                        dataTextView.text =
                            binding.buyedFlight.getString(
                                com.example.universityairlines.R.string.booking_details_flight,
                                "Data",
                                reservation.date
                            )
                        oraTextView.text =
                            binding.buyedFlight.getString(
                                com.example.universityairlines.R.string.booking_details_flight,
                                "Ora",
                                reservation.hour
                            )

                    }

                    binding.prenotationCode.text = codeForCheckin.uppercase()

                    if (!reservation.checkin) {
                        reservation.checkin = true
                        reservationList.remove(reservation)
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
            }
        }

        binding.postoAssegnato.text = Random.nextInt(0,22).toString().plus(allowedChars.random())
        binding.bottoneHome.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
        }*/
    }
}