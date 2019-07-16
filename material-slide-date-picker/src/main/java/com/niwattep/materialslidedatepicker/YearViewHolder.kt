package com.niwattep.materialslidedatepicker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class YearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(year: Int, yearModifier: Int = 0) {
        val textView = itemView.findViewById<TextView>(R.id.tv_year)
        textView.text = (year + yearModifier).toString()
    }
}