package com.example.scstrade.views.analystopinion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemAnalystOpinionBinding
import com.example.scstrade.model.response.analystopinion.AnalystOpinionItem
import java.text.SimpleDateFormat
import java.util.*

class AnalystOpinionAdapter(
    private var itemList: List<AnalystOpinionItem>,
    private val onItemClick: (AnalystOpinionItem) -> Unit
) : RecyclerView.Adapter<AnalystOpinionAdapter.AnalystOpinionViewHolder>() {

    inner class AnalystOpinionViewHolder(private val binding: ItemAnalystOpinionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnalystOpinionItem) {
            binding.apply {
                val raw = item.companyName ?: "-"
                val parts = raw.split(" - ", limit = 2)
                val symbol = parts.getOrNull(0)?.trim() ?: "-"
                val name = parts.getOrNull(1)?.trim() ?: "-"
                mcbimFunds.text = symbol
                crescentSt.text = name
                boardMeeti.text = if (!item.aoText.isNullOrBlank()) {
                    item.aoText
                } else {
                    item.aoHeading?.ifBlank { "-" } ?: "-"
                }
                mar202512.text = formatJsonDate(item.aoDate)
                person.text = item.person?.ifBlank { "-" } ?: "-"
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }

        private fun formatJsonDate(jsonDate: String?): String {
            return try {
                val timestamp = jsonDate?.filter { it.isDigit() }?.toLongOrNull()
                if (timestamp != null) {
                    val date = Date(timestamp)
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    sdf.format(date)
                } else {
                    "-"
                }
            } catch (e: Exception) {
                "-"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalystOpinionViewHolder {
        val binding = ItemAnalystOpinionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnalystOpinionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnalystOpinionViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    fun submitList(newList: List<AnalystOpinionItem>) {
        itemList = newList
        notifyDataSetChanged()
    }
}