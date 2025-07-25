package com.example.scstrade.views.notification
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.NotificationItemBinding
import com.example.scstrade.model.response.announcement.AnnouncementDataItem
import com.example.scstrade.model.response.notification.NotificationDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(private val itemList: List<NotificationDto>,private val onRowItemClick: (NotificationDto)-> Unit) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(private val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
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

        fun bind(
            item: NotificationDto,
            index: Int,
            onRowItemClick: (NotificationDto) -> Unit,
        ) {
            binding.apply {
                mcbimFunds.text = item.companyCode?.trim() ?: ""
                if(!TextUtils.isEmpty(item.mainAnnDate?.toString())) {
                    mar202512.visibility = View.VISIBLE
                    val rawDate = formatDotNetDate(item.mainAnnDate?.toString() ?: "")
                    val label = "Announcement Date: "
                    val fullText = "$label$rawDate"

                    val spannable = SpannableStringBuilder(fullText).apply {
                        setSpan(
                            StyleSpan(Typeface.BOLD),
                            0,
                            label.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }

                    mar202512.text = spannable
                }else{
                    mar202512.visibility = View.GONE
                }
                if(!TextUtils.isEmpty(item.companyName)) {
                    crescentSt.visibility = View.VISIBLE
                    crescentSt.text = item.companyName
                }else{
                    crescentSt.visibility = View.GONE
                }

                boardMeeti.text = item.mainAnnHeading
                boardMeetings.setCardBackgroundColor(getTypeBackgroundColor(binding.root.context,item.announcementTypeName?:""))
                labelText.setTextColor(getTypeFontColor(binding.root.context,item.announcementTypeName?:""))

                labelText.text = item.announcementTypeName
                var isExpanded = false
                moreDetail.setOnClickListener {
                    isExpanded = !isExpanded
                    linearDetail.visibility = if (isExpanded) View.VISIBLE else View.GONE
                    dropdown.rotation = if (isExpanded) 180f else 0f
                }

                linearDetail.removeAllViews()

                val mainAnnDetails = item.mainAnnDetails?.trim().orEmpty()
                Log.e("MainDetails", mainAnnDetails)

                if (mainAnnDetails.isNotEmpty()) {
                    moreDetail.visibility = View.VISIBLE
                    linearDetail.removeAllViews() // clear any previous content

                    if (mainAnnDetails.contains("|")) {
                        // Case: Parse and show key-value pairs
                        mainAnnDetails
                            .split("|")
                            .map { it.trim() }
                            .mapNotNull { line ->
                                val parts = line.split(":", limit = 2)
                                if (parts.size == 2) {
                                    val key = parts[0].trim()
                                    val value = parts[1].trim()
                                    if (value.isNotEmpty() && value.lowercase() != "null") {
                                        key to value
                                    } else null
                                } else null
                            }.forEach { (key, value) ->
                                val row = LinearLayout(root.context).apply {
                                    orientation = LinearLayout.HORIZONTAL
                                }

                                val keyTv = TextView(ContextThemeWrapper(root.context, R.style.engineering)).apply {
                                    text = "$key: "
                                    setTypeface(typeface, Typeface.BOLD)
                                }

                                val valueTv = TextView(root.context).apply {
                                    text = value
                                }

                                row.addView(keyTv)
                                row.addView(valueTv)
                                linearDetail.addView(row)
                            }

                    } else {
                        // Case: Show full text as a paragraph
                        val paragraphView = TextView(root.context).apply {
                            text = mainAnnDetails
                            setPadding(0, 8, 0, 8)
                        }
                        linearDetail.addView(paragraphView)
                        moreDetail.visibility = View.GONE
                    }

                } else {
                    moreDetail.visibility = View.GONE
                }
            }
            binding.root.setOnClickListener {      onRowItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(itemList[position],position,onRowItemClick)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }





}
