package com.niwattep.materialslidedatepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class DatePickerDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(
            startDate: Calendar? = null,
            endDate: Calendar? = null,
            preselectedDate: Calendar? = null,
            yearModifier: Int = 0,
            locale: Locale? = null,
            @ColorInt themeColor: Int = -1,
            @ColorInt headerTextColor: Int = -1,
            headerTextFormat: String? = null,
            showYear: Boolean = true,
            cancelText: String = "",
            confirmText: String = ""
        ) = DatePickerDialogFragment().apply {
            arguments = Bundle().apply {
                startDate?.let { putSerializable(EXTRA_START_DATE, it) }
                endDate?.let { putSerializable(EXTRA_END_DATE, it) }
                preselectedDate?.let { putSerializable(EXTRA_PRESELECTED_DATE, it) }
                putInt(EXTRA_YEAR_MODIFIER, yearModifier)
                locale?.let { putSerializable(EXTRA_LOCALE, it) }
                putInt(EXTRA_THEME_COLOR, themeColor)
                putInt(EXTRA_HEADER_TEXT_COLOR, headerTextColor)
                headerTextFormat?.let { putString(EXTRA_HEADER_DATE_FORMAT, it) }
                putBoolean(EXTRA_SHOW_YEAR, showYear)
                putString(EXTRA_CANCEL_TEXT, cancelText)
                putString(EXTRA_CONFIRM_TEXT, confirmText)
            }
        }

        const val EXTRA_START_DATE = "extra-start-date"
        const val EXTRA_END_DATE = "extra-end-date"
        const val EXTRA_PRESELECTED_DATE = "extra-preselected-date"
        const val EXTRA_YEAR_MODIFIER = "extra-year-modifier"
        const val EXTRA_LOCALE = "extra-locale"
        const val EXTRA_THEME_COLOR = "extra-theme-color"
        const val EXTRA_HEADER_TEXT_COLOR = "extra-header-text-color"
        const val EXTRA_HEADER_DATE_FORMAT = "extra-header-date-format"
        const val EXTRA_SHOW_YEAR = "extra-show-year"
        const val EXTRA_CANCEL_TEXT = "extra-cancel-text"
        const val EXTRA_CONFIRM_TEXT = "extra-confirm-text"
    }

    private lateinit var viewModel: DatePickerDialogViewModel

    private lateinit var rootView: View
    private val topContainer: LinearLayout by lazy { rootView.findViewById<LinearLayout>(R.id.top_container) }
    private val tvYear: TextView by lazy { rootView.findViewById<TextView>(R.id.tv_year) }
    private val tvDate: TextView by lazy {rootView.findViewById<TextView>(R.id.tv_date) }
    private val recyclerViewDay: RecyclerView by lazy { rootView.findViewById<RecyclerView>(R.id.recycler_view_day) }
    private val recyclerViewMonth: RecyclerView by lazy { rootView.findViewById<RecyclerView>(R.id.recycler_view_month) }
    private val recyclerViewYear: RecyclerView by lazy { rootView.findViewById<RecyclerView>(R.id.recycler_view_year) }
    private val btnCancel: Button by lazy { rootView.findViewById<Button>(R.id.btn_cancel) }
    private val btnConfirm: Button by lazy { rootView.findViewById<Button>(R.id.btn_confirm) }

    private val dayAdapter = WheelAdapter(WheelAdapter.Type.DAY)
    private val monthAdapter = WheelAdapter(WheelAdapter.Type.MONTH)
    private val yearAdapter = WheelAdapter(WheelAdapter.Type.YEAR)
    private val daySnapHelper = LinearSnapHelper()
    private val monthSnapHelper = LinearSnapHelper()
    private val yearSnapHelper = LinearSnapHelper()
    lateinit var dayLayoutManager: LinearLayoutManager
    lateinit var monthLayoutManager: LinearLayoutManager
    lateinit var yearLayoutManager: LinearLayoutManager

    private var yearLastScrollState = RecyclerView.SCROLL_STATE_SETTLING
    private var monthLastScrollState = RecyclerView.SCROLL_STATE_SETTLING
    private var dayLastScrollState = RecyclerView.SCROLL_STATE_SETTLING

    private var startDate: Calendar = Calendar.getInstance().apply {
        this.set(Calendar.YEAR, this.get(Calendar.YEAR) - 100)
        this.set(Calendar.MONTH, 0)
        this.set(Calendar.DAY_OF_MONTH, 1)
    }
    private var endDate: Calendar = Calendar.getInstance()
    private var preselectedDate: Calendar = Calendar.getInstance()
    private var yearModifier: Int = 0
    private var locale: Locale = Locale.US
    @ColorInt private var themeColor: Int = -1
    @ColorInt private var headerTextColor: Int = -1
    private var headerDateFormat: String = "EEE, MMM dd"
    private var showYear = true
    private var cancelText = ""
    private var confirmText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { bundle ->
            restoreInstanceState(bundle)
        } ?: run {
            restoreArgument(arguments)
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.dialog_date_picker, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DatePickerDialogViewModel(startDate, endDate, preselectedDate) as T
            }

        }).get(DatePickerDialogViewModel::class.java)
        initialize()
        observe()
    }

    private fun restoreInstanceState(bundle: Bundle) {
        bundle.run {
            getSerializable(EXTRA_START_DATE)?.let { startDate = it as Calendar }
            getSerializable(EXTRA_END_DATE)?.let { endDate = it as Calendar }
            getSerializable(EXTRA_PRESELECTED_DATE)?.let { preselectedDate = it as Calendar }
            getInt(EXTRA_YEAR_MODIFIER).let { yearModifier = it }
            getSerializable(EXTRA_LOCALE)?.let { locale = it as Locale }
            getInt(EXTRA_THEME_COLOR, -1).let { themeColor = it }
            getInt(EXTRA_HEADER_TEXT_COLOR, -1).let { headerTextColor = it }
            getString(EXTRA_HEADER_DATE_FORMAT, "").let { headerDateFormat = it }
            getBoolean(EXTRA_SHOW_YEAR, true).let { showYear = it }
            getString(EXTRA_CANCEL_TEXT, "").let { cancelText = it }
            getString(EXTRA_CONFIRM_TEXT, "").let { confirmText = it }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putSerializable(EXTRA_START_DATE, startDate)
            putSerializable(EXTRA_END_DATE, endDate)
            putSerializable(EXTRA_PRESELECTED_DATE, preselectedDate)
            putInt(EXTRA_YEAR_MODIFIER, yearModifier)
            putSerializable(EXTRA_LOCALE, locale)
            putInt(EXTRA_THEME_COLOR, themeColor)
            putInt(EXTRA_HEADER_TEXT_COLOR, headerTextColor)
            putString(EXTRA_HEADER_DATE_FORMAT, headerDateFormat)
            putBoolean(EXTRA_SHOW_YEAR, showYear)
            putString(EXTRA_CANCEL_TEXT, cancelText)
            putString(EXTRA_CONFIRM_TEXT, confirmText)
        }

        super.onSaveInstanceState(outState)

    }

    private fun restoreArgument(bundle: Bundle?) {
        bundle?.run {
            getSerializable(EXTRA_START_DATE)?.let { startDate = it as Calendar }
            getSerializable(EXTRA_END_DATE)?.let { endDate = it as Calendar }
            getSerializable(EXTRA_PRESELECTED_DATE)?.let { preselectedDate = it as Calendar }
            getInt(EXTRA_YEAR_MODIFIER).let { yearModifier = it }
            getSerializable(EXTRA_LOCALE)?.let { locale = it as Locale }
            getInt(EXTRA_THEME_COLOR, -1).let { themeColor = it }
            getInt(EXTRA_HEADER_TEXT_COLOR, -1).let { headerTextColor = it }
            getString(EXTRA_HEADER_DATE_FORMAT, "").let { headerDateFormat = it }
            getBoolean(EXTRA_SHOW_YEAR, true).let { showYear = it }
            getString(EXTRA_CANCEL_TEXT, "").let { cancelText = it }
            getString(EXTRA_CONFIRM_TEXT, "").let { confirmText = it }
        }
    }

    private fun initialize() {
        dayLayoutManager = LinearLayoutManager(context)
        monthLayoutManager = LinearLayoutManager(context)
        yearLayoutManager = LinearLayoutManager(context)

        recyclerViewDay.layoutManager = dayLayoutManager
        recyclerViewDay.adapter = dayAdapter
        recyclerViewDay.addOnScrollListener(dayScrollListener)
        recyclerViewDay.onFlingListener = null
        daySnapHelper.attachToRecyclerView(recyclerViewDay)

        recyclerViewMonth.layoutManager = monthLayoutManager
        recyclerViewMonth.adapter = monthAdapter
        recyclerViewMonth.addOnScrollListener(monthScrollListener)
        recyclerViewMonth.onFlingListener = null
        monthSnapHelper.attachToRecyclerView(recyclerViewMonth)

        recyclerViewYear.layoutManager = yearLayoutManager
        recyclerViewYear.adapter = yearAdapter
        recyclerViewYear.addOnScrollListener(yearScrollListener)
        recyclerViewYear.onFlingListener = null
        yearSnapHelper.attachToRecyclerView(recyclerViewYear)

        btnConfirm.setOnClickListener {
            onDialogConfirm()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        setConfiguration()
    }

    private fun setConfiguration() {

        if (themeColor != -1) {
            topContainer.setBackgroundColor(themeColor)
            btnConfirm.setTextColor(themeColor)
            btnCancel.setTextColor(themeColor)
        }

        if (!showYear) {
            tvYear.visibility = View.GONE
        }

        if (headerDateFormat.isEmpty()) {
            headerDateFormat = "EEE, MMM dd"
        }

        if (cancelText.isNotBlank()) {
            btnCancel.text = cancelText
        }

        if (confirmText.isNotBlank()) {
            btnConfirm.text = confirmText
        }

        monthAdapter.locale = locale
        yearAdapter.yearModifier = yearModifier
    }

    private fun onDialogConfirm() {
        val day = viewModel.getCurrentDay().value!!
        val month = viewModel.getCurrentMonth().value!!
        val year = viewModel.getCurrentYear().value!!
        val calendar = viewModel.getCalendar().value!!
        getListener()?.onPositiveClick(day, month, year, calendar)
        dismiss()
    }

    private fun observe() {
        viewModel.getDays().observe(this, Observer {
            dayAdapter.data = it
            dayAdapter.notifyDataSetChanged()
            viewModel.getCurrentDay().value?.let { day ->
                val currentDayPosition = dayAdapter.getPositionByValue(day)
                recyclerViewDay.alpha = 0f
                recyclerViewDay.animate().alpha(1f).apply {
                    duration = 200
                }.start()
                recyclerViewDay.scrollToPosition(currentDayPosition)
                recyclerViewDay.smoothScrollToPosition(currentDayPosition)
            }
        })

        viewModel.getMonths().observe(this, Observer {
            monthAdapter.data = it
            monthAdapter.notifyDataSetChanged()
            viewModel.getCurrentMonth().value?.let { month ->
                val currentMonthPosition = monthAdapter.getPositionByValue(month)
                recyclerViewMonth.alpha = 0f
                recyclerViewMonth.animate().alpha(1f).apply {
                    duration = 200
                }.start()
                recyclerViewMonth.scrollToPosition(currentMonthPosition)
                recyclerViewMonth.smoothScrollToPosition(currentMonthPosition)
            }
        })

        viewModel.getYears().observe(this, Observer {
            yearAdapter.data = it
            yearAdapter.notifyDataSetChanged()
            viewModel.getCurrentYear().value?.let { year ->
                val currentYearPosition = yearAdapter.getPositionByValue(year)
                recyclerViewYear.scrollToPosition(currentYearPosition)
                recyclerViewYear.smoothScrollToPosition(currentYearPosition)
            }
        })

        viewModel.getCalendar().observe(this, Observer {
            tvDate.text = SimpleDateFormat(headerDateFormat, locale).format(it.time)
        })

        viewModel.getCurrentYear().observe(this, Observer {
            tvYear.text = (it + yearModifier).toString()
        })
    }

    private val dayScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            if (newState == RecyclerView.SCROLL_STATE_IDLE && dayLastScrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                val snappedView = daySnapHelper.findSnapView(dayLayoutManager)
                snappedView?.let { view ->
                    val day = dayAdapter.getValueByPosition(dayLayoutManager.getPosition(view))
                    viewModel.setDay(day)
                }
            }

            dayLastScrollState = newState
        }
    }

    private val monthScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            if (newState == RecyclerView.SCROLL_STATE_IDLE && monthLastScrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                val snappedView = monthSnapHelper.findSnapView(monthLayoutManager)
                snappedView?.let { view ->
                    val month = monthAdapter.getValueByPosition(monthLayoutManager.getPosition(view))
                    viewModel.setMonth(month)
                }
            }

            monthLastScrollState = newState
        }
    }

    private val yearScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            if (newState == RecyclerView.SCROLL_STATE_IDLE && yearLastScrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                val snappedView = yearSnapHelper.findSnapView(yearLayoutManager)
                snappedView?.let { view ->
                    val year = yearAdapter.getValueByPosition(yearLayoutManager.getPosition(view))
                    viewModel.setYear(year)
                }
            }

            yearLastScrollState = newState
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerViewDay.removeOnScrollListener(dayScrollListener)
        recyclerViewMonth.removeOnScrollListener(monthScrollListener)
        recyclerViewYear.removeOnScrollListener(yearScrollListener)
    }

    private fun getListener(): DatePickerDialogFragmentCallback? {
        return when {
            parentFragment != null -> (parentFragment as? DatePickerDialogFragmentCallback)
            activity != null -> (activity as? DatePickerDialogFragmentCallback)
            else -> null
        }
    }

}
