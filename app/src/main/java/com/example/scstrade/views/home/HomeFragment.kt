package com.example.scstrade.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
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
import com.example.scstrade.viewmodels.HomeViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.stock.StockAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private var entries= emptyList<KSEIndices>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.cardHome.apply {
            line.setOnClickListener {
                homeViewModel.setSelectedLine()
            }
            candle.setOnClickListener {
                homeViewModel.setSelectedCandle()
            }
            min1.setOnClickListener {
                homeViewModel.setSelectedTime(1)
            }
            min5.setOnClickListener {
                homeViewModel.setSelectedTime(5)
            }
            min15.setOnClickListener {
                homeViewModel.setSelectedTime(15)
            }
            min30.setOnClickListener {
                homeViewModel.setSelectedTime(30)
            }
            hr1.setOnClickListener {
                homeViewModel.setSelectedTime(60)
            }
            imageViewDropDown.setOnClickListener {
                showPopup(it)
            }

            kmiallshr.setOnClickListener {
                showPopup(imageViewDropDown)
            }

            homeViewModel.apply {
                isLineSelected.observe(viewLifecycleOwner, Observer {
                    line.setChipSelected(it)
                    if(it) {
                        lineChart.visibility = View.VISIBLE
                        candlestickChart.visibility = View.GONE
                        min1.isEnabled=false
                        min5.isEnabled=false
                        min15.isEnabled=false
                        min30.isEnabled=false
                        hr1.isEnabled=false
                    }
                })

                isCandleSelected.observe(viewLifecycleOwner, Observer {
                    candle.setChipSelected(it)
                    if(it) {
                        lineChart.visibility = View.GONE
                        candlestickChart.visibility = View.VISIBLE
                        min1.isEnabled=true
                        min5.isEnabled=true
                        min15.isEnabled=true
                        min30.isEnabled=true
                        hr1.isEnabled=true
                    }
                })
                selectedTime.observe(viewLifecycleOwner, Observer {
                    min1.setChipSelected(it[0])
                    min5.setChipSelected(it[1])
                    min15.setChipSelected(it[2])
                    min30.setChipSelected(it[3])
                    hr1.setChipSelected(it[4])
                })
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

        homeViewModel.selectedIndex.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                val kseIndices=it
                binding.cardHome.apply {
                    kmiallshr.text=kseIndices?.iNDEXCODE
                    if(kseIndices?.vALUETRADED!="" && kseIndices?.vOLUMETRADED!="" && kseIndices?.cURRENTINDEX!="" && kseIndices?.nETCHANGE!="" && kseIndices?.hIGHINDEX!="" && kseIndices?.lOWINDEX!=""){
                        tradeValueView.text=Utils.convertToMillions(kseIndices?.vALUETRADED?.toDouble()?:0.0)
                        if(kseIndices?.nETCHANGE?.contains("-")?:false) {
                            tradeValueView.drawable =
                                AppCompatResources.getDrawable(requireContext(), R.drawable.drop_down)
                        }else{
                            tradeValueView.drawable =
                                AppCompatResources.getDrawable(requireContext(), R.drawable.drop_up)
                        }
                        netChangeChip.text = "${kseIndices?.nETCHANGE} (${String.format("%.2f",(kseIndices?.nETCHANGE?.toDouble()?.div(kseIndices?.preClose?:0.0))?.times(100))}%)"
                        volumeChip.text="Volume: ${Utils.convertToMillions(kseIndices?.vOLUMETRADED?.toDouble()?:0.0)}"
                        highView.text = "H: ${kseIndices?.hIGHINDEX} ${String.format("%.2f",kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))} " +
                                "(${String.format("%.2f",(kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))?.div(kseIndices?.preClose?:1.0)?.times(100))}%)"
                        lowView.text = "L: ${kseIndices?.lOWINDEX} ${String.format("%.2f",kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))} " +
                                "(${String.format("%.2f",(kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))?.div(kseIndices?.preClose?:1.0)?.times(100))}%)"



                    }

                }
            }
        })

        viewModel.mutableIndices.observe(viewLifecycleOwner, Observer {result->
            when(result){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    entries=result.data?: emptyList()
                    if(homeViewModel.selectedIndex.value==null){

                        homeViewModel.setSelectedIndex(result.data?.first {
                            it.iNDEXCODE.contains("kse 100",true)
                        }?: emptyList<KSEIndices>().first())
                        homeViewModel.fetchChart()
                    }else{
                        homeViewModel.setSelectedIndex(result.data?.first {
                            it.iNDEXCODE.contains(binding.cardHome.kmiallshr.text,true)
                        }?: emptyList<KSEIndices>().first())
                    }
                    if(!viewModel.isLoading){
                        binding.loader.visibility=View.GONE
                        binding.main.visibility=View.VISIBLE
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

        homeViewModel.chartItem.observe(viewLifecycleOwner, Observer {result->
           when(result){
               is Resource.Error -> {}
               is Resource.Loading -> {}
               is Resource.Success -> {
                   var interval=0
                   if(!result.data.isNullOrEmpty()){
                       if(homeViewModel.isCandleSelected.value==true){
                           binding.cardHome.candlestickChart.setCandleData(
                               result.data?.map {
                                   if(homeViewModel.selectedTime.value?.indexOf(true)==0){
                                       interval+=1
                                   } else if(homeViewModel.selectedTime.value?.indexOf(true)==1){
                                       interval+=5
                                   } else if(homeViewModel.selectedTime.value?.indexOf(true)==2){
                                       interval+=15
                                   }else if(homeViewModel.selectedTime.value?.indexOf(true)==3){
                                       interval+=60
                                   }
                                   CandleEntry(interval.toFloat(),it.tradingHigh.toFloat(),it.tradingLow.toFloat(),it.tradingOpen.toFloat(),it.tradingClose.toFloat())
                               }?: emptyList()
                           )
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

    private fun showPopup(view: View) {
        val popupMenu=PopupMenu(requireContext(),view)
        entries.forEach {
            popupMenu.menu.add(it.iNDEXCODE)
        }
        popupMenu.setOnMenuItemClickListener {menu->

            homeViewModel.setSelectedIndex(viewModel.mutableIndices.value?.data?.first {
                it.iNDEXCODE.contains(menu.title.toString(),true)
            }?: emptyList<KSEIndices>().first())
            homeViewModel.fetchChart()
            true
        }
        popupMenu.show()
    }

}