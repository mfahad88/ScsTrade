package com.example.scstrade.views.snapshot

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scstrade.databinding.FragmentTechnicalBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import java.util.Locale


class TechnicalFragment : Fragment() {
   lateinit var binding: FragmentTechnicalBinding
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTechnicalBinding.inflate(inflater, container, false)
        sharedViewModel=(requireActivity().application as MyApp).viewModel
        toggleLineChart(binding.candle)
        binding.apply {
            line.setOnClickListener {
                toggleLineChart(it)
            }

            candle.setOnClickListener {
                toggleLineChart(it)
            }

            d1.setOnClickListener {
                toggleDay(it)
            }

            hr1.setOnClickListener {
                toggleDay(it)
            }

            min30.setOnClickListener {
                toggleDay(it)
            }

            min15.setOnClickListener {
                toggleDay(it)
            }
            min5.setOnClickListener {
                toggleDay(it)
            }

            min1.setOnClickListener {
                toggleDay(it)
            }


        }
//        sharedViewModel.snapshotChart((requireActivity() as SnapshotActivity).symbol)
        sharedViewModel.mutableSnapShotChart.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message)
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val stockIndex=result.data?.stockIndex
                    binding.indexStockChart.setChartData(stockIndex?.date?.filter { it!=null }?.map { convertData(it) }?.toList()?: emptyList(), stockIndex?.index?.map { it.toFloat() }?: emptyList(),
                        stockIndex?.stock?.map { it.toFloat() }?: emptyList(),"Index", (requireActivity() as SnapshotActivity).symbol)
                }
            }
        })

        sharedViewModel.mutableChart.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message)
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    var interval=0
                    val candleEntry:ArrayList<CandleEntry>?= ArrayList()
                    result.data?.reversed()?.forEachIndexed { index, it ->
                        candleEntry?.add(
                            CandleEntry(index.toFloat(), it.tradingHigh.toFloat(),it.tradingLow.toFloat(),it.tradingOpen.toFloat(),it.tradingClose.toFloat())
                        )
                    }
                    binding.candlestickChart.setCandleData(candleEntry?: emptyList())

                    binding.lineChart.setEntries(result.data?.reversed()?.map {
                        interval+=1
                        Entry(interval.toFloat(),it.tradingHigh.toFloat())
                    },false,true)

                }
            }
        })
        return binding.root
    }

    private fun toggleDay(it: View?) {
        when(it?.id){
            binding.d1.id->{
                binding.apply {
                    d1.setChipSelected(true)
                    hr1.setChipSelected(false)
                    min30.setChipSelected(false)
                    min15.setChipSelected(false)
                    min5.setChipSelected(false)
                    min1.setChipSelected(false)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1D")
                }
            }


            binding.hr1.id->{
                binding.apply {
                    d1.setChipSelected(false)
                    hr1.setChipSelected(true)
                    min30.setChipSelected(false)
                    min15.setChipSelected(false)
                    min5.setChipSelected(false)
                    min1.setChipSelected(false)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"60")
                }
            }


            binding.min30.id->{
                binding.apply {
                    d1.setChipSelected(false)
                    hr1.setChipSelected(false)
                    min30.setChipSelected(true)
                    min15.setChipSelected(false)
                    min5.setChipSelected(false)
                    min1.setChipSelected(false)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"30")
                }
            }

            binding.min15.id->{
                binding.apply {
                    d1.setChipSelected(false)
                    hr1.setChipSelected(false)
                    min30.setChipSelected(false)
                    min15.setChipSelected(true)
                    min5.setChipSelected(false)
                    min1.setChipSelected(false)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"15")
                }
            }


            binding.min5.id->{
                binding.apply {
                    d1.setChipSelected(false)
                    hr1.setChipSelected(false)
                    min30.setChipSelected(false)
                    min15.setChipSelected(false)
                    min5.setChipSelected(true)
                    min1.setChipSelected(false)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"5")
                }
            }

            binding.min1.id->{
                binding.apply {
                    d1.setChipSelected(false)
                    hr1.setChipSelected(false)
                    min30.setChipSelected(false)
                    min15.setChipSelected(false)
                    min5.setChipSelected(false)
                    min1.setChipSelected(true)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1")
                }
            }

        }

    }

    private fun toggleLineChart(view: View) {
        if(view.id==binding.line.id){
            binding.line.setChipSelected(true)
            binding.candle.setChipSelected(false)
            binding.lineChart.visibility = View.VISIBLE
            binding.candlestickChart.visibility = View.GONE
            binding.apply {
                if(d1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1D")
                }

                if(hr1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"60")
                }
                if(min30.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"30")
                }
                if(min15.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"15")
                }
                if(min5.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"5")
                }
                if(min1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1")
                }
            }

        }else{
            binding.line.setChipSelected(false)
            binding.candle.setChipSelected(true)
            binding.lineChart.visibility = View.GONE
            binding.candlestickChart.visibility = View.VISIBLE

            binding.apply {
                if(d1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1D")
                }

                if(hr1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"60")
                }
                if(min30.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"30")
                }
                if(min15.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"15")
                }
                if(min5.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"5")
                }
                if(min1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1")
                }
            }
        }

        binding.apply {
            if(!d1.isSelected && !hr1.isSelected && !min30.isSelected && !min15.isSelected && !min5.isSelected && !min1.isSelected){
                toggleDay(d1)
                sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1D")
            }
        }

    }


    fun convertData(inputDate:String?): String? {
        val input = inputDate
        val inputFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM", Locale.getDefault())

        val date = inputFormat.parse(input)
        val month = outputFormat.format(date) // → "March"
        return month
    }

}