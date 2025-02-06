package com.example.scstrade.views.register

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.ItemIndicesCardBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.ChartViewModel
import com.github.mikephil.charting.data.Entry

class IndexAdapter(
    private var list: List<KSEIndices>,
):RecyclerView.Adapter<IndexAdapter.IndexViewHolder>() {

    inner class  IndexViewHolder(private val binding: ItemIndicesCardBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(kseIndices: KSEIndices){
            binding.apply {
                kse100.text = kseIndices.iNDEXCODE
                tradingValue.text = Utils.convertToMillions(kseIndices.vALUETRADED.toDouble())
                netChange.text = kseIndices.nETCHANGE
                volume.text = "MVol: ${Utils.convertToMillions(kseIndices.vOLUMETRADED.toDouble())}"
                populateChart(kseIndices.charts)

            }
        }

        private fun populateChart(data: List<ChartItem>?) {
           try{
               val entries = data?.map {
                   Entry(Utils.convertDate(it.tradingDate).time/100f,it.tradingClose.toFloat())
               }
               binding.lineChart.setEntries(entries)

               binding.lineChart.setLineColor(R.color.md_theme_primary)
               binding.lineChart.removeGrid()
               binding.lineChart.removeAxisValues()
               binding.lineChart.removeAxisTitles()

               // Optionally, animate the chart
               binding.lineChart.animateXY(1000, 1000)
           }catch (e:Exception){
               e.printStackTrace()
           }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexViewHolder {
        val binding=ItemIndicesCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  IndexViewHolder(binding = binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: IndexViewHolder, position: Int) {
        val kseIndices=list[position]
        holder.bind(kseIndices)
    }

    fun addItems(list:List<KSEIndices>){
        this.list=list
        notifyDataSetChanged()
    }

}