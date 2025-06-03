package com.example.mycalendar_sdk

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

class CalendarAdapter(
    private var daysList: List<Date>,
    private var available:HashSet<String>,
    private val onDateClick: (Date) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    private var selectedPosition: Int = -1
    private val dateFormat = SimpleDateFormat("d", Locale.getDefault())

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        val isAvailable:ImageView = itemView.findViewById(R.id.isAvailable)

        public fun bind(date:Date){
            isAvailable.visibility = View.INVISIBLE
            if(available.contains(SimpleDateFormat("dd MMM yyyy").format(date))){
//                Log.e("Dates","Matched: "+SimpleDateFormat("dd/MM/yyyy").format(date))
                isAvailable.visibility = View.VISIBLE
            }
            /*available.forEach {

                if(compareDates(it,date.time)){
                    Log.e("Dates","Matched: "+SimpleDateFormat("dd/MM/yyyy").format(date))
                    isAvailable.visibility = View.VISIBLE
                }
            }*/
            tvDay.text = dateFormat.format(date)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = daysList[position]
        holder.apply {
            bind(date)
            itemView.isSelected = (position == selectedPosition)
            itemView.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                onDateClick(date)
            }
        }


    }

    override fun getItemCount(): Int = daysList.size

    fun updateData(newDaysList: List<Date>,newAvailableDates:HashSet<String>) {
        daysList = newDaysList
        available = newAvailableDates
        selectedPosition = -1
        notifyDataSetChanged()
    }

    fun compareDates(date1:String,date2:Long): Boolean {
        try {
            val timestamp1 = Date(date1).time
            val sdf=SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
            val timestamp2 = date2
            return if(sdf.format(Date(timestamp1)).compareTo(sdf.format(Date(timestamp2?:0L)))!=0){
                false
            }else {
                true
            }
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }
    }

    fun convertDateString(dateString: String,format: String): String {
        // Extract the timestamp value from the string
        val timestamp = dateString.replace(Regex("[^0-9]"), "").toLong()

        // Convert to Date
        val date = Date(timestamp)

        // Format the date to "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(date)
    }
}
