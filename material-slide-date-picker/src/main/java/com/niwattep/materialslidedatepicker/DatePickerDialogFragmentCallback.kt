package com.niwattep.materialslidedatepicker

import java.util.*

interface DatePickerDialogFragmentCallback {
    fun onPositiveClick(day: Int, month: Int, year: Int, calendar: Calendar)
}