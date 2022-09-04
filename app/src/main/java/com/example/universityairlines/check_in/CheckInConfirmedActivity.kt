package com.example.universityairlines.check_in

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.universityairlines.R
import com.example.universityairlines.databinding.ActivityCheckInConfirmedBinding
import com.example.universityairlines.homepage.HomepageActivity
import com.example.universityairlines.model.Reservation
import com.example.universityairlines.ui.getString
import com.example.universityairlines.ui.randomAlphanumeric
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.random.Random

class CheckInConfirmedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckInConfirmedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInConfirmedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val codeForCheckin = intent.getStringExtra("code").toString()
        binding.presentationTextView.text = getString(R.string.check_in_effettuato)
        binding.subPresentationTextView.text = getString(R.string.check_in_advise)

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

        binding.postoAssegnato.text = randomAlphanumeric()
        binding.bottoneHome.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
        }
    }
}