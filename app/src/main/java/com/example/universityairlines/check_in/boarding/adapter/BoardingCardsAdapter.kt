package com.example.universityairlines.check_in.boarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil as BaseDiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.universityairlines.R
import com.example.universityairlines.databinding.BoardingCardBinding
import com.example.universityairlines.model.Passenger
import com.example.universityairlines.model.Reservation
import com.example.universityairlines.ui.getString
import com.example.universityairlines.ui.randomAlphanumeric

class BoardingCardsAdapter(private val reservation: Reservation) :
    ListAdapter<Passenger, BoardingCardsAdapter.ViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.boarding_card, parent, false)
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
        private val binding = BoardingCardBinding.bind(itemView)

        fun bind(entry: Passenger) {
            /**
             * Bind your content to the view here
             * for example:
             * textview.text = entry.nome
             */
            val stringData = binding.getString(R.string.booking_details_flight, "Data", reservation.date)
            val stringOra = binding.getString(R.string.booking_details_flight, "Ora", reservation.hour)
            binding.prenotationReference.text = reservation.code
            binding.passengerNameSurname.text = entry.nome.plus(" ").plus(entry.cognome)
            binding.postoAssegnato.text = reservation.postoAssegnato
             binding.origineTextView.text = reservation.origin
             binding.destinazioneTextView.text = reservation.destination
             binding.dataTextView.text = stringData
             binding.oraTextView.text = stringOra
        }
    }

    class DiffUtil : BaseDiffUtil.ItemCallback<Passenger>() {
        override fun areItemsTheSame(oldItem: Passenger, newItem: Passenger): Boolean {
            /**
             * Check if the items are the same by using an item unique characteristic
             * such as an id, a timestamp etc.
             */
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Passenger, newItem: Passenger): Boolean {
            /**
             * Make sure the items your comparing implement their own [equals()]
             * (or leave it as is if you're happy with the default one, such as data classes)
             */
            return oldItem == newItem
        }
    }
}