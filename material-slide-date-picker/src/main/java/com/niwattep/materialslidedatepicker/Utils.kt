package com.niwattep.materialslidedatepicker

import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

fun getMonthMaxDay(month: Int, year: Int): Int {
    return when {
        month == 4 || month == 6 || month == 9 || month == 11 -> 30
        month == 2 && !isLeapYear(year) -> 28
        month == 2 && isLeapYear(year) -> 29
        else -> 31
    }
}

fun getYearFromTo(start: Int, end: Int): ArrayList<Int> {
    val years = ArrayList<Int>()

    val yearCount = end - start

    for (i in 0..yearCount) {
        years.add(start + i)
    }

    return years
}


fun getAvailableMonths(currentYear: Int, startDate: Calendar, endDate: Calendar): ArrayList<Int> {
    val result = ArrayList<Int>()

    val maxYear = endDate.get(Calendar.YEAR)
    val minYear = startDate.get(Calendar.YEAR)

    if (minYear != maxYear) {
        when (currentYear) {
            minYear -> {
                val minMonth = startDate.get(Calendar.MONTH) + 1
                result.addAll((minMonth..12).toList())
            }
            maxYear -> {
                val maxMonth = endDate.get(Calendar.MONTH) + 1
                result.addAll((1..maxMonth).toList())
            }
            else -> {
                result.addAll((1..12).toList())
            }
        }
    } else {
        val minMonth = startDate.get(Calendar.MONTH) + 1
        val maxMonth = endDate.get(Calendar.MONTH) + 1
        result.addAll((minMonth..maxMonth).toList())
    }

    return result
}

fun getAvailableDays(currentYear: Int, currentMonth: Int, startDate: Calendar, endDate: Calendar): ArrayList<Int> {
    val result = ArrayList<Int>()

    val maxYear = endDate.get(Calendar.YEAR)
    val minYear = startDate.get(Calendar.YEAR)
    val maxMonth = endDate.get(Calendar.MONTH) + 1
    val minMonth = startDate.get(Calendar.MONTH) + 1


    when {
        minYear == maxYear && currentYear == maxYear && maxMonth == minMonth && currentMonth == minMonth -> {
            val minDate = startDate.get(Calendar.DAY_OF_MONTH)
            val maxDate = endDate.get(Calendar.DAY_OF_MONTH)
            result.addAll((minDate..maxDate).toList())
        }
        currentYear == minYear && currentMonth == minMonth -> {
            val minDate = startDate.get(Calendar.DAY_OF_MONTH)
            val maxDate = getMonthMaxDay(currentMonth, currentYear)
            result.addAll((minDate..maxDate).toList())
        }
        currentYear == maxYear && currentMonth == maxMonth -> {
            val maxDate = endDate.get(Calendar.DAY_OF_MONTH)
            result.addAll((1..maxDate).toList())
        }
        else -> {
            val maxDate = getMonthMaxDay(currentMonth, currentYear)
            result.addAll((1..maxDate).toList())
        }
    }

    return result
}

fun getCalendar(day: Int, month: Int, year: Int): Calendar {
    return Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.MONTH, month - 1)
        set(Calendar.YEAR, year)
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
    }
}

fun getMonths(locale: Locale = Locale.US): ArrayList<String> {
    return ArrayList(DateFormatSymbols(locale).months.toList())
}

fun format2LengthDay(num: Int): String {
    return if (num < 10) "0$num" else num.toString()
}
