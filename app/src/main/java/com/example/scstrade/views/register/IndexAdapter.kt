package com.example.scstrade.views.register
import androidx.compose.ui.res.dimensionResource

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    var chartItems = mutableListOf<ChartItem>()
    var chartItems1 = mutableListOf<ChartItem>()
    var chartItems2 = mutableListOf<ChartItem>()
    var chartItems3 = mutableListOf<ChartItem>()

    inner class IndexViewHolder(private val binding: ItemIndicesCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(kseIndices: KSEIndices) {
            binding.apply {
                val percentChange = Utils.formatDouble(
                    kseIndices.nETCHANGE.toDouble()
                        .div(kseIndices.preClose.toDouble())
                        .times(100)
                )
                val netChangeFormatted =
                    "${if (kseIndices.nETCHANGE.toDouble() < 0.0) "" else "+"} ${
                        Utils.formatDouble(kseIndices.nETCHANGE.toDouble())
                    }"

                kse100.text = kseIndices.iNDEXCODE

                tradingValue.text = if (kseIndices.cURRENTINDEX != "")
                    Utils.convertToMillions(kseIndices.cURRENTINDEX.toDouble())
                else
                    0.0.toString()

                netChange.text = "$percentChange % $netChangeFormatted"

                volume.text = "MVol: ${
                    if (kseIndices.vOLUMETRADED != "")
                        Utils.convertToMillions(kseIndices.vOLUMETRADED.toDouble())
                    else
                        0.0.toString()
                }"

                if (kseIndices.nETCHANGE.toDouble() < 0.0) {
                    marketDown.visibility = View.VISIBLE
                    marketUp.visibility = View.GONE
                    relativeLayout.setBackgroundResource(R.drawable.red_chip)
                } else {
                    marketDown.visibility = View.GONE
                    marketUp.visibility = View.VISIBLE
                    relativeLayout.setBackgroundResource(R.drawable.green_chip)
                }

                populateChart(kseIndices.iNDEXCODE)
            }
        }

        private fun populateChart(data: String) {
            try {
                var interval = 0
                val entries = when {
                    data.contains("kse all", ignoreCase = true) -> chartItems
                    data.contains("kse 100", ignoreCase = true) -> chartItems1
                    data.contains("kse 30", ignoreCase = true) -> chartItems2
                    data.contains("kmi 30", ignoreCase = true) -> chartItems3
                    else -> emptyList()
                }.map {
                    interval += 1
                    Entry(interval.toFloat(), it.tradingHigh.toFloat())
                }
                binding.lineChart.setEntries(entries, false, false)
                binding.lineChart.moveViewToX(interval.toFloat())
                binding.lineChart.xAxis.setDrawLabels(false)
            } catch (e: Exception) {
                e.printStackTrace()
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
        chartItems: List<ChartItem>,
        chartItems1: List<ChartItem>,
        chartItems2: List<ChartItem>,
        chartItems3: List<ChartItem>
    ) {
        this.chartItems = chartItems.toMutableList()
        this.chartItems1 = chartItems1.toMutableList()
        this.chartItems2 = chartItems2.toMutableList()
        this.chartItems3 = chartItems3.toMutableList()
        submitList(list)
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
