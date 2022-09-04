package com.example.universityairlines.booking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.universityairlines.booking.BookingFlightsListActivity.Companion.EXTRAKEY_ANDATA
import com.example.universityairlines.booking.BookingFlightsListActivity.Companion.EXTRAKEY_DESTINAZIONE
import com.example.universityairlines.booking.BookingFlightsListActivity.Companion.EXTRAKEY_ORIGINE
import com.example.universityairlines.booking.BookingFlightsListActivity.Companion.EXTRAKEY_PASSEGGERI
import com.example.universityairlines.booking.BookingFlightsListActivity.Companion.EXTRAKEY_RITORNO
import com.example.universityairlines.databinding.BookingFormLayoutBinding
import com.example.universityairlines.ui.toPrettyDate
import com.example.universityairlines.validation.setupValidation
import com.google.android.material.datepicker.*
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import java.util.*

class BookingFragment : Fragment() {

    private lateinit var binding: BookingFormLayoutBinding
    private var resultLauncherOrigine =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if ((result.resultCode == Activity.RESULT_OK) && result != null) {
                // There are no request codes
                val data: Intent? = result.data
                binding.edittextorigine.setText(data?.getStringExtra(BookingAirportList.EXTRAKEY_AIRPORT))

            }
        }

    private var resultLauncherDestinazione =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if ((result.resultCode == Activity.RESULT_OK) && result != null) {
                // There are no request codes
                val data: Intent? = result.data
                binding.edittextdestinazione.setText(data?.getStringExtra(BookingAirportList.EXTRAKEY_AIRPORT))

            }
        }

    private var minDate: Long = Calendar.getInstance().time.time - 86400000
    private var maxDate: Long = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BookingFormLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var number = 0

        binding.edittextorigine.setOnClickListener {
            val intent = Intent(context, BookingAirportList::class.java)
            it.isEnabled = false
            resultLauncherOrigine.launch(intent)
        }

        binding.edittextdestinazione.setOnClickListener {
            val intent = Intent(context, BookingAirportList::class.java)
            it.isEnabled = false
            resultLauncherDestinazione.launch(intent)
        }

        binding.edittextandata.setOnClickListener {
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleziona data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .apply {
                    val constraints: CalendarConstraints =
                        CalendarConstraints.Builder().apply {
                            val list = buildList {
                                add(DateValidatorPointForward.from(minDate))
                                if (maxDate != -1L) add(DateValidatorPointBackward.before(maxDate))
                            }
                            setValidator(CompositeDateValidator.allOf(list))
                        }.build()
                    setCalendarConstraints(constraints)
                }
                .build()
                .apply {
                    addOnPositiveButtonClickListener {
                        binding.edittextandata.setText(it.toPrettyDate())
                        minDate = it
                    }
                    show(this@BookingFragment.requireActivity().supportFragmentManager, null)
                }
        }

        binding.edittextritorno.setOnClickListener {
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleziona data")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .apply {
                    if (minDate != -1L) {
                        Log.v("BookingActivity", "$minDate 455332")
                        val constraints: CalendarConstraints =
                            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.from(minDate)).build()
                        setCalendarConstraints(constraints)
                    }
                }
                .build()
                .apply {
                    addOnPositiveButtonClickListener {
                        binding.edittextritorno.setText(it.toPrettyDate())
                        maxDate = it
                    }
                        show(this@BookingFragment.requireActivity().supportFragmentManager, null)
                }
        }

        binding.minusbutton.setOnClickListener {
            if (number > 1) {
                number = number.dec()
            }
            binding.npasseggeriedittext.setText(number.toString())
        }
        binding.plusbutton.setOnClickListener {
            if(number <= 7)
            number = number.inc()
            binding.npasseggeriedittext.setText(number.toString())
        }

        binding.cercavolibutton.setOnClickListener {
            val intent = Intent(context, BookingFlightsListActivity::class.java)
            intent.putExtra(EXTRAKEY_ORIGINE, binding.edittextorigine.text.toString())
            intent.putExtra(
                EXTRAKEY_DESTINAZIONE,
                binding.edittextdestinazione.text.toString()
            )
            intent.putExtra(EXTRAKEY_ANDATA, binding.edittextandata.text.toString())
            intent.putExtra(EXTRAKEY_RITORNO, binding.edittextritorno.text.toString())
            intent.putExtra(
                EXTRAKEY_PASSEGGERI,
                binding.npasseggeriedittext.text.toString()
            )
            it.isEnabled = false
            startActivity(intent)
        }

        with(binding) {
            setupValidation(cercavolibutton, edittextandata, edittextorigine, edittextritorno, edittextdestinazione, npasseggeriedittext)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.edittextorigine.isEnabled = true
        binding.edittextdestinazione.isEnabled = true
    }
}


