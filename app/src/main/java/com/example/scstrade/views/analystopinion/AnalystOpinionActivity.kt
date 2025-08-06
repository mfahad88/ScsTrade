package com.example.scstrade.views.analystopinion

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityAnalystOpinionBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.analystopinion.AnalystOpinionItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.analystopinion.adapter.AnalystOpinionAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.android.material.tabs.TabLayout

class AnalystOpinionActivity : AppCompatActivity() {
    lateinit var binding: ActivityAnalystOpinionBinding
    lateinit var sharedViewModel: SharedViewModel
    private lateinit var adapterA: AnalystOpinionAdapter
    private var fullList = listOf<AnalystOpinionItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAnalystOpinionBinding.inflate(LayoutInflater.from(this))
        sharedViewModel = (this.application as MyApp).viewModel
        setContentView(binding.root)
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        binding.toolbar.binding.market.text= getString(R.string.analyst_opinion)
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        adapterA = AnalystOpinionAdapter(emptyList()) { item ->
            if (!item.link.isNullOrBlank()) {
                val browserIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = android.net.Uri.parse(item.link)
                }
                startActivity(browserIntent)
            }
        }
        binding.recyclerView.apply {
            adapter = adapterA
            layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL,false)
            val divider = DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
            AppCompatResources.getDrawable(context, R.drawable.custom_divider)?.let {
                divider.setDrawable(it)
            }
            addItemDecoration(divider)
        }

        // Fetch analyst opinions
        sharedViewModel.analystOpinion()

        // Observe data
        sharedViewModel.mutableAnalystOpinion.observe(this) { result ->
            when(result) {
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Utils.showError(binding.root,result.message)
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    result.let {
                        fullList = it.data?: emptyList()
                        // Default tab = Fundamental
                        adapterA.submitList(fullList.filter { item -> item.aoType.equals("Fundamental", true) })
                    }
                    binding.apply {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                }
            }

        }

        // Tab selection handling
        binding.tabLayout.getTabAt(0)?.select()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedType = when (tab?.position) {
                    0 -> "Fundamental"
                    1 -> "Technical"
                    else -> null
                }
                val filtered = fullList.filter { it.aoType.equals(selectedType, ignoreCase = true) }
                adapterA.submitList(filtered)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.densityDpi = resources.displayMetrics.densityDpi
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration(res.configuration)
        val metrics = res.displayMetrics

        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

        config.fontScale = when {
            diagonalInches in 3.9..4.9 -> 0.85f
            diagonalInches in 4.9..5.4 -> 0.95f
            diagonalInches in 5.5..6.9 -> 1.0f
            else -> 1.2f
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment = 0
        }

        res.updateConfiguration(config, metrics)
        return res
    }
}