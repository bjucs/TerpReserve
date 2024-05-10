package com.groupproject.terpreserve

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ReservationAdapter(context: Context, reservations: List<Reservation>)
    : ArrayAdapter<Reservation>(context, 0, reservations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.reservation_item, parent, false)
        val reservation = getItem(position)
        val textView = view.findViewById<TextView>(R.id.tvReservationDetails)

        val locationMap = mapOf("location1" to "McKeldin Library", "location2" to "Washington Quad", "location3" to "Van Munching")

        textView.text = "${locationMap[reservation?.location]} | ${reservation?.date} | ${reservation?.time}"
        return view
    }
}
