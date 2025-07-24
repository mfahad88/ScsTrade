package com.example.scstrade.views.stockscreener.customscreener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentCustomScreenerBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.FilterValue
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stockscreener.StockScreenerItem
import com.example.scstrade.viewmodels.StockScreenerViewModel
import com.example.scstrade.views.stockscreener.StockScreenerActivity
import com.example.scstrade.views.widgets.FilterItemView

class CustomScreenerFragment : Fragment() {

    private lateinit var binding: FragmentCustomScreenerBinding
    private lateinit var viewModel: StockScreenerViewModel
    private var allStocks: List<StockScreenerItem> = emptyList()
//    private val selectedFilters = mutableSetOf<String>()
    private val selectedFilters get() = viewModel.selectedFilters
    private val filterViewsMap = mutableMapOf<String, FilterItemView>()

    private val filterFieldMap = mapOf(
        "Share Price" to "Price",
        "Price to Earnings (P/E)" to "PriceToEarning",
        "Expected Price to Earnings" to "ExpectedPriceToEarning",
        "Dividend Yield" to "DividendYield",
        "Price to Book Value (P/B)" to "PriceToBookValue",
        "Return on Assets (ROA)" to "ReturnOnAssets",
        "Return on Equity (ROE)" to "ReturnOnEquity",
        "EBITDA Margin" to "EBITDAMargin",
        "EV to EBITDA" to "EnterpriseValueToEBITDA",
        "Gross Profit Margin" to "GrossProfitMargin",
        "Payout Ratio" to "PayoutRatio",
        "Price Earning Growth" to "PriceEarningGrowth",
        "Total Debt to Assets" to "TotalDebtToAssets",
        "Total Debt to Equity" to "TotalDebtToEquity"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (requireActivity() as StockScreenerActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomScreenerBinding.inflate(inflater, container, false)

        binding.btn.text = getString(R.string.total_record, 0)
        binding.selectedCount.text = getString(R.string.selected_3, 0)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvSelectedFilters.setOnClickListener {
            showFilterSelectionDialog()
        }

        binding.btn.setOnClickListener {
            val fragment = CustomScreenerListFragment()
            fragment.arguments = Bundle().apply {
                putStringArrayList("selectedFilters", ArrayList(selectedFilters))
            }

            (requireActivity() as StockScreenerActivity).loadFragment("customscreenerdetail") {
                fragment
            }
        }

        viewModel.getStockScreener()
        viewModel.mutableStockScreener.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> Utils.showError(requireView(), result.message)
                is Resource.Loading -> {}
                is Resource.Success -> {
                    allStocks = (result.data ?: emptyList()).filterNot { stock ->
                        filterFieldMap.values.all { key ->
                            getStockFieldValue(stock, key) == 0.0
                        }
                    }

                    /*if (selectedFilters.isEmpty()) {
                        selectedFilters.addAll(
                            listOf(
                                "Share Price",
                                "Price to Earnings (P/E)",
                                "Expected Price to Earnings"
                            )
                        )
                    }*/

                    updateVisibleFilters()
                    binding.filterContainer.visibility = View.VISIBLE
                }
            }
        }

        return binding.root
    }

    private fun showFilterSelectionDialog() {
        val filterTitles = filterFieldMap.keys.toTypedArray()
        val checked = filterTitles.map { selectedFilters.contains(it) }.toBooleanArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Select Filters")
            .setMultiChoiceItems(filterTitles, checked) { _, which, isChecked ->
                val title = filterTitles[which]
                if (isChecked) selectedFilters.add(title)
                else selectedFilters.remove(title)
            }
            .setPositiveButton("OK") { _, _ -> updateVisibleFilters() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateVisibleFilters() {
        val container = binding.filterContainer
        filterViewsMap.clear()
        container.removeViews(2, container.childCount - 2)

        selectedFilters.forEach { title ->
            val filterView = FilterItemView(requireContext())
            filterView.setFilterTitle(title)
            container.addView(filterView)
            filterViewsMap[title] = filterView

            val key = filterFieldMap[title] ?: return@forEach
            val avg = calculateAverage(allStocks, key)
            filterView.setAverage(avg)

            val count = allStocks.count {
                val value = getStockFieldValue(it, key)
                !value.isNaN() && value != 0.0
            }
            filterView.setResult(count)
        }

        binding.selectedCount.text = getString(R.string.selected_3, selectedFilters.size)
        binding.tvSelectedFilters.text = "Select your Criteria"

        observeSelectedFilterChanges()
    }

    private fun observeSelectedFilterChanges() {
        filterViewsMap.forEach { (_, view) ->
            view.filterLiveData.removeObservers(viewLifecycleOwner)
        }

        filterViewsMap.forEach { (title, view) ->
            val field = filterFieldMap[title] ?: return@forEach
            view.filterLiveData.observe(viewLifecycleOwner) {
                val filters = filterViewsMap.mapNotNull { (t, v) ->
                    filterFieldMap[t]?.let { key -> key to v.filterLiveData.value!! }
                }.toMap()

                val hasValidFilter = filters.any { it.value.min != null || it.value.max != null }
                val filtered = if (hasValidFilter) applyFilters(allStocks, filters) else emptyList()
                viewModel.mutableFiltered.value = filtered
                if(filtered.size>0){
                    binding.btn.isEnabled = true
                }
                binding.btn.text = getString(R.string.total_record, filtered.size)

                filterViewsMap.forEach { (t, v) ->
                    val key = filterFieldMap[t] ?: return@forEach
                    val avg = calculateAverage(allStocks, key)
                    v.setAverage(avg)

                    val filter = filters[key]
                    if (filter?.min == null && filter?.max == null) {
                        v.setResult(0)
                    } else {
                        val count = allStocks.count { stock ->
                            val value = getStockFieldValue(stock, key)
                            when (filter.operator.trim()) {
                                "Between two values" -> {
                                    val minOk = filter.min?.let { value >= it } ?: true
                                    val maxOk = filter.max?.let { value <= it } ?: true
                                    minOk && maxOk
                                }
                                "Greater than equal to" -> filter.min?.let { value >= it } ?: false
                                "Less than equal to" -> filter.max?.let { value <= it } ?: false
                                else -> false
                            }
                        }
                        v.setResult(count)
                    }
                }
            }
        }
    }

    private fun getStockFieldValue(stock: StockScreenerItem, key: String): Double {
        return when (key) {
            "Price" -> stock.price
            "PriceToEarning" -> stock.priceToEarning
            "ExpectedPriceToEarning" -> stock.expectedPriceToEarning
            "DividendYield" -> stock.dividendYield
            "PriceToBookValue" -> stock.priceToBookValue
            "ReturnOnAssets" -> stock.returnOnAssets
            "ReturnOnEquity" -> stock.returnOnEquity
            "EBITDAMargin" -> stock.eBITAMargin
            "EnterpriseValueToEBITDA" -> stock.enterpriseValueToEBITDA
            "GrossProfitMargin" -> stock.grossProfitMargin
            "PayoutRatio" -> stock.payoutRatio
            "PriceEarningGrowth" -> stock.priceEarningGrowth
            "TotalDebtToAssets" -> stock.totalDebtToAssets
            "TotalDebtToEquity" -> stock.totalDebtToEquity
            else -> 0.0
        }
    }

    private fun calculateAverage(stocks: List<StockScreenerItem>, field: String): Double {
        val values = stocks.mapNotNull {
            val v = getStockFieldValue(it, field)
            if (v.isNaN() || v == 0.0) null else v
        }
        return if (values.isNotEmpty()) values.average() else 0.0
    }

    private fun applyFilters(
        stocks: List<StockScreenerItem>,
        filters: Map<String, FilterValue>
    ): List<StockScreenerItem> {
        var current = stocks
        filters.forEach { (field, filter) ->
            if (filter.min == null && filter.max == null) return@forEach
            current = current.filter {
                val value = getStockFieldValue(it, field)
                when (filter.operator.trim()) {
                    "Between two values" -> {
                        val minOk = filter.min?.let { value >= it } ?: true
                        val maxOk = filter.max?.let { value <= it } ?: true
                        minOk && maxOk
                    }
                    "Greater than equal to" -> filter.min?.let { value >= it } ?: true
                    "Less than equal to" -> filter.max?.let { value <= it } ?: true
                    else -> true
                }
            }
        }
        return current
    }
}