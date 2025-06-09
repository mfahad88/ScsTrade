package com.example.scstrade.views.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentHomeBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.HomeViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.ChartActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.allstock.StockAdapter
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.widgets.HorizontalDivider
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private var entries= emptyList<KSEIndices>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel = (requireActivity().application as MyApp).viewModel
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        (parentFragment as LandingFragment).binding.toolbar.binding.apply {
            titleItem.visibility = View.GONE
            group.visibility = View.VISIBLE
            titleItem.text = "Home"
        }

//        viewModel.fetchAllData()
//        viewModel.fetchIndices()

        binding.cardHome.apply {
            zoom.setOnClickListener {
                val intent = Intent(requireContext(),ChartActivity::class.java)
                intent.putExtra("Indices",binding.cardHome.kmiallshr.text.toString())
                startActivity(intent)
            }
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

                    }
                })

                isCandleSelected.observe(viewLifecycleOwner, Observer {
                    candle.setChipSelected(it)
                    if(it) {
                        lineChart.visibility = View.GONE
                        candlestickChart.visibility = View.VISIBLE

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
            adapter= StockAdapter()
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(15.dp))
            isNestedScrollingEnabled=true
        }

        binding.recyclerGainers.apply {
            adapter= StockAdapter()
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(15.dp))
            isNestedScrollingEnabled=true
        }
        binding.recyclerLosers.apply {
            adapter= StockAdapter()
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30.dp))
            isNestedScrollingEnabled=true

        }

        homeViewModel.selectedIndex.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                val kseIndices=it
                binding.cardHome.apply {
                    kmiallshr.text=kseIndices?.iNDEXCODE?.replace("Index","")
                    if(kseIndices?.vALUETRADED!="" && kseIndices?.vOLUMETRADED!="" && kseIndices?.cURRENTINDEX!="" && kseIndices?.nETCHANGE!="" && kseIndices?.hIGHINDEX!="" && kseIndices?.lOWINDEX!=""){
                        tradeValueView.text=Utils.convertToMillions(kseIndices?.vALUETRADED?.toDouble()?:0.0)
                        if(kseIndices?.nETCHANGE?.contains("-")?:false) {
                            tradeValueView.drawable =
                                AppCompatResources.getDrawable(requireContext(), R.drawable.drop_down)
                            volumeChip.binding.relativeLayout.background=AppCompatResources.getDrawable(requireContext(),R.drawable.rounded_gray_red)
                        }else{
                            tradeValueView.drawable =
                                AppCompatResources.getDrawable(requireContext(), R.drawable.drop_up)
                            volumeChip.binding.relativeLayout.background=AppCompatResources.getDrawable(requireContext(),R.drawable.rounded_gray_green)
                        }

                        netChangeChip.setText(kseIndices.nETCHANGE,kseIndices.preClose.toString())
                        volumeChip.text=kseIndices.vOLUMETRADED

                        highView.text = "H: ${Utils.formatDouble(kseIndices?.hIGHINDEX?.toDouble()?:0.0)} ${Utils.formatDouble(kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0)?:0.0)} " +
                                "(${Utils.formatDouble((kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))?.div(kseIndices?.preClose?:1.0)?.times(100)?:0.0)}%)"
                        lowView.text = "L: ${Utils.formatDouble(kseIndices?.lOWINDEX?.toDouble()?:0.0)} ${Utils.formatDouble(kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0)?:0.0)} " +
                                "(${Utils.formatDouble((kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))?.div(kseIndices?.preClose?:1.0)?.times(100)?:0.0)}%)"



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
                        homeViewModel.setSelectedLine()

//                        homeViewModel.fetchChart()
                    }else{
                        homeViewModel.setSelectedIndex(result.data?.first {
                            it.iNDEXCODE.contains(binding.cardHome.kmiallshr.text,true)
                        }?: emptyList<KSEIndices>().first())
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
                    (binding.recyclerLeaders.adapter as StockAdapter).submitList(result.data?.sortedByDescending { it.v }?.take(10)?: emptyList())
                    (binding.recyclerGainers.adapter as StockAdapter).submitList(result.data?.sortedByDescending { it.cHP }?.take(10)?: emptyList())
                    (binding.recyclerLosers.adapter as StockAdapter).submitList(result.data?.sortedBy { it.cHP }?.take(10)?: emptyList())
                    if(binding.main.visibility==View.GONE){
                        binding.loader.visibility=View.GONE
                        binding.main.visibility=View.VISIBLE
                    }
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
                           var candleEntry:ArrayList<CandleEntry>?= ArrayList()
                           result.data?.forEachIndexed { index, it ->
                               candleEntry?.add(
                                   CandleEntry(index.toFloat(), it.tradingHigh.toFloat(),it.tradingLow.toFloat(),it.tradingOpen.toFloat(),it.tradingClose.toFloat()))
                           }

                           binding.cardHome.candlestickChart.setCandleData(candleEntry?: emptyList())
                          /* binding.cardHome.candlestickChart.setCandleData(
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
                           )*/
                       }else{
                           binding.cardHome.lineChart.setEntries(result.data?.map {
                               interval+=1
                               Entry(interval.toFloat(),it.tradingHigh.toFloat())
                           },true)

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