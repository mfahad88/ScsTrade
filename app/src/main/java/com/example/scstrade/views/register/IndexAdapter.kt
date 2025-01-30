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
    private val viewModelChart: ChartViewModel,
    private val lifecycleOwner: LifecycleOwner
):RecyclerView.Adapter<IndexAdapter.IndexViewHolder>() {

    inner class  IndexViewHolder(private val binding: ItemIndicesCardBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(kseIndices: KSEIndices){
            binding.apply {
                kse100.text = kseIndices.iNDEXCODE
                tradingValue.text = Utils.convertToMillions(kseIndices.vALUETRADED.toDouble())
                netChange.text = kseIndices.nETCHANGE
                volume.text = "MVol: ${Utils.convertToMillions(kseIndices.vOLUMETRADED.toDouble())}"
                System.out.println("Chart: ${kseIndices.charts}")
                populateChart(kseIndices.charts)
               /* if(kseIndices.iNDEXCODE.lowercase().contains("kse all")){
                    viewModelChart.getIndexChart("kseall",1)
                    viewModelChart.mutableChart.observe(lifecycleOwner, Observer {resource->
                        when(resource){
                            is Resource.Error -> binding.loader.visibility= View.GONE
                            is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                            is Resource.Success -> {
                                binding.loader.visibility = View.GONE
                                populateChart(resource.data)

                            }
                        }
                    })
                }else if(kseIndices.iNDEXCODE.lowercase().contains("kse 100")){
                    viewModelChart.getIndexChart("kse",1)
                    viewModelChart.mutableChart.observe(lifecycleOwner, Observer {resource->
                        when(resource){
                            is Resource.Error -> binding.loader.visibility= View.GONE
                            is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                            is Resource.Success -> {
                                binding.loader.visibility = View.GONE
                                populateChart(resource.data)

                            }
                        }
                    })
                }else if(kseIndices.iNDEXCODE.lowercase().contains("kse 30")){
                    viewModelChart.getIndexChart("kse30",1)
                    viewModelChart.mutableChart.observe(lifecycleOwner, Observer {resource->
                        when(resource){
                            is Resource.Error -> binding.loader.visibility= View.GONE
                            is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                            is Resource.Success -> {
                                binding.loader.visibility = View.GONE
                                populateChart(resource.data)

                            }
                        }
                    })
                }else if(kseIndices.iNDEXCODE.lowercase().contains("kmi 30")){
                    viewModelChart.getIndexChart("kmi30",1)
                    viewModelChart.mutableChart.observe(lifecycleOwner, Observer {resource->
                        when(resource){
                            is Resource.Error -> binding.loader.visibility= View.GONE
                            is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                            is Resource.Success -> {
                                binding.loader.visibility = View.GONE
                                populateChart(resource.data)

                            }
                        }
                    })
                }*/
            }
        }

        private fun populateChart(data: List<ChartItem>?) {
           val entries = data?.map {
                Entry(Utils.convertDate(it.tradingDate).time.toFloat(),it.tradingClose.toFloat())
            }
            binding.lineChart.setEntries(entries)

            // Set custom X and Y axis labels
//            binding.lineChart.setXAxisLabels(listOf("Jan", "Feb", "Mar", "Apr"))
//            binding.lineChart.setYAxisLabels(listOf("0", "1", "2", "3"))

            // Set custom line color and value text color
            binding.lineChart.setLineColor(R.color.md_theme_primary)
//            binding.lineChart.setValueTextColor(resources.getColor(android.R.color.holo_red_dark))
            binding.lineChart.removeGrid()
            binding.lineChart.removeAxisValues()
            binding.lineChart.removeAxisTitles()
            // Initialize the chart with default settings
            binding.lineChart.invalidate() // or customLineChart.notifyDataSetChanged()

            // Optionally, animate the chart
            binding.lineChart.animateXY(1000, 1000)
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