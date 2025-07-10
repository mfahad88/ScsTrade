package com.example.scstrade.views.globalmarket

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityGlobalMarketBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.GlobalMarketViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.android.material.tabs.TabLayout
import androidx.compose.ui.unit.dp
import com.example.scstrade.model.response.globalMarket.GlobalMarketItem

class GlobalMarketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGlobalMarketBinding
    private lateinit var marketViewModel: GlobalMarketViewModel
    private var globalData: List<GlobalMarketItem>? = null // Replace with your actual model class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlobalMarketBinding.inflate(LayoutInflater.from(this))
        marketViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application as MyApp)
            .create(GlobalMarketViewModel::class.java)

        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)

        initRecyclerView()
        setupTabLayout()
        observeMarketData()

        marketViewModel.globalMarket()
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GlobalStockAdapter() { /* item click callback */ }
            addItemDecoration(HorizontalDivider(30.dp))
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                filterBySelectedTab()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                filterBySelectedTab()
            }
        })
    }

    private fun observeMarketData() {
        marketViewModel.mutableIndices.observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    globalData = result.data
                    filterBySelectedTab()
                }
                is Resource.Error -> {
                    Utils.showError(binding.root, result.message)
                }
                is Resource.Loading -> {
                    // Optional: show loading indicator
                }
            }
        }
    }

    private fun filterBySelectedTab() {
        val data = globalData ?: return
        val filtered = when (binding.tabLayout.selectedTabPosition) {
            0 -> data.filter { it.worldMarketType.equals("indices", true) }
            1 -> data.filter { it.worldMarketType.equals("Commodities", true) }
            2 -> data.filter { it.worldMarketType.equals("Currencies", true) }
            3 -> data.filter { it.worldMarketType.equals("Futrue", true) }
            4 -> data.filter { it.worldMarketType.equals("Crypto", true) }
            5 -> data.filter { it.worldMarketType.equals("Bonds", true) }
            else -> emptyList<GlobalMarketItem>()
        }
        (binding.recyclerView.adapter as GlobalStockAdapter).submitList(filtered)
    }


}
