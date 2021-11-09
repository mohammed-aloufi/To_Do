package com.example.todo.new_to_do

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.todo.CRIME_DATE_KEY
import java.util.*

class DatePickerDialogFragment: DialogFragment() {

    interface DatePickerCallBack{
        fun onDateSelected(date: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val date = arguments?.getSerializable(CRIME_DATE_KEY) as Date
        val calender = Calendar.getInstance()
        calender.time = date

        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

            val resultDate = GregorianCalendar(year, month, dayOfMonth).time
            targetFragment?.let {
                (it as DatePickerCallBack).onDateSelected(resultDate)
            }
        }

        return DatePickerDialog(
            requireContext(),
            dateListener,
            year,
            month,
            day)
    }
}