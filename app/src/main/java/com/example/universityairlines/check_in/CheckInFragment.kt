package com.example.universityairlines.check_in

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.universityairlines.R
import com.example.universityairlines.check_in.boarding.BoardingActivity
import com.example.universityairlines.databinding.ActivityHomepageBinding
import com.example.universityairlines.databinding.FragmentCheckInBinding
import com.example.universityairlines.databinding.FragmentContainerHomepageBinding
import com.example.universityairlines.registration.RegistrationActivity
import com.example.universityairlines.validation.setupValidation

class CheckInFragment : Fragment() {

    private lateinit var binding: FragmentCheckInBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCheckInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkInButton.setOnClickListener {
            val intent = Intent(context, CheckInActivity::class.java)
            intent.putExtra("code", binding.editTextPrenotazione.text.toString())
            startActivity(intent)
        }

        binding.boardingCardsButton.setOnClickListener {
            val intent = Intent(context, BoardingActivity::class.java)
            intent.putExtra("code", binding.editTextPrenotazione.text.toString())
            startActivity(intent)
        }

        with(binding) {
            setupValidation(checkInButton, editTextPrenotazione, editTextEmail)
        }

        with(binding) {
            setupValidation(boardingCardsButton, editTextPrenotazione, editTextEmail)
        }
    }

    companion object {
        const val EXTRAKEY = "username"
    }

}