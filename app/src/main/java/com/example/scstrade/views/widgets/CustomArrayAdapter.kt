package com.example.scstrade.views.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.scstrade.R

class CustomArrayAdapter(
    private val context: Context,
    private val items: List<String>
) : ArrayAdapter<String>(context,0,items){
    override fun getCount(): Int =items.size

    override fun getItem(p0: Int) = items[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dropdown, parent, false)
        val text1 = view.findViewById<TextView>(R.id.text1)
        text1.text = items[position]
        return view
    }
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dropdown, parent, false)
        val text1 = view.findViewById<TextView>(R.id.text1)
        text1.text = items[position]
        return view
    }

}