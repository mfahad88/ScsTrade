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
import com.example.scstrade.model.response.stock.StockItem
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
import java.util.PriorityQueue


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
/*
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
//            Toast.makeText(requireContext(),"Working In Progress under fixes",Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), AofActivity::class.java))
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
                    if(!entries.isNullOrEmpty()){
                        if(homeViewModel.selectedIndex.value==null){

                            val defaultIndex = result.data?.firstOrNull {
                                it.iNDEXCODE.contains("kse 100", true)
                            }

                            if (defaultIndex != null) {
                                homeViewModel.setSelectedIndex(defaultIndex)
                                homeViewModel.setSelectedCandle()
                            } else {
                                Log.w("HomeFragment", "No index found containing 'kse 100'")
                            }

//                        homeViewModel.fetchChart()
                        }else{
                            homeViewModel.setSelectedIndex(result.data?.first {
                                it.iNDEXCODE.replace("Index","").replace("Share","").contains(binding.cardHome.kmiallshr.text,true)
                            }?: emptyList<KSEIndices>().first())
                        }
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
                        if (!result.data.isNullOrEmpty()) {
                            val items: List<ListItem> = withContext(Dispatchers.Default) {
                                val data = result.data ?: emptyList()
                                val bySymbolMap = data.associateBy { it.sYM }

                                fun topKByVolume(data: List<StockItem>, k: Int = 10) =
                                    data.sortedByDescending { it.v }.take(k)

                                fun topKByGain(data: List<StockItem>, k: Int = 10) =
                                    data.sortedByDescending { it.cHP }.take(k)

                                fun topKByLoss(data: List<StockItem>, k: Int = 10) =
                                    data.sortedBy { it.cHP }.take(k)

                                val leaders = topKByVolume(data).map { ListItem.Item(it) }
                                val gainers = topKByGain(data).map { ListItem.Item(it) }
                                val losers = topKByLoss(data).map { ListItem.Item(it) }

                                val scsItems = topPicks
                                    .mapNotNull { bySymbolMap[it.sCSImpItemSymbol] }
                                    .map { ListItem.Item(it) }

                                buildList {
                                    add(ListItem.Header("Leaders"))
                                    addAll(leaders)

                                    if (scsItems.isNotEmpty()) {
                                        add(ListItem.Header("SCS Top Picks"))
                                        addAll(scsItems)
                                    }

                                    add(ListItem.Header("Gainers"))
                                    addAll(gainers)

                                    add(ListItem.Header("Losers"))
                                    addAll(losers)
                                }
                            }

                            // Submit list to RecyclerView
                            (binding.recyclerLeaders.adapter as StockAdapter).submitList(items) {
                                binding.apply {
                                    if (loader.alpha == 1f) {
                                        loader.alpha = 0f
                                        main.alpha = 1f
                                    }
                                }
                            }
                        }
                    }


                }
            }
        })
    }

    */
/*private fun showPopup(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)

        entries.forEach {
            popupMenu.menu.add(it.iNDEXCODE.replace("Index","").replace("Share",""))
        }

        popupMenu.setOnMenuItemClickListener { menu ->
            if(!viewModel.mutableIndices.value?.data.isNullOrEmpty()) {
                homeViewModel.setSelectedIndex(viewModel.mutableIndices.value?.data?.first {
                    it.iNDEXCODE.replace("Index", "").replace("Share", "")
                        .contains(menu.title.toString(), true)
                } ?: emptyList<KSEIndices>().first())
                homeViewModel.fetchChart()
                true
            }else{
                false
            }
        }
        popupMenu.show()

    }*//*

    private fun showPopup(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)

        // Populate menu
        entries.forEach {
            popupMenu.menu.add(it.iNDEXCODE.replace("Index", "").replace("Share", ""))
        }

        // Handle selection
        popupMenu.setOnMenuItemClickListener { menu ->
            val allIndices = viewModel.mutableIndices.value?.data ?: emptyList()

            val selected = allIndices.firstOrNull {
                it.iNDEXCODE.replace("Index", "").replace("Share", "")
                    .contains(menu.title.toString(), ignoreCase = true)
            }

            return@setOnMenuItemClickListener if (selected != null) {
                homeViewModel.setSelectedIndex(selected)
                homeViewModel.fetchChart()
                true
            } else {
                Toast.makeText(requireContext(), "Selected index not found", Toast.LENGTH_SHORT).show()
                Log.w("HomeFragment", "No index matched: ${menu.title}")
                false
            }
        }

        popupMenu.show()
    }

}*/
class HomeFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private var entries = emptyList<KSEIndices>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = (requireActivity().application as MyApp).viewModel
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        binding.aofCard.setOnClickListener {
            startActivity(Intent(requireContext(), AofActivity::class.java))
        }

        binding.cardHome.apply {
            zoom.setOnClickListener {
                val intent = Intent(requireContext(), ChartActivity::class.java)
                intent.putExtra("Indices", kmiallshr.text.trim().toString())
                startActivity(intent)
            }
            line.setOnClickListener { homeViewModel.setSelectedLine() }
            candle.setOnClickListener { homeViewModel.setSelectedCandle() }
            min1.setOnClickListener { homeViewModel.setSelectedTime(1) }
            min5.setOnClickListener { homeViewModel.setSelectedTime(5) }
            min15.setOnClickListener { homeViewModel.setSelectedTime(15) }
            min30.setOnClickListener { homeViewModel.setSelectedTime(30) }
            hr1.setOnClickListener { homeViewModel.setSelectedTime(60) }
            d1.setOnClickListener { homeViewModel.setSelectedTime(1440) }
            cardKmiAllShr.setOnClickListener { showPopup(it) }
            kmiallshr.setOnClickListener { showPopup(cardKmiAllShr) }
        }

        binding.recyclerLeaders.apply {
            adapter = StockAdapter()
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(HorizontalDivider(15.dp))
            isNestedScrollingEnabled = true
        }

        observeViewModels()
        return binding.root
    }

    private fun observeViewModels() {
        homeViewModel.apply {
            isLineSelected.observe(viewLifecycleOwner) {
                binding.cardHome.line.setChipSelected(it)
                binding.cardHome.lineChart.visibility = if (it) View.VISIBLE else View.GONE
                binding.cardHome.candlestickChart.visibility = if (it) View.GONE else View.VISIBLE
            }
            isCandleSelected.observe(viewLifecycleOwner) {
                binding.cardHome.candle.setChipSelected(it)
            }
            selectedTime.observe(viewLifecycleOwner) {
                val times = listOf(binding.cardHome.min1, binding.cardHome.min5, binding.cardHome.min15, binding.cardHome.min30, binding.cardHome.hr1, binding.cardHome.d1)
                times.forEachIndexed { i, chip -> chip.setChipSelected(it[i]) }
            }

            chartItem.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Success -> {
                        lifecycleScope.launch {
                            val data = result.data ?: return@launch
                            if (isCandleSelected.value == true) {
                                val candles = withContext(Dispatchers.Default) {
                                    data.takeLast(100).mapIndexed { index, it ->
                                        CandleEntry(index.toFloat(), it.tradingHigh.toFloat(), it.tradingLow.toFloat(), it.tradingOpen.toFloat(), it.tradingClose.toFloat())
                                    }
                                }
                                binding.cardHome.candlestickChart.setCandleData(candles)
                            } else {
                                val entries = withContext(Dispatchers.Default) {
                                    data.takeLast(100).mapIndexed { index, it ->
                                        Entry(index.toFloat(), it.tradingHigh.toFloat())
                                    }
                                }
                                binding.cardHome.lineChart.setEntries(entries, false, true)
                            }
                        }
                    }
                    else -> {}
                }
            }
        }

        viewModel.mutableIndices.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                entries = result.data ?: emptyList()
                if (entries.isNotEmpty() && homeViewModel.selectedIndex.value == null) {
                    val defaultIndex = entries.firstOrNull { it.iNDEXCODE.contains("kse 100", true) }
                    defaultIndex?.let {
                        homeViewModel.setSelectedIndex(it)
                        homeViewModel.setSelectedCandle()
                    }
                }
            }
        }

        homeViewModel.selectedIndex.observe(viewLifecycleOwner) { index ->
            index?.let { updateIndexUI(it) }
        }

        viewModel.mutableAllData.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                lifecycleScope.launch {
                    val topPicks = viewModel.mutableTopPicks.value?.data ?: emptyList()
                    val items = withContext(Dispatchers.Default) {
                        val allData = result.data ?: emptyList()
                        val map = allData.associateBy { it.sYM }
                        val leaders = allData.sortedByDescending { it.v }.take(10).map { ListItem.Item(it) }
                        val gainers = allData.sortedByDescending { it.cHP }.take(10).map { ListItem.Item(it) }
                        val losers = allData.sortedBy { it.cHP }.take(10).map { ListItem.Item(it) }
                        val scsItems = topPicks.mapNotNull { map[it.sCSImpItemSymbol] }.map { ListItem.Item(it) }

                        buildList {
                            add(ListItem.Header("Leaders"))
                            addAll(leaders)
                            if (scsItems.isNotEmpty()) {
                                add(ListItem.Header("SCS Top Picks"))
                                addAll(scsItems)
                            }
                            add(ListItem.Header("Gainers"))
                            addAll(gainers)
                            add(ListItem.Header("Losers"))
                            addAll(losers)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        (binding.recyclerLeaders.adapter as StockAdapter).submitList(items) {
                            binding.loader.alpha = 0f
                            binding.main.alpha = 1f
                        }
                    }
                }
            }
        }
    }

    private fun updateIndexUI(kse: KSEIndices) {
        binding.cardHome.apply {
            kmiallshr.text = kse.iNDEXCODE.replace("Index", "").replace("Share", "")
            val current = kse.cURRENTINDEX.toDoubleOrNull() ?: 0.0
            val change = kse.nETCHANGE ?: "0.0"
            tradeValueView.text = Utils.convertToMillions(current)
            tradeValueView.drawable = if (change.contains("-")) AppCompatResources.getDrawable(requireContext(), R.drawable.drop_down)
            else AppCompatResources.getDrawable(requireContext(), R.drawable.drop_up)
            netChangeChip.setText(change, kse.preClose.toString())
            volumeChip.text = kse.vOLUMETRADED
            highView.text = Utils.formatHighLow("H", kse.hIGHINDEX.toDoubleOrNull()?:0.0, kse.preClose)
            lowView.text = Utils.formatHighLow("L", kse.lOWINDEX.toDoubleOrNull()?:0.0, kse.preClose)
        }
    }

    private fun showPopup(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        entries.forEach {
            popupMenu.menu.add(it.iNDEXCODE.replace("Index", "").replace("Share", ""))
        }
        popupMenu.setOnMenuItemClickListener { menu ->
            val selected = viewModel.mutableIndices.value?.data?.firstOrNull {
                it.iNDEXCODE.replace("Index", "").replace("Share", "").contains(menu.title.toString(), true)
            }
            if (selected != null) {
                homeViewModel.setSelectedIndex(selected)
                homeViewModel.fetchChart()
                true
            } else {
                Toast.makeText(requireContext(), "Selected index not found", Toast.LENGTH_SHORT).show()
                false
            }
        }
        popupMenu.show()
    }
}