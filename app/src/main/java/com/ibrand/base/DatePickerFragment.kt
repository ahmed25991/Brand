package com.ibrand.base

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.ibrand.R
import com.ibrand.utils.SendSingleItemListener
import java.util.*

class DatePickerFragment(
    private val year: Int = 0,
    private val month: Int = 0,
    private val day: Int = 0,
    private val restrictPast: Boolean? = false,
    private val restrictFuture: Boolean? = false,
    private val birthdayPicker: Boolean? = false,
    private val sendItemDataListener: SendSingleItemListener<Array<Int>>? = null,
    private val context_: Context? = null
) : DialogFragment(), DatePickerDialog.OnDateSetListener ,DatePicker.OnDateChangedListener {

    @SuppressLint("NewApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val actualYear =   c.get(Calendar.YEAR)
        val actualMonth = c.get(Calendar.MONTH)
        val actualDay = c.get(Calendar.DAY_OF_MONTH)
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            context?.resources?.configuration?.locale?: Locale("en")
        }
        Locale.setDefault(locale?: Locale("en"))
        val contextToUse = context_ ?: this.requireContext()
        val datePickerDialog = DatePickerDialog(
            contextToUse,
            R.style.dobpicker,
            this,
            actualYear,
            actualMonth,
            actualDay
        )
        datePickerDialog.setOnDateSetListener(this)
        filterDatePicker(datePickerDialog.datePicker, c)
        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        this.sendItemDataListener?.sendItem(arrayOf(day, month.plus(1), year))
    }

    override fun onDateChanged(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        filterDatePicker(view, c)
    }

    private fun filterDatePicker(view: DatePicker?, c: Calendar) {
        if (this.restrictPast == true)
            view?.minDate = System.currentTimeMillis()
        if (this.restrictFuture == true)
            view?.maxDate = System.currentTimeMillis()
        if (this.birthdayPicker == true) {
            val birthdayCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, c.get(Calendar.YEAR)-18)
            }
            view?.maxDate = birthdayCalendar.time.time
            c.add(Calendar.YEAR, 0)
        }
    }
}