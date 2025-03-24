package com.example.mycalendar_sdk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(
    private var daysList: List<Date>,
    private val onDateClick: (Date) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    private var selectedPosition: Int = -1
    private val dateFormat = SimpleDateFormat("d", Locale.getDefault())

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDay: TextView = itemView.findViewById(R.id.tvDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = daysList[position]
        holder.tvDay.text = dateFormat.format(date)
        holder.itemView.isSelected = (position == selectedPosition)
        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            onDateClick(date)
        }
    }

    override fun getItemCount(): Int = daysList.size

    fun updateData(newDaysList: List<Date>) {
        daysList = newDaysList
        selectedPosition = -1
        notifyDataSetChanged()
    }
}
