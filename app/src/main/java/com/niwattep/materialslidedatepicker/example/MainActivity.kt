package com.niwattep.materialslidedatepicker.example

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.niwattep.materialslidedatepicker.DatePickerDialogFragment
import com.niwattep.materialslidedatepicker.DatePickerDialogFragmentCallback
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialogFragmentCallback {

    private val btnOpen: Button by lazy { findViewById<Button>(R.id.btn_open) }
    private val btnOpenTh: Button by lazy { findViewById<Button>(R.id.btn_open_th) }
    private val tvDate: TextView by lazy { findViewById<TextView>(R.id.tv_date) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            btnOpen.setOnClickListener {
                DatePickerDialogFragment.newInstance()
                    .show(supportFragmentManager, "TAG")
            }
            btnOpenTh.setOnClickListener {
                DatePickerDialogFragment.newInstance(
                    locale = Locale("th"),
                    yearModifier = 543,
                    headerTextFormat = "EEE dd MMMM",
                    cancelText = "ยกเลิก",
                    confirmText = "ตกลง")
                    .show(supportFragmentManager, "TAG")
            }

    }

    override fun onPositiveClick(day: Int, month: Int, year: Int, calendar: Calendar) {
        tvDate.text = SimpleDateFormat("EEEE, MMM dd, yyyy").format(calendar.time)
    }
}
