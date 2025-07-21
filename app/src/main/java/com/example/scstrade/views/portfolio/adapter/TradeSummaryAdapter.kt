package com.example.scstrade.views.portfolio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemMyPortfolioHistoryBinding
import com.example.scstrade.model.response.portfolio.CloseTrade
import com.example.scstrade.model.response.portfolio.TradeSummary

class TradeSummaryAdapter(private val summaries: List<TradeSummary>,private val onItemClick: (TradeSummary) -> Unit) :
    RecyclerView.Adapter<TradeSummaryAdapter.SummaryViewHolder>() {



    inner class SummaryViewHolder(val binding: ItemMyPortfolioHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMyPortfolioHistoryBinding.inflate(inflater, parent, false)
        return SummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val item = summaries[position]
        val context = holder.binding.root.context
        holder.binding.root.setOnClickListener {
            onItemClick(item)
        }
        val status = item.status // "Loss" or "Gain"
        val color = if (status == "Loss")
            ContextCompat.getColor(context, R.color.md_theme_error)
        else
            ContextCompat.getColor(context, R.color.md_theme_primary)

        val summaryText = "You made a total $status of ${item.amount} with a " +
                (if (status == "Loss") "decrease" else "increase") +
                " of ${item.percent}%"

        val spannable = android.text.SpannableString(summaryText)

        // Highlight the status word ("Loss"/"Gain")
        val statusStart = summaryText.indexOf(status)
        val statusEnd = statusStart + status.length

        // Highlight the amount value
        val amountStart = summaryText.indexOf(item.amount)
        val amountEnd = amountStart + item.amount.length

        // Highlight the percent value
        val percentStart = summaryText.indexOf(item.percent)
        val percentEnd = percentStart + item.percent.length + 1 // +1 for '%'

        // Apply color spans
        spannable.setSpan(
            android.text.style.ForegroundColorSpan(color),
            statusStart, statusEnd,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            android.text.style.ForegroundColorSpan(color),
            amountStart, amountEnd,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            android.text.style.ForegroundColorSpan(color),
            percentStart, percentEnd,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set styled text
        holder.binding.tvSymbol.text = item.symbol
        holder.binding.tvSummary.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    override fun getItemCount(): Int = summaries.size
}

