package com.example.scstrade.views.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentHomeBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.HomeViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.ChartActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.allstock.ListItem
import com.example.scstrade.views.allstock.StockAdapter
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.widgets.HorizontalDivider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

        binding.aofCard.setOnClickListener {
            Toast.makeText(requireContext(),"Working In Progress under fixes",Toast.LENGTH_SHORT).show()
//            startActivity(Intent(requireContext(), AofActivity::class.java))
        }


        binding.cardHome.apply {
            zoom.setOnClickListener {
                val intent = Intent(requireContext(),ChartActivity::class.java)
                intent.putExtra("Indices",binding.cardHome.kmiallshr.text.trim().toString())
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
            d1.setOnClickListener {
                homeViewModel.setSelectedTime(1440)
            }
            cardKmiAllShr.setOnClickListener {
                showPopup(it)
            }

            kmiallshr.setOnClickListener {
                showPopup(cardKmiAllShr)
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
                    d1.setChipSelected(it[5])
                })
            }
        }

        binding.recyclerLeaders.apply {
            adapter= StockAdapter()
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(15.dp))
            isNestedScrollingEnabled=true

        }



        homeViewModel.selectedIndex.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                val kseIndices=it
                try {
                    binding.cardHome.apply {
                        kmiallshr.text=kseIndices.iNDEXCODE.replace("Index","").replace("Share","")
                        if(kseIndices?.vALUETRADED!="" && kseIndices?.vOLUMETRADED!="" && kseIndices?.cURRENTINDEX!="" && kseIndices?.nETCHANGE!="" && kseIndices?.hIGHINDEX!="" && kseIndices?.lOWINDEX!=""){
                            tradeValueView.text=Utils.convertToMillions(
                                kseIndices.cURRENTINDEX.toDouble() ?:0.0)
                            if(kseIndices?.nETCHANGE?.contains("-")?:false) {
                                tradeValueView.drawable =
                                    AppCompatResources.getDrawable(requireContext(), R.drawable.drop_down)
//                            volumeChip.binding.relativeLayout.background=AppCompatResources.getDrawable(requireContext(),R.drawable.rounded_gray_red)
                            }else{
                                tradeValueView.drawable =
                                    AppCompatResources.getDrawable(requireContext(), R.drawable.drop_up)
//                            volumeChip.binding.relativeLayout.background=AppCompatResources.getDrawable(requireContext(),R.drawable.rounded_gray_green)
                            }

                            netChangeChip.setText(kseIndices.nETCHANGE,kseIndices.preClose.toString())
                            volumeChip.text=kseIndices.vOLUMETRADED

                            if(kseIndices.hIGHINDEX.toDouble().minus(kseIndices.preClose)<0.0){
                                highView.text = "H: ${Utils.formatDouble(kseIndices?.hIGHINDEX?.toDouble()?:0.0)} ${Utils.formatDouble(kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0)?:0.0)} " +
                                        "${Utils.formatDouble((kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))?.div(kseIndices?.preClose?:1.0)?.times(100)?:0.0)}%"
                            }else{

                                highView.text = "H: ${Utils.formatDouble(kseIndices?.hIGHINDEX?.toDouble()?:0.0)} +${Utils.formatDouble(kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0)?:0.0)} " +
                                        "+${Utils.formatDouble((kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))?.div(kseIndices?.preClose?:1.0)?.times(100)?:0.0)}%"
                            }

                            if(kseIndices.lOWINDEX.toDouble().minus(kseIndices.preClose)<0.0){
                                lowView.text = "L: ${Utils.formatDouble(kseIndices?.lOWINDEX?.toDouble()?:0.0)} ${Utils.formatDouble(kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0)?:0.0)} " +
                                        "${Utils.formatDouble((kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))?.div(kseIndices?.preClose?:1.0)?.times(100)?:0.0)}%"
                            }else{
                                lowView.text = "L: ${Utils.formatDouble(kseIndices?.lOWINDEX?.toDouble()?:0.0)} +${Utils.formatDouble(kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0)?:0.0)} " +
                                        "+${Utils.formatDouble((kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))?.div(kseIndices?.preClose?:1.0)?.times(100)?:0.0)}%"
                            }



                        }else{
                            binding.cardHome.apply {
                                tradeValueView.text = "0.0"
                                volumeChip.text="0.0"
                                netChangeChip.setText("0.0 0.0%","0.0")
                                highView.text = "H: 0.0 0.0 0.0%"
                                lowView.text = "L: 0.0 0.0 0.0%"
                            }
                        }

                    }
                }catch (e:Exception){
                    e.printStackTrace()
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
                        homeViewModel.setSelectedCandle()

//                        homeViewModel.fetchChart()
                    }else{
                        homeViewModel.setSelectedIndex(result.data?.first {
                            it.iNDEXCODE.replace("Index","").replace("Share","").contains(binding.cardHome.kmiallshr.text,true)
                        }?: emptyList<KSEIndices>().first())
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
                           result.data?.reversed()?.forEachIndexed { index, it ->
                               candleEntry?.add(
                                   CandleEntry(index.toFloat(), it.tradingHigh.toFloat(),it.tradingLow.toFloat(),it.tradingOpen.toFloat(),it.tradingClose.toFloat()))
                           }

                           binding.cardHome.candlestickChart.setCandleData(candleEntry?: emptyList())

                       }else{

                           binding.cardHome.lineChart.setEntries(result.data?.reversed()?.map {
                               interval+=1
                               Entry(interval.toFloat(),it.tradingHigh.toFloat())
                           },false,true)

                       }
                   }
               }
           }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mutableAllData.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val topPicks = viewModel.mutableTopPicks.value?.data ?: emptyList()
                    lifecycleScope.launch {
                        // Step 1: Do heavy computation off the main thread
                        val items: List<ListItem> = withContext(Dispatchers.Default) {
                            val data = result.data ?: emptyList()

                            val bySymbolMap = data.associateBy { it.sYM } // O(n) map for fast lookup

                            buildList {
                                // Leaders Section
                                add(ListItem.Header("Leaders"))
                                data.sortedByDescending { it.v } // sort by volume descending
                                    .take(10)
                                    .forEach { add(ListItem.Item(it)) }

                                // SCS Top Picks - Optimized
                                val scsItems = topPicks
                                    .asSequence()
                                    .map { it.sCSImpItemSymbol }
                                    .mapNotNull { bySymbolMap[it] }
                                    .toList()

                                if (scsItems.isNotEmpty()) {
                                    add(ListItem.Header("SCS Top Picks"))
                                    scsItems.forEach { add(ListItem.Item(it)) }
                                }

                                // Gainers Section
                                add(ListItem.Header("Gainers"))
                                data.sortedByDescending { it.cHP } // sort by change percentage high to low
                                    .take(10)
                                    .forEach { add(ListItem.Item(it)) }

                                // Losers Section
                                add(ListItem.Header("Losers"))
                                data.sortedBy { it.cHP } // sort by change percentage low to high
                                    .take(10)
                                    .forEach { add(ListItem.Item(it)) }
                            }
                        }

                        // Step 2: Back on the main thread, update UI once
                        (binding.recyclerLeaders.adapter as StockAdapter).submitList(items) {
                            binding.apply {
                                if (loader.visibility == View.VISIBLE) {
                                    loader.visibility = View.GONE
                                    main.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

                }
            }
        })
    }

    private fun showPopup(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)

        entries.forEach {
            popupMenu.menu.add(it.iNDEXCODE)
        }

        popupMenu.setOnMenuItemClickListener { menu ->

            homeViewModel.setSelectedIndex(viewModel.mutableIndices.value?.data?.first {
                it.iNDEXCODE.contains(menu.title.toString(), true)
            } ?: emptyList<KSEIndices>().first())
            homeViewModel.fetchChart()
            true
        }
        popupMenu.show()

    }


}