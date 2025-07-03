package com.example.scstrade.views.snapshot.adapter

import android.graphics.Typeface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
            mcbimFunds.text = item.company_code?.trim() ?: ""
            mar202512.text = if(!TextUtils.isEmpty(item.Announcement_Date?.toString())) formatDotNetDate(item.Announcement_Date?.toString() ?: "") else formatDotNetDate(item.Meeting_Date)
       /*     if(TextUtils.isEmpty(item.Announcement_Date?.toString())){
                mar202512.visibility = View.GONE
            }else{
                mar202512.visibility = View.VISIBLE
            }*/

            crescentSt.text = item.name
            // Set short description
            boardMeeti.text = item.Discription ?: ""

            // Control tags visibility based on announcement type
            boardMeetings.visibility = if (item.AnnouncementType?.contains("Board", true) == true) View.VISIBLE else View.GONE
            finanicalResults.visibility = if (item.AnnouncementType?.contains("Result", true) == true) View.VISIBLE else View.GONE
            materialInformation.visibility = if (item.AnnouncementType?.contains("Material", true) == true) View.VISIBLE else View.GONE
            executiveDisclosures.visibility = if (item.AnnouncementType?.contains("Board", true) != true && item.AnnouncementType?.contains("Result", true) != true && item.AnnouncementType?.contains("Material", true) != true) View.VISIBLE else View.GONE

            labelText.text = item.AnnouncementType
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
                "company_code", "Announcement_Date", "Discription", "AnnouncementType","bm_PDFLink","bm_ImageLink"
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

                        val keyTv = TextView(root.context).apply {
                            text = prop.name.replace("_", " ") + ": "
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
}
