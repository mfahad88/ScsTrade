package com.example.scstrade.views.stockscreener.fundamental

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityFundamentalDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.snapshot.SnapshotActivity
import com.example.scstrade.views.stockscreener.fundamental.adapter.FundamentalDetailAdapter
import com.example.scstrade.views.widgets.VerticalSpaceItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class FundamentalDetailActivity : BaseActivity() {

    lateinit var binding: ActivityFundamentalDetailBinding
    lateinit var viewModel: SharedViewModel

    private var resultList = mutableListOf<Array<String>>()
    private var originalList = mutableListOf<Array<String>>()
    private var currentSortColumn: Int? = null
    private var sortAscending = true
    private lateinit var adapter: FundamentalDetailAdapter
    private lateinit var sortIcons: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (this.application as MyApp).viewModel
        binding = ActivityFundamentalDetailBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)

        // Toolbar label
        binding.toolbar.binding.market.text = intent.extras?.getString(AppConstants.TECHNICAL_SELECTION)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FundamentalDetailActivity)
            addItemDecoration(
                VerticalSpaceItemDecoration(
                    1,
                    ContextCompat.getColor(this@FundamentalDetailActivity, R.color.md_theme_outline)
                )
            )
        }

        adapter = FundamentalDetailAdapter(resultList, viewModel) {
            val intent = Intent(this@FundamentalDetailActivity, SnapshotActivity::class.java)
            intent.putExtra(AppConstants.SYMBOL, it)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter

        // Icons
        sortIcons = listOf(
            binding.sortIcon1,
            binding.sortIcon2,
            binding.sortIcon3,
            binding.sortIcon4
        )

        // Header click listeners
        binding.header1Container.setOnClickListener { sortByColumn(0) }
        binding.header2Container.setOnClickListener { sortByColumn(1) }
        binding.header3Container.setOnClickListener { sortByColumn(3) }
        binding.header4Container.setOnClickListener { sortByColumn(4) }

        viewModel.getFundamentalDetail(intent.extras?.getString(AppConstants.TECHNICAL_SELECTION) ?: "")
        viewModel.mutableFundamentalDetail.observe(this, Observer {
            when (it) {
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root, it.message ?: "An error occurred")
                }

                is Resource.Loading -> binding.loader.visibility = View.VISIBLE

                is Resource.Success -> {
                    val jsonElement = it.data
                    if (jsonElement?.isJsonArray == true) {
                        val headers = jsonElement.asJsonArray.first().asJsonObject.entrySet()
                            .distinctBy { it.key }
                            .filterNot { it.key.equals("company_name", true) }

                        headers.forEachIndexed { index, entry ->
                            when (index) {
                                0 -> binding.header1.text = entry.key
                                1 -> binding.header2.text = entry.key
                                2 -> binding.header3.text = entry.key
                                3 -> binding.header4.text = entry.key
                            }
                        }

                        resultList.clear()
                        originalList.clear()

                        lifecycleScope.launch(Dispatchers.IO) {
                            jsonElement.asJsonArray.forEach { element ->
                                val row = element.asJsonObject.entrySet()
                                    .map { it.value.asString }
                                    .toMutableList()

                                val symbol = row.getOrNull(0)
                                val match = viewModel.mutableAllData.value?.data?.firstOrNull {
                                    it.sYM.equals(symbol, ignoreCase = true)
                                }

                                val avValue = match?.aV?.toString() ?: "-"
                                row.add(avValue)
                                resultList.add(row.toTypedArray())
                            }
                            originalList = resultList.toMutableList()
                            runOnUiThread {
                                adapter.submitList(resultList)
                                binding.loader.visibility = View.GONE
                                binding.groupMain.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        })
    }

    private fun sortByColumn(index: Int) {
        sortAscending = if (currentSortColumn == index) !sortAscending else true
        currentSortColumn = index

        val sortedList = if (index == 0) {
            if (sortAscending) {
                originalList.sortedBy {
                    it.getOrNull(index)?.trim()?.lowercase() ?: ""
                }
            } else {
                originalList.sortedByDescending {
                    it.getOrNull(index)?.trim()?.lowercase() ?: ""
                }
            }
        } else {
            if (sortAscending) {
                originalList.sortedBy {
                    it.getOrNull(index)?.trim()?.toDoubleOrNull() ?: 0.0
                }
            } else {
                originalList.sortedByDescending {
                    it.getOrNull(index)?.trim()?.toDoubleOrNull() ?: 0.0
                }
            }
        }
        resultList = sortedList.toMutableList()
        adapter.submitList(resultList.toList())


        if(index==0) {
            sortIcons.forEach { it.setImageResource(R.drawable.ic_sort_default) }
            val iconRes = if (sortAscending) R.drawable.ic_sort_up else R.drawable.ic_sort_down
            sortIcons.getOrNull(0)?.setImageResource(iconRes)
        }else if (index==1){
            sortIcons.forEach { it.setImageResource(R.drawable.ic_sort_default) }
            val iconRes = if (sortAscending) R.drawable.ic_sort_up else R.drawable.ic_sort_down
            sortIcons.getOrNull(1)?.setImageResource(iconRes)
        }else if (index==3){
            sortIcons.forEach { it.setImageResource(R.drawable.ic_sort_default) }
            val iconRes = if (sortAscending) R.drawable.ic_sort_up else R.drawable.ic_sort_down
            sortIcons.getOrNull(2)?.setImageResource(iconRes)
        }else if(index==4){
            sortIcons.forEach { it.setImageResource(R.drawable.ic_sort_default) }
            val iconRes = if (sortAscending) R.drawable.ic_sort_up else R.drawable.ic_sort_down
            sortIcons.getOrNull(3)?.setImageResource(iconRes)
        }
       /* sortAscending = if (currentSortColumn == index) !sortAscending else true
        currentSortColumn = index

        // Perform sorting
        val sortedList = originalList.sortedWith(compareBy {
            it.getOrNull(index)?.let { value -> value.toDoubleOrNull() ?: value }
        })

        resultList = if (sortAscending) sortedList.toMutableList() else sortedList.reversed().toMutableList()
        adapter.submitList(resultList)

        // Update all icons to default
        sortIcons.forEachIndexed { i, icon ->
            icon.setImageResource(R.drawable.ic_sort_default)
        }

        // Set current icon based on sort direction
        val iconRes = if (sortAscending) R.drawable.ic_sort_down else R.drawable.ic_sort_up
        sortIcons.getOrNull(index)?.setImageResource(iconRes)*/
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration(res.configuration)
        val metrics = res.displayMetrics
        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = sqrt(widthInches * widthInches + heightInches * heightInches)

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

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.densityDpi = resources.displayMetrics.densityDpi
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}
