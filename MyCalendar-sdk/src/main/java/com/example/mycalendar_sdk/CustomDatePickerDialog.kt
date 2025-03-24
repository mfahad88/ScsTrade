package com.example.mycalendar_sdk

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CustomDatePickerDialog(private val onDateSelected: (String) -> Unit) : DialogFragment() {
    private lateinit var tvMonthYear: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private var calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private var selectedDate: Date? = null

    private val handler = Handler(Looper.getMainLooper()) // For long press
    private var isLongPressing = false // Long press flag

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_custom_date_picker)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // Makes corners visible
        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * 0.9).toInt()
        dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        val view =dialog
     /*   val builder = android.app.AlertDialog.Builder(requireContext())
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_custom_date_picker, null)
        builder.setView(view)*/

        tvMonthYear = view.findViewById(R.id.tvMonthYear)
        val btnPrevMonth = view.findViewById<Button>(R.id.btnPrevMonth)
        val btnNextMonth = view.findViewById<Button>(R.id.btnNextMonth)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnOk = view.findViewById<Button>(R.id.btnOk)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)

        calendarAdapter = CalendarAdapter(generateDaysForMonth()) { date ->
            selectedDate = date
        }

        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.adapter = calendarAdapter

        updateMonthYear()

        setupLongPress(btnPrevMonth, -1) // Move back on long press
        setupLongPress(btnNextMonth, 1)  // Move forward on long press

        btnCancel.setOnClickListener { dismiss() }

        btnOk.setOnClickListener {
            selectedDate?.let {
                onDateSelected(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it))
            }
            dismiss()
        }

        return dialog
    }

    private fun setupLongPress(button: Button, monthChange: Int) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isLongPressing = true
                    updateMonth(monthChange)
                    startRepeatingTask(monthChange)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isLongPressing = false
                    handler.removeCallbacksAndMessages(null) // Stop repeating
                }
            }
            true
        }
    }

    private fun startRepeatingTask(monthChange: Int) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isLongPressing) {
                    updateMonth(monthChange)
                    handler.postDelayed(this, 200)
                }
            }
        }, 500) // Initial delay before rapid change
    }

    private fun updateMonth(monthChange: Int) {
        calendar.add(Calendar.MONTH, monthChange)
        calendarAdapter.updateData(generateDaysForMonth())
        updateMonthYear()
    }

    private fun updateMonthYear() {
        tvMonthYear.text = dateFormat.format(calendar.time)
    }

    private fun generateDaysForMonth(): List<Date> {
        val daysList = mutableListOf<Date>()
        val tempCalendar = calendar.clone() as Calendar
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1)
        val maxDays = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..maxDays) {
            tempCalendar.set(Calendar.DAY_OF_MONTH, i)
            daysList.add(tempCalendar.time)
        }

        return daysList
    }
}
