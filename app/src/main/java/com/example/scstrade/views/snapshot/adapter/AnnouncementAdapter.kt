package com.example.scstrade.views.snapshot.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemAnnouncementBinding
import com.example.scstrade.model.response.announcement.AnnouncementDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.reflect.full.memberProperties

class AnnouncementAdapter(
    private val onEyeClick: (AnnouncementDataItem) -> Unit,
    private val onDownloadClick: (AnnouncementDataItem) -> Unit,
    private val onShareClick: (AnnouncementDataItem) -> Unit
) : RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {

    private val items = mutableListOf<AnnouncementDataItem>()

    fun setData(newItems: List<AnnouncementDataItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val binding = ItemAnnouncementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class AnnouncementViewHolder(
        private val binding: ItemAnnouncementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnnouncementDataItem) = with(binding) {

            // Set company title and date
            mcbimFunds.text = item.companyCode?.trim() ?: ""
            mar202512.text = if(!TextUtils.isEmpty(item.announcementDate?.toString())) "Announcement Date: ${formatDotNetDate(item.announcementDate?.toString() ?: "")}" else "Meeting Date: ${formatDotNetDate(item.meetingDate)}"
       /*     if(TextUtils.isEmpty(item.Announcement_Date?.toString())){
                mar202512.visibility = View.GONE
            }else{
                mar202512.visibility = View.VISIBLE
            }*/

            crescentSt.text = item.name
            // Set short description
            boardMeeti.text = item.discription ?: ""

            // Control tags visibility based on announcement type
            boardMeetings.setCardBackgroundColor(getTypeBackgroundColor(binding.root.context,item.discription))
            labelText.setTextColor(getTypeFontColor(binding.root.context,item.discription))

            labelText.text = item.announcementType
            // Handle click actions
            eye.setOnClickListener { onEyeClick(item) }
            download.setOnClickListener { onDownloadClick(item) }
            share.setOnClickListener { onShareClick(item) }

            // Optional: expand/collapse detail section
            var isExpanded = false
            moreDetail.setOnClickListener {
                isExpanded = !isExpanded
                linearDetail.visibility = if (isExpanded) View.VISIBLE else View.GONE
                dropdown.rotation = if (isExpanded) 180f else 0f
            }

            linearDetail.removeAllViews()

            // Keys we ALREADY displayed so we must skip them
            val staticKeys = setOf(
                "companyCode", "announcementDate", "discription", "announcementType","imageLink","pDFLink"
            )

            // Kotlin reflection gives us all properties
            AnnouncementDataItem::class.memberProperties.forEach { prop ->
                if (prop.name !in staticKeys) {
                    val rawValue = prop.get(item)
                    val valueStr = when {
                        prop.name.contains("date", ignoreCase = true) -> formatDotNetDate(rawValue?.toString())
                        rawValue is String -> rawValue
                        rawValue is Number -> rawValue.toString()
                        else -> rawValue?.toString()
                    } ?: ""                      // null‑safe

                    if (valueStr.isNotBlank() && valueStr != "null") {

                        // ------------- build one horizontal row -------------
                        val row = LinearLayout(root.context).apply {
                            orientation = LinearLayout.HORIZONTAL
                        }

                        val keyTv = TextView(ContextThemeWrapper(root.context, R.style.engineering)).apply {
                            text =  prop.name
                                .replace("_", " ")                                  // snake_case to words
                                .replace(Regex("(?<=[a-z])(?=[A-Z])"), " ")         // camelCase to words
                                .replaceFirstChar { it.uppercaseChar() }  + ": "
                            setTypeface(typeface, Typeface.BOLD)
                        }
                        val valueTv = TextView(root.context).apply {
                            text = valueStr
                        }
                        row.addView(keyTv)
                        row.addView(valueTv)

                        // add to parent container
                        linearDetail.addView(row)
                    }
                }
            }
        }
    }

    private fun formatDotNetDate(raw: Any?): String {
        val str = raw?.toString() ?: return ""
        val ms  = Regex("""/Date\((\d+)\)/""").find(str)?.groupValues?.get(1)?.toLongOrNull()
        return if (ms != null) {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(ms))
        } else str
    }

    fun getTypeBackgroundColor(context: Context, type: String): Int {
        return when (type) {
            "Board Meetings" -> ContextCompat.getColor(context, R.color.type_board_meeting_bg)
            "Material Information" -> ContextCompat.getColor(context, R.color.type_material_info_bg)
            "Financial Result" -> ContextCompat.getColor(context, R.color.type_financial_result_bg)
            "Executive Disclosures" -> ContextCompat.getColor(context, R.color.type_exec_disclosure_bg)
            "Shareholder Meetings" -> ContextCompat.getColor(context, R.color.type_shareholder_meeting_bg)
            "Insider Transactions" -> ContextCompat.getColor(context, R.color.type_insider_tx_bg)
            "Payout" -> ContextCompat.getColor(context, R.color.type_payout_bg)
            "Other" -> ContextCompat.getColor(context, R.color.type_other_bg)
            else -> ContextCompat.getColor(context, R.color.type_default_bg)
        }
    }

    fun getTypeFontColor(context: Context, type: String): Int {
        return when (type) {
            "Board Meetings" -> ContextCompat.getColor(context, R.color.type_board_meeting_fg)
            "Material Information" -> ContextCompat.getColor(context, R.color.type_material_info_fg)
            "Financial Result" -> ContextCompat.getColor(context, R.color.type_financial_result_fg)
            "Shareholder Meetings" -> ContextCompat.getColor(context, R.color.type_shareholder_meeting_fg)
            "Insider Transactions" -> ContextCompat.getColor(context, R.color.type_insider_tx_fg)
            "Payout" -> ContextCompat.getColor(context, R.color.type_payout_fg)
            "Other" -> ContextCompat.getColor(context, R.color.type_other_fg)
            else -> ContextCompat.getColor(context, R.color.type_default_fg)
        }
    }
}
