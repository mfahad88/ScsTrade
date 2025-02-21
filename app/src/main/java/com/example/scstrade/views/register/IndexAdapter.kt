package com.example.scstrade.views.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.databinding.ItemIndicesCardBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.github.mikephil.charting.data.Entry

class IndexAdapter(
    private var list: List<KSEIndices>,
    val viewModel: SharedViewModel,
    val viewLifecycleOwner: LifecycleOwner,
):RecyclerView.Adapter<IndexAdapter.IndexViewHolder>() {

    inner class  IndexViewHolder(private val binding: ItemIndicesCardBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(kseIndices: KSEIndices){
            binding.apply {
                kse100.text = kseIndices.iNDEXCODE
                tradingValue.text = if(kseIndices.vALUETRADED!="") Utils.convertToMillions(kseIndices.vALUETRADED.toDouble()) else 0.0.toString()
                netChange.text = kseIndices.nETCHANGE
                volume.text = "MVol: ${if(kseIndices.vOLUMETRADED!="")Utils.convertToMillions(kseIndices.vOLUMETRADED.toDouble()) else 0.0.toString()}"
                populateChart(kseIndices.iNDEXCODE)

            }
        }

        private fun populateChart(data: String) {
           try{
               var interval = 0
                if(data.contains("kse all",true)){
                    viewModel.fetchChart("kseall")
                }else if(data.contains("kse 100",true)){
                    viewModel.fetchChart("kse")
                }else if(data.contains("kse 30",true)){
                    viewModel.fetchChart("kse")
                }else if(data.contains("kmi 30",true)){
                    viewModel.fetchChart("kmi30")
                }
                viewModel.mutableChart.observe(viewLifecycleOwner, Observer { result->

                    val entries = result.data?.map {
                        interval+=1
                        Entry(interval.toFloat(),it.tradingHigh.toFloat())
                    }
                    binding.lineChart.setEntries(entries)
                    binding.lineChart.notifyDataSetChanged()
                    binding.lineChart.moveViewToX(interval.toFloat())
                    binding.lineChart.xAxis.apply {
                        setDrawLabels(false)
                    }
                })

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