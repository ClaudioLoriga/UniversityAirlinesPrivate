package com.example.universityairlines.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.example.universityairlines.R
import com.example.universityairlines.databinding.PassengerDetailItemBinding
import com.example.universityairlines.databinding.PassengersDetailsListBinding
import com.example.universityairlines.model.Flight
import com.example.universityairlines.model.FlightsResponse
import com.example.universityairlines.model.Passenger
import com.example.universityairlines.validation.setupValidation

class BookingPassengersDetailsActivity : AppCompatActivity() {

    private lateinit var binding: PassengersDetailsListBinding
    private val passengersDetails = ArrayList<Passenger>()
    private val campi = mutableListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PassengersDetailsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Passeggeri"
        val flightList = intent.extras?.getParcelable<FlightsResponse>("flightList")
        val flight = intent.extras?.getParcelable<Flight>("flight")

        if (flightList != null) {
            setupUi(flightList)
            binding.buttonConferma.setOnClickListener {
                val intent = Intent(this, BookingPaymentActivity::class.java)
                intent.putExtra("passengers", passengersDetails)
                intent.putExtra("flightInfo", flight)
                startActivity(intent)
            }
        }

    }

    fun setupUi(flightList: FlightsResponse) {
        for (i in 0 until (flightList.passengersNumber.toInt())) {
            binding.linearlayout.addView(creaVista(i))
        }

        setupValidation(binding.buttonConferma, *campi.toTypedArray())
    }

    fun creaVista(passengerNumber: Int): View {
        passengersDetails.add(passengerNumber, Passenger())
        val entryBinding =
            PassengerDetailItemBinding.inflate(layoutInflater, binding.linearlayout, false)

        entryBinding.passeggeroText.text =
            getString(R.string.passeggero, (passengerNumber + 1).toString())

        entryBinding.editTextNome.doOnTextChanged { text, _, _, _ ->
            passengersDetails[passengerNumber] = passengersDetails[passengerNumber].copy(
                nome = text.takeIf { it.isNullOrBlank().not() }?.toString()
                    .orEmpty()
            )
        }

        entryBinding.editTextCognome.doOnTextChanged { text, _, _, _ ->
            passengersDetails[passengerNumber] = passengersDetails[passengerNumber].copy(
                cognome = text.takeIf { it.isNullOrBlank().not() }?.toString()
                    .orEmpty()
            )
        }

        campi.addAll(
            listOf(
                entryBinding.editTextNome,
                entryBinding.editTextCognome,
                entryBinding.editTextIndirizzo,
                entryBinding.editTextTelefono,
                entryBinding.editTextDocumento
            )
        )
        return entryBinding.root
    }
}