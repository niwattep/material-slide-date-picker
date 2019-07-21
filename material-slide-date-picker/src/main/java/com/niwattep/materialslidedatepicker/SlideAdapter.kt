package com.niwattep.materialslidedatepicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class SlideAdapter(private val type: Type) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: ArrayList<Int> = ArrayList()

    var locale: Locale = Locale.US
    var yearModifier = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (type) {
            Type.DAY -> DayViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_holder_day,
                    parent,
                    false
                )
            )
            Type.MONTH -> MonthViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_holder_month,
                    parent,
                    false
                )
            )
            else -> YearViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_holder_year,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DayViewHolder -> holder.bind(data[position])
            is MonthViewHolder -> holder.bind(data[position], locale)
            is YearViewHolder -> holder.bind(data[position], yearModifier)
        }
    }

    fun getPositionByValue(value: Int): Int {
        data.forEachIndexed { index, item ->
            if (value == item) {
                return index
            }
        }
        return data.size - 1
    }

    fun getValueByPosition(position: Int): Int {
        data.forEachIndexed { index, item ->
            if (position == index) {
                return item
            }
        }
        return 1
    }

    enum class Type {
        DAY, MONTH, YEAR
    }
}