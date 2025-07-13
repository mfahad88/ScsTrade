package com.example.scstrade.views.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.example.scstrade.views.allstock.ListItem
import com.example.scstrade.views.allstock.StockAdapter
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.widgets.HorizontalDivider
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.view.doOnPreDraw
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay

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
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        Utils.startFrameTimeMonitoring()

        setupUI()
        setupRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            delay(700)
            observeLiveData()
        }

        return binding.root
    }

    private fun setupUI() {
        binding.aofCard.setOnClickListener {
            startActivity(Intent(requireContext(), AofActivity::class.java))
        }

        binding.cardHome.apply {
            zoom.setOnClickListener {
                val intent = Intent(requireContext(), ChartActivity::class.java)
                intent.putExtra("Indices", kmiallshr.text.trim().toString())
                startActivity(intent)
            }

            listOf(min1 to 1, min5 to 5, min15 to 15, min30 to 30, hr1 to 60, d1 to 1440).forEach { (view, value) ->
                view.setOnClickListener { homeViewModel.setSelectedTime(value) }
            }

            line.setOnClickListener { homeViewModel.setSelectedLine() }
            candle.setOnClickListener { homeViewModel.setSelectedCandle() }
            listOf(cardKmiAllShr, kmiallshr).forEach { it.setOnClickListener { showPopup(cardKmiAllShr) } }

            homeViewModel.apply {
                isLineSelected.observe(viewLifecycleOwner) {
                    line.setChipSelected(it)
                    lineChart.visibility = if (it) View.VISIBLE else View.GONE
                    candlestickChart.visibility = if (!it) View.VISIBLE else View.GONE
                }
                isCandleSelected.observe(viewLifecycleOwner) {
                    candle.setChipSelected(it)
                }
                selectedTime.observe(viewLifecycleOwner) {
                    listOf(min1, min5, min15, min30, hr1, d1).forEachIndexed { index, chip ->
                        chip.setChipSelected(it[index])
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerLeaders.apply {
            adapter = StockAdapter()
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(HorizontalDivider(15.dp))
            isNestedScrollingEnabled = true
            itemAnimator = null
        }
    }

    private fun observeLiveData() {
        homeViewModel.selectedIndex.observe(viewLifecycleOwner) { kseIndices ->
            kseIndices?.let { updateIndexData(it) }
        }

        viewModel.mutableIndices.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                entries = result.data.orEmpty()
                val defaultIndex = entries.firstOrNull { it.iNDEXCODE.contains("kse 100", true) }
                val selectedText = binding.cardHome.kmiallshr.text.toString()
                val matched = entries.firstOrNull { it.iNDEXCODE.replace("Index", "").replace("Share", "").contains(selectedText, true) }
                homeViewModel.setSelectedIndex(homeViewModel.selectedIndex.value ?: matched ?: defaultIndex ?: return@observe)
                homeViewModel.setSelectedCandle()
            }
        }

        homeViewModel.chartItem.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success && !result.data.isNullOrEmpty()) {
                val reversedData = result.data!!.reversed()
                if (homeViewModel.isCandleSelected.value == true) {
                    val entries = reversedData.mapIndexed { index, it ->
                        CandleEntry(index.toFloat(), it.tradingHigh.toFloat(), it.tradingLow.toFloat(), it.tradingOpen.toFloat(), it.tradingClose.toFloat())
                    }
                    binding.cardHome.candlestickChart.setCandleData(entries)
                } else {
                    val entries = reversedData.mapIndexed { index, it ->
                        Entry(index.toFloat(), it.tradingHigh.toFloat())
                    }
                    binding.cardHome.lineChart.setEntries(entries, false, true)
                }
            }
        }

        viewModel.mutableAllData.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                val topPicks = viewModel.mutableTopPicks.value?.data.orEmpty()
                val topPickSymbols = topPicks.mapTo(HashSet()) { it.sCSImpItemSymbol }
                viewLifecycleOwner.lifecycleScope.launch {
                    val data = result.data.orEmpty()

                    val items = withContext(Dispatchers.IO) {
                        val sortedDescending = data.sortedByDescending { it.cHP }.map { ListItem.Item(it) }
                        val leaders = async {
                            data.sortedByDescending { it.v }.take(10).map { ListItem.Item(it) }
                        }
                        val gainers = async {
                            sortedDescending.take(10)
//                            data.sortedByDescending { it.cHP }.take(10).map { ListItem.Item(it) }
                        }
                        val losers = async {
                            sortedDescending.takeLast(10)
//                            data.sortedBy { it.cHP }.take(10).map { ListItem.Item(it) }
                        }
                        val scs = async {
                            data.filter { it.sYM in topPickSymbols }.map { ListItem.Item(it) }
                        }

                        mutableListOf<ListItem>().apply {
                            add(ListItem.Header("Leaders"))
                            addAll(leaders.await())
                            if (scs.await().isNotEmpty()) {
                                add(ListItem.Header("SCS Top Picks"))
                                addAll(scs.await())
                            }
                            add(ListItem.Header("Gainers"))
                            addAll(gainers.await())
                            add(ListItem.Header("Losers"))
                            addAll(losers.await())
                        }


                    }

                    (binding.recyclerLeaders.adapter as? StockAdapter)?.submitList(items) {
                        binding.loader.animate()
                            .alpha(0f)
                            .setDuration(10)
                            .withEndAction {
//                                    binding.loader.cancelAnimation()
                                binding.main.animate().alpha(1f).setDuration(10).start()
                            }
                            .start()

                    }
                }
            }
        }
    }

    private fun updateIndexData(kse: KSEIndices) = binding.cardHome.run {
        kmiallshr.text = kse.iNDEXCODE.replace("Index", "").replace("Share", "")
        if (kse.vALUETRADED.isNotBlank() && kse.vOLUMETRADED.isNotBlank()) {
            tradeValueView.text = Utils.convertToMillions(kse.cURRENTINDEX.toDoubleOrNull() ?: 0.0)
            tradeValueView.drawable = AppCompatResources.getDrawable(
                requireContext(),
                if (kse.nETCHANGE.contains("-")) R.drawable.drop_down else R.drawable.drop_up
            )
            netChangeChip.setText(kse.nETCHANGE, kse.preClose.toString())
            volumeChip.text = kse.vOLUMETRADED
            highView.text = formatIndexText("H", kse.hIGHINDEX, kse.preClose)
            lowView.text = formatIndexText("L", kse.lOWINDEX, kse.preClose)
        } else {
            tradeValueView.text = "0.0"
            volumeChip.text = "0.0"
            netChangeChip.setText("0.0 0.0%", "0.0")
            highView.text = "H: 0.0 0.0 0.0%"
            lowView.text = "L: 0.0 0.0 0.0%"
        }
    }

    private fun formatIndexText(prefix: String, index: String, preClose: Double): String {
        val value = index.toDoubleOrNull() ?: return "$prefix: 0.0 0.0 0.0%"
        val diff = value - preClose
        val percent = (diff / preClose) * 100
        val sign = if (diff >= 0) "+" else ""
        return "$prefix: ${Utils.formatDouble(value)} $sign${Utils.formatDouble(diff)} $sign${Utils.formatDouble(percent)}%"
    }

    private fun showPopup(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        entries.forEach {
            popupMenu.menu.add(it.iNDEXCODE.replace("Index", "").replace("Share", ""))
        }
        popupMenu.setOnMenuItemClickListener { menu ->
            viewModel.mutableIndices.value?.data?.firstOrNull {
                it.iNDEXCODE.replace("Index", "").replace("Share", "").contains(menu.title.toString(), true)
            }?.let {
                homeViewModel.setSelectedIndex(it)
                homeViewModel.fetchChart()
                true
            } ?: false
        }
        popupMenu.show()
    }
}
