package com.example.universityairlines.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil as BaseDiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.universityairlines.R
import com.example.universityairlines.databinding.BookingSimpleFlightViewBinding
import com.example.universityairlines.model.Flight
import com.example.universityairlines.model.FlightsResponse
import com.example.universityairlines.ui.getString
import kotlinx.coroutines.flow.callbackFlow


class BookingFlightListAdapter(private val callBack: (FlightsResponse, Flight) -> Unit, private val flightList : FlightsResponse) :
    ListAdapter<Flight, BookingFlightListAdapter.ViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_simple_flight_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        /**
         * Add here your ViewHolder's views, such as TextViews, ImageViews etc.
         * for example:
         * private val textview = itemView.findViewById<TextView>(R.id.la_mia_textview)
         */
        private val binding = BookingSimpleFlightViewBinding.bind(itemView)


        fun bind(entry: Flight) {
            /**
             * Bind your content to the view here
             * for example:
             * textview.text = entry.nome
             */
            val (data, ora) = entry.departureDate.split(" ")
            val stringAndata =
                binding.getString(R.string.airport_description, entry.origin, entry.originIata)
            val stringRitorno = binding.getString(
                R.string.airport_description,
                entry.destination,
                entry.destinationIata
            )
            val stringData = binding.getString(R.string.booking_details_flight, "Data", data)
            val stringOra = binding.getString(R.string.booking_details_flight, "Ora", ora)
            with(binding) {
                andataTextView.text = stringAndata
                ritornoTextView.text = stringRitorno
                dataTextView.text = stringData
                oraTextView.text = stringOra

                card.setOnClickListener {
                    callBack(flightList, entry)
                }
            }

        }
    }

//    private fun BookingSimpleFlightViewBinding.getString(
//        @StringRes id: Int,
  //      vararg params: String
  //  ) =
   //     root.context.resources.getString(id, *params)

    class DiffUtil : BaseDiffUtil.ItemCallback<Flight>() {
        override fun areItemsTheSame(oldItem: Flight, newItem: Flight): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Flight, newItem: Flight): Boolean {
            return oldItem == newItem
        }
    }
}
