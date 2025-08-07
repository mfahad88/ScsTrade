package com.example.scstrade.views.register

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemIndicesCardBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.summary.KSEIndices
import com.github.mikephil.charting.data.Entry

class IndexAdapter : ListAdapter<KSEIndices, IndexAdapter.IndexViewHolder>(KSEIndicesDiffCallback()) {

    private var chartMap: Map<String, List<ChartItem>> = emptyMap()

    inner class IndexViewHolder(private val binding: ItemIndicesCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(kseIndices: KSEIndices) {
            binding.apply {
                ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
                    val padding = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    view.setPadding(padding.left, padding.top, padding.right, padding.bottom)
                    insets
                }

                val netChangeDouble = kseIndices.nETCHANGE?.toDoubleOrNull() ?: 0.0
                val preCloseDouble = kseIndices.preClose ?: 1.0
                val percentChange = Utils.formatDouble((netChangeDouble / preCloseDouble) * 100)
                val netChangeFormatted = "${if (netChangeDouble < 0.0) "" else "+"}${Utils.formatDouble(netChangeDouble)}"

                kse100.text = kseIndices.iNDEXCODE ?: ""
                tradingValue.text = Utils.convertToMillions(kseIndices.cURRENTINDEX?.toDoubleOrNull() ?: 0.0)
                volume.text = "MVol: ${Utils.convertToMillions(kseIndices.vOLUMETRADED?.toDoubleOrNull() ?: 0.0)}"
                netChange.text = "$percentChange % $netChangeFormatted"

                if (netChangeDouble < 0.0) {
                    marketDown.visibility = View.VISIBLE
                    marketUp.visibility = View.GONE
                    imageView3.rotation = 180f
                    imageView3.setColorFilter(ContextCompat.getColor(binding.root.context,R.color.md_theme_error))
                    relativeLayout.setBackgroundResource(R.drawable.red_chip)
                } else {
                    marketDown.visibility = View.GONE
                    marketUp.visibility = View.VISIBLE
                    relativeLayout.setBackgroundResource(R.drawable.green_chip)
                    imageView3.setColorFilter(ContextCompat.getColor(binding.root.context,R.color.md_theme_primary))
                    imageView3.rotation = 0f
                }

                populateChart(kseIndices.iNDEXCODE ?: "")
            }
        }

        private fun populateChart(indexCode: String) {
            try {
                val cleanedKey = indexCode
                    .lowercase()
                    .replace("-", " ")
                    .replace("_", " ")
                    .replace("index", "")
                    .replace("share", "")
                    .trim()

                // Try best match from chartMap keys
                val matchedEntry = chartMap.entries.firstOrNull { (key, _) ->
                    cleanedKey.contains(key.lowercase())
                }

                val matchedKey = matchedEntry?.key
                val chartList = matchedEntry?.value ?: emptyList()



                val entries = chartList.reversed().mapNotNull { it.tradingHigh?.toFloat() }
                    .mapIndexed { index, high -> Entry(index.toFloat(), high) }

                binding.lineChart.setEntries(entries,0f, false, false)
                Log.d("ChartDebug", "IndexCode: \"$indexCode\" → Cleaned: \"$cleanedKey\" → Matched Key: \"$matchedKey\" → Entries: $entries")
                binding.lineChart.moveViewToX(entries.size.toFloat())
                binding.lineChart.xAxis.setDrawLabels(false)
//                binding.lineChart.invalidate()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ChartDebug", "Chart generation failed for \"$indexCode\": ${e.message}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexViewHolder {
        val binding = ItemIndicesCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IndexViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndexViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun addItems(
        list: List<KSEIndices>,
        chartMap: Map<String, List<ChartItem>>
    ) {
        this.chartMap = chartMap
        submitList(list.toList())
    }
}

class KSEIndicesDiffCallback : DiffUtil.ItemCallback<KSEIndices>() {
    override fun areItemsTheSame(oldItem: KSEIndices, newItem: KSEIndices): Boolean {
        return oldItem.iNDEXCODE == newItem.iNDEXCODE
    }

    override fun areContentsTheSame(oldItem: KSEIndices, newItem: KSEIndices): Boolean {
        return oldItem == newItem
    }
}
