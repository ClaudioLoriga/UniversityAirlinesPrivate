package com.example.universityairlines.booking

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityairlines.repository.UserRepository
import com.example.universityairlines.booking.adapter.BookingFlightListAdapter
import com.example.universityairlines.databinding.BookingFlightsListBinding
import com.example.universityairlines.model.ApiResult
import com.example.universityairlines.model.Flight
import com.example.universityairlines.model.FlightsResponse
import kotlinx.coroutines.launch


class BookingFlightsListActivity : AppCompatActivity() {

    private lateinit var binding: BookingFlightsListBinding
    private lateinit var adapter: BookingFlightListAdapter
    private val layoutManager by lazy { LinearLayoutManager(this@BookingFlightsListActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BookingFlightsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Lista voli"

        val stringOrigine = intent.getStringExtra(EXTRAKEY_ORIGINE).orEmpty()
        val stringDestinazione = intent.getStringExtra(EXTRAKEY_DESTINAZIONE).orEmpty()
        //val stringOrigine = "Cagliari" //STRINGA PROVVISORIA
        //val stringDestinazione = "Bogota" // STRINGA PROVVISORIA
        val stringAndata = intent.getStringExtra(EXTRAKEY_ANDATA).orEmpty()
        val stringRitorno = intent.getStringExtra(EXTRAKEY_RITORNO).orEmpty()
        val stringPasseggeri = intent.getStringExtra(EXTRAKEY_PASSEGGERI).orEmpty()

        lifecycleScope.launch {
            binding.loadingAnimation.isVisible = true
            binding.frameLayout.isVisible = true
            val response = UserRepository.getFlights(
                stringOrigine,
                stringDestinazione,
                stringAndata,
                stringRitorno,
                stringPasseggeri
            )
            binding.recyclerView.layoutManager = layoutManager

            when (response) {
                is ApiResult.Success -> {
                    binding.loadingAnimation.isVisible = false
                    binding.frameLayout.isVisible = false
                    adapter = BookingFlightListAdapter(
                        callBack = ::navigateNextActivityWithFlight,
                        response.value,
                    )
                    binding.recyclerView.adapter = adapter
                    binding.textViewSelezioneVolo.isVisible = true
                    creaListaAeroporti(response.value)
                }
                is ApiResult.Failure -> Unit// Mappare errore
            }


        }
    }

    companion object {
        const val EXTRAKEY_ORIGINE = "origine"
        const val EXTRAKEY_DESTINAZIONE = "destinazione"
        const val EXTRAKEY_ANDATA = "andata"
        const val EXTRAKEY_RITORNO = "ritorno"
        const val EXTRAKEY_PASSEGGERI = "passeggeri"
    }

    fun creaListaAeroporti(response: FlightsResponse) {
        adapter.submitList(response.flights)
    }

    fun navigateNextActivityWithFlight(flightList: FlightsResponse, flight: Flight) {
        val intent = Intent(this, BookingPassengersDetailsActivity::class.java)
        intent.putExtra("flightList", flightList)
        intent.putExtra("flight", flight)
        startActivity(intent)
    }
}