package com.example.scstrade.views.reports

import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemReportBinding
import com.example.scstrade.model.response.researchreport.ResearchReportItem
import java.util.Date
import java.util.Locale

class ResearchReportAdapter(
    private val reports: List<ResearchReportItem>,
    private val onItemClick: (ResearchReportItem) -> Unit
) : RecyclerView.Adapter<ResearchReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReportBinding.inflate(inflater, parent, false)
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(reports[position])
    }

    override fun getItemCount(): Int = reports.size

    inner class ReportViewHolder(private val binding: ItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ResearchReportItem) {
            // File Title
            if (!item.fileTitle.isNullOrBlank()) {
                binding.boardMeeti.text = item.fileTitle
                binding.boardMeeti.visibility = View.VISIBLE
            } else {
                binding.boardMeeti.visibility = View.GONE
            }

            // Company Name
            if (!item.companyName.isNullOrBlank()) {
                binding.mcbimFunds.text = item.companyCode
                binding.mcbimFunds.visibility = View.VISIBLE
            } else {
                binding.mcbimFunds.visibility = View.GONE
            }

            // Company Code
            if (!item.companyCode.isNullOrBlank()) {
                binding.crescentSt.text = item.companyName
                binding.crescentSt.visibility = View.VISIBLE
            } else {
                binding.crescentSt.visibility = View.GONE
            }

            // Report Type (Label Text inside card)
            if (!item.reportType.isNullOrBlank()) {
                binding.labelText.text = item.reportType
                binding.boardMeetings.visibility = View.VISIBLE
            } else {
                binding.boardMeetings.visibility = View.GONE
            }

            // File Date
            val rawDate = formatDate(item.fileDate)
            val label = "Date: "
            val fullText = "$label$rawDate"

            if (rawDate.isNotBlank()) {
                val spannable = SpannableStringBuilder(fullText)
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    label.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.mar202512.text = spannable
                binding.mar202512.visibility = View.VISIBLE
            } else {
                binding.mar202512.visibility = View.GONE
            }

            // Set item click listener
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }

        private fun formatDate(dateString: String?): String {
            return try {
                val timestamp = dateString
                    ?.replace("/Date(", "")?.replace(")/", "")?.toLongOrNull()
                timestamp?.let {
                    val date = Date(it)
                    SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(date)
                } ?: ""
            } catch (e: Exception) {
                ""
            }
        }
    }
}