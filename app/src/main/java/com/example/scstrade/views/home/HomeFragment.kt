package com.example.scstrade.views.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentHomeBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.stock.StockAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()
    private val homeViewModel:HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)

        binding.cardHome.apply {
            line.setOnClickListener {
                lineChart.visibility = View.VISIBLE
                candlestickChart.visibility = View.GONE
                min1.visibility=View.GONE
                min1.visibility=View.GONE
                min1.visibility=View.GONE
                min1.visibility=View.GONE
                min1.visibility=View.GONE
                homeViewModel.selectLine()

            }
            candle.setOnClickListener {
                lineChart.visibility = View.GONE
                candlestickChart.visibility = View.VISIBLE
                homeViewModel.selectCandle()

            }
            homeViewModel.isLineSelected.observe(viewLifecycleOwner, Observer {

                line.setChipSelected(it)
            })

            homeViewModel.isCandleSelected.observe(viewLifecycleOwner, Observer {

                candle.setChipSelected(it)
            })

            homeViewModel.timeSelected.observe(viewLifecycleOwner, Observer {
                min1.setChipSelected(it[0])
                min5.setChipSelected(it[1])
                min15.setChipSelected(it[2])
                min30.setChipSelected(it[3])
                hr1.setChipSelected(it[4])
            })
            min1.setOnClickListener {
                homeViewModel.selectTime(1)

            }
            min5.setOnClickListener {
                homeViewModel.selectTime(5)
            }

            min15.setOnClickListener {
                homeViewModel.selectTime(15)
            }

            min30.setOnClickListener {
                homeViewModel.selectTime(30)
            }

            hr1.setOnClickListener {
                homeViewModel.selectTime(60)
            }


        }


        binding.recyclerLeaders.apply {
            adapter=StockAdapter(emptyList())
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30))
            isNestedScrollingEnabled=true
        }

        binding.recyclerGainers.apply {
            adapter=StockAdapter(emptyList())
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30))
            isNestedScrollingEnabled=true
        }
        binding.recyclerLosers.apply {
            adapter=StockAdapter(emptyList())
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30))
            isNestedScrollingEnabled=true
        }
        viewModel.mutableIndices.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if(!viewModel.isLoading){

//                        initMarket(result.data)
                        if(homeViewModel.selectedIndex.value==null) {
                            initSelection(viewModel.mutablePreviousIndices.value?.filter { it.iNDEXCODE.contains("kse 100",true) }?.first(),result.data?.filter { it.iNDEXCODE.contains("kse 100",true) }?.first())
                            homeViewModel.fetchChart()
                        }else{
                            initSelection(viewModel.mutablePreviousIndices.value?.filter { it.iNDEXCODE.contains(
                                homeViewModel.selectedIndex.value!!.iNDEXCODE,true) }?.first(),result.data?.filter { it.iNDEXCODE.contains( homeViewModel.selectedIndex.value!!.iNDEXCODE,true) }?.first())
                        }
                        binding.cardHome.imageViewDropDown.setOnClickListener {

                            showDropdownMenu(binding.cardHome.imageViewDropDown,result.data,binding.cardHome.kmiallshr)
                        }
                        binding.apply {
                            main.visibility = View.VISIBLE
                            loader.visibility = View.GONE
                        }
                    }
                }
            }
        })

        viewModel.mutableAllData.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    (binding.recyclerLeaders.adapter as StockAdapter).addItems(result.data?.sortedByDescending { it.v }?.take(10)?: emptyList())
                    (binding.recyclerGainers.adapter as StockAdapter).addItems(result.data?.sortedByDescending { it.cHP }?.take(10)?: emptyList())
                    (binding.recyclerLosers.adapter as StockAdapter).addItems(result.data?.sortedBy { it.cHP }?.take(10)?: emptyList())
                }
            }
        })

        homeViewModel.chartMutable.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    var interval=0L
                    if(!result.data.isNullOrEmpty()){
                       if(homeViewModel.isCandleSelected.value == true){
                           initCandleStick( result.data?.map {
                               if(homeViewModel.timeSelected.value?.indexOf(true)==0){
                                   interval+=1
                               } else if(homeViewModel.timeSelected.value?.indexOf(true)==1){
                                   interval+=5
                               } else if(homeViewModel.timeSelected.value?.indexOf(true)==2){
                                   interval+=15
                               }else if(homeViewModel.timeSelected.value?.indexOf(true)==3){
                                   interval+=60
                               }

                               CandleEntry(interval.toFloat(),it.tradingHigh.toFloat(),it.tradingLow.toFloat(),it.tradingOpen.toFloat(),it.tradingClose.toFloat())
                           })
                       }else{


                           binding.cardHome.lineChart.entries = result.data.map {
                               interval+=1
                               Entry(interval.toFloat(),it.tradingHigh.toFloat())
                           }
                       }
                    }

                }
            }
        })



        return binding.root
    }

    private fun initCandleStick(entries: List<CandleEntry>?) {
        if(entries!=null) {
            binding.cardHome.candlestickChart.setCandleData(entries)
        }

    }

    private fun initSelection(prevkseIndices:KSEIndices?,kseIndices: KSEIndices?) {
        binding.cardHome.apply {
            kmiallshr.text=kseIndices?.iNDEXCODE
            if(kseIndices?.vALUETRADED!="" && kseIndices?.vOLUMETRADED!="" && kseIndices?.cURRENTINDEX!="" && kseIndices?.nETCHANGE!="" && kseIndices?.hIGHINDEX!="" && kseIndices?.lOWINDEX!=""){
                if(!kseIndices?.vALUETRADED.equals(prevkseIndices?.vALUETRADED)){
                    tradeValueView.text=Utils.convertToMillions(kseIndices?.vALUETRADED?.toDouble()?:0.0)
                    if(kseIndices?.nETCHANGE?.contains("-")?:false) {
                        tradeValueView.drawable =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.drop_down)
                    }else{
                        tradeValueView.drawable =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.drop_up)
                    }
                }
                if(!kseIndices?.nETCHANGE.equals(prevkseIndices?.nETCHANGE)) {
                    netChangeChip.text = "${kseIndices?.nETCHANGE} (${
                        String.format(
                            "%.2f",
                            (kseIndices?.nETCHANGE?.toDouble()
                                ?.div(kseIndices?.preClose ?: 0.0))?.times(100)
                        )
                    }%)"
                }
                if(!kseIndices?.vOLUMETRADED.equals(prevkseIndices?.vOLUMETRADED)) {
                    volumeChip.text =
                        "Volume: ${Utils.convertToMillions(kseIndices?.vOLUMETRADED?.toDouble() ?: 0.0)}"
                }
                if(!kseIndices?.hIGHINDEX.equals(prevkseIndices?.hIGHINDEX)) {
                    highView.text = "H: ${kseIndices?.hIGHINDEX} ${
                        String.format(
                            "%.2f",
                            kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose ?: 0.0)
                        )
                    } " +
                            "(${
                                String.format(
                                    "%.2f",
                                    (kseIndices?.hIGHINDEX?.toDouble()
                                        ?.minus(kseIndices?.preClose ?: 0.0))?.div(kseIndices?.preClose ?: 1.0)
                                        ?.times(100)
                                )
                            }%)"
                }
                if(!kseIndices?.lOWINDEX.equals(prevkseIndices?.lOWINDEX)) {
                    lowView.text = "L: ${kseIndices?.lOWINDEX} ${
                        String.format(
                            "%.2f",
                            kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose ?: 0.0)
                        )
                    } " +
                            "(${
                                String.format(
                                    "%.2f",
                                    (kseIndices?.lOWINDEX?.toDouble()
                                        ?.minus(kseIndices?.preClose ?: 0.0))?.div(kseIndices?.preClose ?: 1.0)
                                        ?.times(100)
                                )
                            }%)"
                }

                homeViewModel.setSelectedIndex(kseIndices)
            }

        }
    }

    private fun showDropdownMenu(view: View, list: List<KSEIndices>?, textView: TextView) {
        val popupMenu = PopupMenu(requireContext(), view)

        list?.forEach{
            popupMenu.getMenu().add(it.iNDEXCODE)
        }


        popupMenu.setOnMenuItemClickListener { item ->
            viewModel.mutablePreviousIndices.value=null
            initSelection(viewModel.mutablePreviousIndices.value?.filter { it.iNDEXCODE.equals(item.title.toString(),true)}?.first(),list?.filter { it.iNDEXCODE.equals(item.title.toString(),true)}?.first())
            homeViewModel.fetchChart()
            textView.setText(item.getTitle()) // Set selected option

            true
        }

        popupMenu.show()
    }



}