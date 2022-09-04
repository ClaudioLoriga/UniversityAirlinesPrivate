package com.example.universityairlines.booking

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.universityairlines.R
import com.example.universityairlines.repository.UserRepository
import com.example.universityairlines.databinding.ActivityBookingPaymentBinding
import com.example.universityairlines.model.ApiResult
import com.example.universityairlines.model.Flight
import com.example.universityairlines.model.Passenger
import com.example.universityairlines.ui.getString
import com.example.universityairlines.validation.setupValidation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class BookingPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Pagamento"

        val passengerDetails = intent.extras?.getParcelableArrayList<Passenger>("passengers")
        val flight = intent.extras?.getParcelable<Flight>("flightInfo")

        if (flight != null) {
            val (data, ora) = flight.departureDate.split(" ")
            with(binding.choosenFlight) {
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
                dataTextView.text = binding.getString(R.string.booking_details_flight, "Data", data)
                oraTextView.text = binding.getString(R.string.booking_details_flight, "Ora", ora)
            }

            if (passengerDetails != null) {
                with(binding) {
                    passengerNumberTextView.text =
                        binding.getString(
                            R.string.numero_passeggeri,
                            passengerDetails.size.toString()
                        )
                }

                lifecycleScope.launch {
                    binding.loadingAnimation.isVisible = true
                    binding.frameLayout.isVisible = true
                    val responseInit = UserRepository.getPaymentInit(
                        flight.origin,
                        flight.destination,
                        flight.departureDate,
                        flight.returnDate,
                        passengerDetails.size.toString(),
                        flight.price.toString()
                    )

                    when (responseInit) {
                        is ApiResult.Success -> {
                            binding.loadingAnimation.isVisible = false
                            binding.frameLayout.isVisible = false
                            binding.paySumTextView.text = binding.getString(
                                R.string.placeholder_price,
                                responseInit.value.totalToPay.toString(),
                                flight.currency
                            )
                        }
                        is ApiResult.Failure -> Unit// Mappare errore
                    }
                }


                binding.buttonPay.setOnClickListener {
                    it.isEnabled = false
                    lifecycleScope.launch {
                        binding.loadingAnimation.isVisible = true
                        binding.frameLayout.isVisible = true
                        val responseConfirmation = UserRepository.getPaymentConfirmation(
                            flight.origin,
                            flight.destination,
                            flight.departureDate,
                            flight.returnDate,
                            binding.paySumTextView.text.toString(),
                            binding.cardNumberEditText.text.toString(),
                            binding.expireEditText.text.toString(),
                            binding.cvvEditText.text.toString()
                        )

                        when (responseConfirmation) {
                            is ApiResult.Success -> {
                                binding.loadingAnimation.isVisible = false
                                binding.frameLayout.isVisible = false
                                val intent = Intent(
                                    this@BookingPaymentActivity,
                                    BookingPaymentConfirmationActivity::class.java
                                )
                                intent.putExtra("flight", flight)
                                intent.putExtra(
                                    BookingPaymentConfirmationActivity.EXTRAKEY_PNR,
                                    responseConfirmation.value.pnr
                                )
                                intent.putExtra(
                                    BookingPaymentConfirmationActivity.EXTRAKEY_TTP,
                                    binding.paySumTextView.text
                                )
                                intent.putExtra(
                                    BookingPaymentConfirmationActivity.EXTRAKEY_CARD_NUMBER,
                                    binding.cardNumberEditText.text.toString()
                                )
                                intent.putExtra(
                                    BookingPaymentConfirmationActivity.EXTRAKEY_CARD_EXPIRATION,
                                    binding.expireEditText.text.toString()
                                )
                                intent.putExtra(
                                    BookingPaymentConfirmationActivity.EXTRAKEY_CVV,
                                    binding.cvvEditText.text.toString()
                                )
                                startActivity(intent)
                            }

                            is ApiResult.Failure -> {
                                MaterialAlertDialogBuilder(this@BookingPaymentActivity)
                                    .setTitle(resources.getString(R.string.attenzione))
                                    .setPositiveButton(
                                        "Ok"
                                    ) { dialog, which -> dialog?.dismiss() }
                                    .setMessage(
                                        resources.getString(R.string.problema_riprovare_pagamento)
                                    ).show()
                                binding.loadingAnimation.isVisible = false
                                binding.frameLayout.isVisible = false
                            }
                        }
                    }
                }
            }
            binding.paySumTextView.text = binding.getString(
                R.string.placeholder_price,
                "",
                flight.price.toString()
            )
        }

       /* with(binding){
            setupValidation(buttonPay, cardNumberEditText, expireEditText, cvvEditText)
        }*/

        binding.terminiServizio.setFormatLinkText(
            resources.getString(R.string.cliccando_paga_ora_accetto_i_termini_e_condizioni_dell_esercente_del_pagamento),
            resources.getString(R.string.terminiecondizioni) to ::openWebsite,
            showUnderLine = true
        )
    }

    @Suppress("SpreadOperator")
    fun TextView.setFormatLinkText(message: String, vararg links: Pair<String, () -> Unit>, showUnderLine: Boolean = false) {
        val linkTexts = links.map { it.first }.toTypedArray()
        val formattedMessage = String.format(message, *linkTexts)
        val spannableString = SpannableString(formattedMessage)

        links.forEach { pair ->
            val linkText = pair.first
            val linkListener = pair.second

            val linkStartIndex = spannableString.indexOf(linkText)
            val linkEndIndex = linkStartIndex + linkText.length

            if (linkStartIndex < 0 || linkEndIndex > spannableString.length) return@forEach

            spannableString.setSpan(
                getClickableSpan(linkListener),
                linkStartIndex,
                linkEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (showUnderLine) {
                spannableString.setSpan(UnderlineSpan(), linkStartIndex, linkEndIndex, 0)
            }
        }

        movementMethod = LinkMovementMethod.getInstance()
        text = spannableString
    }

    private fun getClickableSpan(onClickListener: () -> Unit) = object : ClickableSpan() {
        override fun onClick(widget: View) {
            widget.cancelPendingInputEvents()
            onClickListener()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = ds.linkColor
            ds.typeface = Typeface.DEFAULT_BOLD
        }
    }

    private fun openWebsite() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.visaitalia.com/assistenza-visa/consumatore/visa-rules.html")
        startActivity(intent)
    }

}
