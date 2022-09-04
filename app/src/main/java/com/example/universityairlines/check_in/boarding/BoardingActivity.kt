package com.example.universityairlines.check_in.boarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityairlines.R
import com.example.universityairlines.booking.adapter.BookingFlightListAdapter
import com.example.universityairlines.check_in.boarding.adapter.BoardingCardsAdapter
import com.example.universityairlines.databinding.ActivityCheckInConfirmedBinding
import com.example.universityairlines.databinding.BoardingCardBinding
import com.example.universityairlines.databinding.BoardingCardListBinding
import com.example.universityairlines.homepage.HomepageActivity
import com.example.universityairlines.model.Reservation
import com.example.universityairlines.ui.getString
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.random.Random

class BoardingActivity : AppCompatActivity() {

    private lateinit var binding: BoardingCardListBinding
    private lateinit var adapter: BoardingCardsAdapter
    private val layoutManager by lazy { LinearLayoutManager(this@BoardingActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardingCardListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Carte d'imbarco"

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

            binding.recyclerView.layoutManager = layoutManager
            reservationList.forEach { reservation ->
                if (reservation.code.uppercase() == codeForCheckin.uppercase() && reservation.checkin) {
                    // Ho la reservation corretta, devo popolare tante boarding pass quanti sono i passeggeri
                    adapter = BoardingCardsAdapter(reservation)
                    binding.recyclerView.adapter = adapter
                    adapter.submitList(reservation.passengerDetails)
                }
            }
        }

        binding.buttonConferma.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
        }
    }
}
