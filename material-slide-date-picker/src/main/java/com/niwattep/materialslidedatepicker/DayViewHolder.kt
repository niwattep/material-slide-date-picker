package com.niwattep.materialslidedatepicker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(day: Int) {
        val textView = itemView.findViewById<TextView>(R.id.tv_day)
        textView.text = format2LengthDay(day)
    }
}