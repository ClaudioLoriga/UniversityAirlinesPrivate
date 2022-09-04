package com.example.universityairlines.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityairlines.R
import androidx.recyclerview.widget.DiffUtil as BaseDiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.universityairlines.model.Airport

class AirportAdapter(private val mList: List<Airport>, private val callBack: (String) -> Unit) :
    ListAdapter<Airport, AirportAdapter.ViewHolder>(
        DiffUtil()
    ) {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val airport = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.nameAirport.text = airport.name
        holder.nameAirport.setOnClickListener {
            callBack(airport.name)
        }

    }

    class DiffUtil : BaseDiffUtil.ItemCallback<Airport>() {
        override fun areItemsTheSame(oldItem: Airport, newItem: Airport): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Airport, newItem: Airport): Boolean {
            return oldItem == newItem
        }
    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameAirport: TextView = itemView.findViewById(R.id.textviewname)
    }
}
