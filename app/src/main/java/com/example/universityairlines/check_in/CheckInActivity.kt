package com.example.universityairlines.check_in

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.universityairlines.R
import com.example.universityairlines.databinding.ActivityCheckInBinding
import com.example.universityairlines.homepage.HomepageActivity
import com.example.universityairlines.model.Flight
import com.example.universityairlines.model.Reservation
import com.example.universityairlines.ui.getString
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CheckInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Check-in"

        val selectedCode = intent.getStringExtra("code").toString()
        var selectedReservation = Reservation("", "", "", "", "", false, "")
        val sharedPref = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE) ?: return
        val defaultValue = ""
        val reservationListString = sharedPref.getString(
            getString(R.string.reservation_list_shared_preferences),
            defaultValue
        )
        val mapper = jacksonObjectMapper()

        if (reservationListString != "") {
            var flag = false
            val reservationList = mapper.readValue(
                reservationListString,
                object : TypeReference<MutableList<Reservation>>() {})

            reservationList.forEach { reservation ->
                if (reservation.code.uppercase() == selectedCode.uppercase()) {
                    flag = true
                    with(binding.buyedFlight) {
                        andataTextView.text = binding.getString(
                            R.string.booking_details_flight,
                            "Origine:",
                            reservation.origin
                        )
                        ritornoTextView.text = binding.getString(
                            R.string.booking_details_flight,
                            "Destinazione:",
                            reservation.destination
                        )
                        dataTextView.text =
                            binding.getString(
                                R.string.booking_details_flight,
                                "Data",
                                reservation.date
                            )
                        oraTextView.text =
                            binding.getString(
                                R.string.booking_details_flight,
                                "Ora",
                                reservation.hour
                            )

                    }
                    selectedReservation = reservation
                }
            }

            if (!flag) {
                MaterialAlertDialogBuilder(this@CheckInActivity)
                    .setTitle("VOLO NON TROVATO")
                    .setPositiveButton(
                        "Ok"
                    ) { dialog, which -> dialog?.dismiss() }
                    .setMessage(
                        "Il volo cercato non è presente o non esiste"
                    ).show()
            }
        } else {
            MaterialAlertDialogBuilder(this@CheckInActivity)
                .setTitle("VOLO NON TROVATO")
                .setPositiveButton(
                    "Ok"
                ) { dialog, which -> dialog?.dismiss() }
                .setMessage(
                    "Non sono presenti voli"
                ).show()
        }

        binding.effettuaCheckInButton.setOnClickListener {

            if (!selectedReservation.checkin) {
                val intent = Intent(this, CheckInConfirmedActivity::class.java)
                intent.putExtra("code", selectedCode)
                startActivity(intent)
            } else {
                MaterialAlertDialogBuilder(this@CheckInActivity)
                    .setTitle("CHECK-IN GIÀ EFFETTUATO")
                    .setPositiveButton(
                        "Ok"
                    ) { dialog, which -> dialog?.dismiss() }
                    .setMessage(
                        "Il check-in per questo volo risulta già effettuato"
                    ).show()
            }
        }
    }
}