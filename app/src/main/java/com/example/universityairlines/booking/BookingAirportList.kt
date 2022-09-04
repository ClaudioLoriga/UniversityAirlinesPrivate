package com.example.universityairlines.booking

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.universityairlines.R
import com.example.universityairlines.repository.UserRepository
import com.example.universityairlines.booking.adapter.AirportAdapter
import com.example.universityairlines.databinding.ActivityAirportListBinding
import com.example.universityairlines.databinding.BookingFlightsListBinding
import com.example.universityairlines.model.ApiResult
import com.example.universityairlines.model.GetAirportResponse
import kotlinx.coroutines.launch

class BookingAirportList : AppCompatActivity() {
    private lateinit var adapter: AirportAdapter
    private lateinit var binding: ActivityAirportListBinding
    private val layoutManager by lazy { LinearLayoutManager(this@BookingAirportList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAirportListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Lista aeroporti"

        lifecycleScope.launch {
            binding.loadingAnimation.isVisible = true
            binding.frameLayout.isVisible = true
            val response = UserRepository.getAirports(
                "code",
                "name",
                "citycode",
                "city",
                "countrycode",
                "country",
                "continent"
            )

            when (response) {
                is ApiResult.Success -> {
                    binding.textViewSelezioneVolo.isVisible = true
                    binding.loadingAnimation.isVisible = false
                    binding.frameLayout.isVisible = false
                    adapter = AirportAdapter(response.value.airports, ::getAirportName)
                    binding.recycleviewairports.adapter = adapter
                    binding.recycleviewairports.layoutManager = layoutManager

                }
                is ApiResult.Failure -> Unit// Mappare errore
            }
        }

    }

    companion object {
        const val EXTRAKEY_AIRPORT = "Volo"
    }

    fun getAirportName(airportName: String) {
        val intent = Intent()
        intent.putExtra(EXTRAKEY_AIRPORT, airportName)
        setResult(RESULT_OK, intent)
        finish()
    }
}