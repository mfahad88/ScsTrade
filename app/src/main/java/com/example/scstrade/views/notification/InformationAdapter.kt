package com.example.scstrade.views.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemInformationBinding
import com.example.scstrade.model.data.KeyDescValue

class InformationAdapter(private val context: Context, private val list: List<KeyDescValue>): BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): KeyDescValue {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemInformationBinding = if (convertView == null) {
            ItemInformationBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemInformationBinding.bind(convertView)
        }
        val  keyDescValue = getItem(position)
        binding.apply {
            text1.text = keyDescValue.key
            text2.text = keyDescValue.desc
        }
        return binding.root
    }

}