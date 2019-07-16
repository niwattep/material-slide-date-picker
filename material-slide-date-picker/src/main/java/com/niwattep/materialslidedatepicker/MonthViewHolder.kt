package com.niwattep.materialslidedatepicker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(month: Int, locale: Locale = Locale.US) {
        val textView = itemView.findViewById<TextView>(R.id.tv_month)
        val monthName = getMonths(locale)[month - 1]
        textView.text = monthName
    }
}