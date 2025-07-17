package com.example.scstrade.views.stockscreener.customscreener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class CustomScreenerFragment : Fragment() {

    private lateinit var binding: FragmentCustomScreenerBinding
    private lateinit var viewModel: StockScreenerViewModel
    private var allStocks: List<StockScreenerItem> = emptyList()
    private val calculatedAverages = mutableSetOf<String>() // Track fields whose avg is already set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (requireActivity() as StockScreenerActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomScreenerBinding.inflate(inflater, container, false)

        binding.totalResul.text = getString(R.string.total_resul, 0)
        binding.btn.text = getString(R.string.total_record, 0)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getStockScreener()
        viewModel.mutableStockScreener.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(requireView(), result.message)
                }

                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    binding.filterContainer.visibility = View.VISIBLE

                    allStocks = (result.data ?: emptyList()).filterNot { stock ->
                        stock.price == 0.0 &&
                                stock.priceToEarning == 0.0 &&
                                stock.expectedPriceToEarning == 0.0 &&
                                stock.dividendYield == 0.0 &&
                                stock.priceToBookValue == 0.0 &&
                                stock.returnOnAssets == 0.0 &&
                                stock.returnOnEquity == 0.0 &&
                                stock.eBITAMargin == 0.0 &&
                                stock.enterpriseValueToEBITDA == 0.0 &&
                                stock.grossProfitMargin == 0.0 &&
                                stock.payoutRatio == 0.0 &&
                                stock.priceEarningGrowth == 0.0 &&
                                stock.totalDebtToAssets == 0.0 &&
                                stock.totalDebtToEquity == 0.0
                    }

                    observeAllFilterChanges()
                }
            }
        })

        binding.btn.setOnClickListener {
            (requireActivity() as StockScreenerActivity).loadFragment("customscreenerdetail") {
                CustomScreenerListFragment()
            }
        }

        return binding.root
    }

    private fun observeAllFilterChanges() {
        val filterViews = listOf(
            binding.filterSharePrice to "Price",
            binding.filterPE to "PriceToEarning",
            binding.filterExpectedPE to "ExpectedPriceToEarning",
            binding.filterDividendYield to "DividendYield",
            binding.filterPBV to "PriceToBookValue",
            binding.filterROA to "ReturnOnAssets",
            binding.filterROE to "ReturnOnEquity",
            binding.filterEBITDAMargin to "EBITDAMargin",
            binding.filterEVToEBITDA to "EnterpriseValueToEBITDA",
            binding.filterGrossProfitMargin to "GrossProfitMargin",
            binding.filterPayoutRatio to "PayoutRatio",
            binding.filterPriceEarningGrowth to "PriceEarningGrowth",
            binding.filterTotalDebtToAssets to "TotalDebtToAssets",
            binding.filterTotalDebtToEquity to "TotalDebtToEquity"
        )

        filterViews.forEach { (view, _) ->
            view.filterLiveData.observe(viewLifecycleOwner) {
                val filters = filterViews.associate { (v, k) -> k to v.filterLiveData.value!! }

                // ✅ Always apply all filters on full list
                val filtered = applyFilters(allStocks, filters)
                viewModel.mutableFiltered.value = filtered

                val resultCount = filtered.size
                binding.totalResul.text = getString(R.string.total_resul, resultCount)
                binding.btn.text = getString(R.string.total_record, resultCount)

                filterViews.forEach { (v, key) ->
                    if (!calculatedAverages.contains(key)) {
                        val avg = calculateAverage(allStocks, key)
                        v.setAverage(avg)
                        calculatedAverages.add(key)
                    }

                    // ✅ Calculate and set per-row result count
                    val singleFilter = filters[key]
                    val count = allStocks.count { stock ->
                        val value = when (key) {
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
                            else -> return@count false
                        }

                        if (singleFilter == null || (singleFilter.min == null && singleFilter.max == null)) return@count false

                        when (singleFilter.operator.trim()) {
                            "Between two values" -> {
                                val minOk = singleFilter.min?.let { value >= it } ?: true
                                val maxOk = singleFilter.max?.let { value <= it } ?: true
                                minOk && maxOk
                            }
                            "Greater than equal to" -> singleFilter.min?.let { value >= it } ?: false
                            "Less than equal to" -> singleFilter.max?.let { value <= it } ?: false
                            else -> false
                        }
                    }

                    v.setResult(count)
                }
            }
        }
    }

    private fun applyFilters(
        stocks: List<StockScreenerItem>,
        filters: Map<String, FilterValue>
    ): List<StockScreenerItem> {
        var currentList = stocks
        filters.forEach { (field, filter) ->
            if (filter.min == null && filter.max == null) return@forEach

            currentList = currentList.filter { stock ->
                val value = when (field) {
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
                    else -> return@filter true
                }

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

        return currentList
    }

    private fun calculateAverage(stocks: List<StockScreenerItem>, field: String): Double {
        val values = stocks.mapNotNull {
            when (field) {
                "Price" -> it.price
                "PriceToEarning" -> it.priceToEarning
                "ExpectedPriceToEarning" -> it.expectedPriceToEarning
                "DividendYield" -> it.dividendYield
                "PriceToBookValue" -> it.priceToBookValue
                "ReturnOnAssets" -> it.returnOnAssets
                "ReturnOnEquity" -> it.returnOnEquity
                "EBITDAMargin" -> it.eBITAMargin
                "EnterpriseValueToEBITDA" -> it.enterpriseValueToEBITDA
                "GrossProfitMargin" -> it.grossProfitMargin
                "PayoutRatio" -> it.payoutRatio
                "PriceEarningGrowth" -> it.priceEarningGrowth
                "TotalDebtToAssets" -> it.totalDebtToAssets
                "TotalDebtToEquity" -> it.totalDebtToEquity
                else -> null
            }
        }

        return if (values.isNotEmpty()) values.average() else 0.0
    }
}
