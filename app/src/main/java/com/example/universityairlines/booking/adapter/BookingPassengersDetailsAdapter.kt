package com.example.universityairlines.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil as BaseDiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.universityairlines.R
import com.example.universityairlines.databinding.BookingSimpleFlightViewBinding
import com.example.universityairlines.databinding.PassengerDetailItemBinding
import com.example.universityairlines.model.Passenger


/*class BookingPassengersDetailsAdapter :
    ListAdapter<Passenger, BookingPassengersDetailsAdapter.ViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.passenger_detail_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        /**
         * Add here your ViewHolder's views, such as TextViews, ImageViews etc.
         * for example:
         * private val textview = itemView.findViewById<TextView>(R.id.la_mia_textview)
         */
        private val binding = PassengerDetailItemBinding.bind(itemView)

        fun bind(position: Int) {
            /**
             * Bind your content to the view here
             * for example:
             * textview.text = entry.nome
             */

            var passenger = currentList[position]

            binding.passeggeroText.text =
                binding.getString(R.string.passeggero, (position + 1).toString())

            binding.editTextNome.doOnTextChanged { text, _, _, _ ->
                passenger =
                    passenger.copy(
                        nome = text.takeIf { it.isNullOrBlank().not() }?.toString()
                            .orEmpty()
                    )

            }

            binding.editTextCognome.doOnTextChanged { text, _, _, _ ->
                passenger =
                    passenger.copy(
                        cognome = text.takeIf { it.isNullOrBlank().not() }?.toString()
                            .orEmpty()
                    )

            }
        }
    }

    private fun PassengerDetailItemBinding.getString(
        @StringRes id: Int,
        vararg params: String
    ) =
        root.context.resources.getString(id, *params)

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
}*/