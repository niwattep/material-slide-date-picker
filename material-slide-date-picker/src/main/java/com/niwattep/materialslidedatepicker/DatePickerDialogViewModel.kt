package com.niwattep.materialslidedatepicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import java.util.*
import kotlin.collections.ArrayList

class DatePickerDialogViewModel(
    private var startDate: Calendar,
    private var endDate: Calendar,
    currentDate: Calendar) : ViewModel() {

    private val currentDay: MutableLiveData<Int> = MutableLiveData()
    private val currentMonth: MutableLiveData<Int> = MutableLiveData()
    private val currentYear: MutableLiveData<Int> = MutableLiveData()

    private val days: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    private val months: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    private val years: MutableLiveData<ArrayList<Int>> = MutableLiveData()

    private val calendar: MutableLiveData<Calendar> = MutableLiveData()

    init {
        this.calendar.value = currentDate

        initCurrentDate()
        initData()
    }

    private fun initCurrentDate() {
        currentDay.value = calendar.value!!.get(Calendar.DAY_OF_MONTH)
        currentMonth.value = calendar.value!!.get(Calendar.MONTH) + 1
        currentYear.value = calendar.value!!.get(Calendar.YEAR)
    }

    private fun initData() {
        years.value = getYearFromTo(startDate.get(Calendar.YEAR), endDate.get(Calendar.YEAR))
    }

    fun getCurrentDay(): LiveData<Int> = currentDay

    fun getCurrentMonth(): LiveData<Int> = currentMonth

    fun getCurrentYear(): LiveData<Int> = currentYear

    fun getDays(): LiveData<ArrayList<Int>> = days

    fun getMonths(): LiveData<ArrayList<Int>> = months

    fun getYears(): LiveData<ArrayList<Int>> = years

    fun getCalendar(): LiveData<Calendar> = calendar

    fun setYear(year: Int) {
        currentYear.value = year
        months.value = getAvailableMonths(currentYear.value!!, startDate, endDate)

        calendar.value = getCalendar(currentDay.value!!, currentMonth.value!!, currentYear.value!!)
    }

    fun setMonth(month: Int) {
        currentMonth.value = month
        days.value = getAvailableDays(currentYear.value!!, currentMonth.value!!, startDate, endDate)

        calendar.value = getCalendar(currentDay.value!!, currentMonth.value!!, currentYear.value!!)
    }

    fun setDay(day: Int) {
        currentDay.value = day

        calendar.value = getCalendar(currentDay.value!!, currentMonth.value!!, currentYear.value!!)
    }
}
