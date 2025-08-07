package com.example.scstrade.views.analystopinion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemAnalystOpinionBinding
import com.example.scstrade.model.response.analystopinion.AnalystOpinionItem
import java.text.SimpleDateFormat
import java.util.*

class AnalystOpinionAdapter(
    private var itemList: List<AnalystOpinionItem>,
    private val onItemClick: (AnalystOpinionItem) -> Unit
) : RecyclerView.Adapter<AnalystOpinionAdapter.AnalystOpinionViewHolder>() {

    // Track which positions are expanded
    private val expandedPositions = mutableSetOf<Int>()

    inner class AnalystOpinionViewHolder(private val binding: ItemAnalystOpinionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnalystOpinionItem, position: Int) = with(binding) {
            // Split symbol and name
            val raw = item.companyName ?: ""
            val parts = raw.split(" - ", limit = 2)
            val symbol = parts.getOrNull(0)?.trim() ?: ""
            val name = parts.getOrNull(1)?.trim() ?: ""
            mcbimFunds.text = symbol
            crescentSt.text = name
            boardMeeti.text = item.aoHeading?.ifBlank { "" } ?: ""
            body.text = item.aoText?.ifBlank { "" } ?: ""
            mar202512.text = formatJsonDate(item.aoDate)
            person.text = item.person?.ifBlank { "" } ?: ""
            if(!symbol.isNullOrBlank()){
                mcbimFunds.visibility = View.VISIBLE
            }else{
                mcbimFunds.visibility = View.GONE
            }

            if(!name.isNullOrBlank()){
                crescentSt.visibility = View.VISIBLE
            }else{
                crescentSt.visibility = View.GONE
            }
            if(!item.link.isNullOrBlank()){
                source.visibility = View.VISIBLE
            }else{
                source.visibility = View.GONE
            }
            source.setOnClickListener {
                onItemClick(item)
            }
            // Handle expansion state
            val isExpanded = expandedPositions.contains(position)
            body.maxLines = if (isExpanded) Int.MAX_VALUE else 3
//            moreDetailText.text = if (isExpanded) "Less" else "More"
            dropdown.rotation = if (isExpanded) 180f else 0f

            // Toggle on click
            moreDetail.setOnClickListener {
                if (isExpanded) {
                    expandedPositions.remove(position)
                } else {
                    expandedPositions.add(position)
                }
                notifyItemChanged(position)
            }
            body.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    moreDetail.visibility = if (body.lineCount > 3) ViewGroup.VISIBLE else ViewGroup.GONE
                    body.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
            // External click listener
            /*root.setOnClickListener {

            }*/
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
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int = itemList.size

    fun submitList(newList: List<AnalystOpinionItem>) {
        itemList = newList
        expandedPositions.clear()
        notifyDataSetChanged()
    }
}
