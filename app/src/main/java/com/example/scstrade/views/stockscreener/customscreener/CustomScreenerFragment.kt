package com.example.scstrade.views.stockscreener.customscreener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.databinding.FragmentCustomScreenerBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.FilterValue
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stockscreener.StockScreenerItem
import com.example.scstrade.viewmodels.StockScreenerViewModel
import com.example.scstrade.views.widgets.FilterItemView

class CustomScreenerFragment : Fragment() {
    private lateinit var binding: FragmentCustomScreenerBinding
    private lateinit var viewModel: StockScreenerViewModel
    private var allStocks: List<StockScreenerItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[StockScreenerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomScreenerBinding.inflate(inflater, container, false)

        viewModel.getStockScreener()
        viewModel.mutableStockScreener.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Error -> {
                    binding.loader.visibility =View.GONE
                    Utils.showError(requireView(), result.message)
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    /*allStocks = result.data ?: emptyList()*/
                    binding.apply {
                        loader.visibility = View.GONE
                        filterContainer.visibility = View.VISIBLE
                    }
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
            binding.filterCurrentRatio to "CurrentRatio",
            binding.filterEBITMargin to "EBITMargin",
            binding.filterEBITDAMargin to "EBITDAMargin",
            binding.filterEVToEBITDA to "EnterpriseValueToEBITDA",
            binding.filterEquityToAssets to "EquityToAssetsRatio",
            binding.filterExpectedDividendYield to "ExpectedDividendYield",
            binding.filterExpectedEarningGrowth to "ExpectedEarningGrowth",
            binding.filterExpectedPayoutRatio to "ExpectedPayoutRatio",
            binding.filterExpectedPBV to "ExpectedPriceToBookValue",
            binding.filterExpectedRetentionRatio to "ExpectedRetentionRatio",
            binding.filterExpectedROE to "ExpectedReturnOnEquity",
            binding.filterGrossProfitMargin to "GrossProfitMargin",
            binding.filterLongTermDebtToAssets to "LongTermDebtToAssets",
            binding.filterLongTermDebtToEquity to "LongTermDebtToEquity",
            binding.filterNetProfitMargin to "NetProfitMargin",
            binding.filterPayoutRatio to "PayoutRatio",
            binding.filterPriceEarningGrowth to "PriceEarningGrowth",
            binding.filterQuickRatio to "QuickRatio",
            binding.filterRetentionRatio to "RetentionRatio",
            binding.filterTotalDebtToAssets to "TotalDebtToAssets",
            binding.filterTotalDebtToEquity to "TotalDebtToEquity"
        )

        filterViews.forEach { (view, key) ->
            view.filterLiveData.observe(viewLifecycleOwner) {
                val filters = filterViews.associate { (v, k) -> k to v.filterLiveData.value!! }

                val filtered = applyFilters(allStocks, filters)

                // 3. Calculate average for current field
                val avg = calculateAverage(filtered, key)

                // 4. Show average and result count in view
                view.setAverageAndResult(avg, filtered.size)
            }
        }
    }

    private fun applyFilters(
        stocks: List<StockScreenerItem>,
        filters: Map<String, FilterValue>
    ): List<StockScreenerItem> {
        return stocks.filter { stock ->
            filters.all { (field, filter) ->
                val value = when (field) {
                    "Price" -> stock.price
                    "PriceToEarning" -> stock.priceToEarning
                    "ExpectedPriceToEarning" -> stock.expectedPriceToEarning
                    "DividendYield" -> stock.dividendYield
                    "PriceToBookValue" -> stock.priceToBookValue
                    "ReturnOnAssets" -> stock.returnOnAssets
                    "ReturnOnEquity" -> stock.returnOnEquity
                    "CurrentRatio" -> 0.0 // Replace with actual field if available
                    "EBITMargin" -> 0.0
                    "EBITDAMargin" -> stock.eBITAMargin
                    "EnterpriseValueToEBITDA" -> stock.enterpriseValueToEBITDA
                    "EquityToAssetsRatio" -> 0.0
                    "ExpectedDividendYield" -> 0.0
                    "ExpectedEarningGrowth" -> 0.0
                    "ExpectedPayoutRatio" -> 0.0
                    "ExpectedPriceToBookValue" -> 0.0
                    "ExpectedRetentionRatio" -> 0.0
                    "ExpectedReturnOnEquity" -> 0.0
                    "GrossProfitMargin" -> stock.grossProfitMargin
                    "LongTermDebtToAssets" -> 0.0
                    "LongTermDebtToEquity" -> 0.0
                    "NetProfitMargin" -> 0.0
                    "PayoutRatio" -> stock.payoutRatio
                    "PriceEarningGrowth" -> stock.priceEarningGrowth
                    "QuickRatio" -> 0.0
                    "RetentionRatio" -> 0.0
                    "TotalDebtToAssets" -> stock.totalDebtToAssets
                    "TotalDebtToEquity" -> stock.totalDebtToEquity
                    else -> return@all true
                }

                when (filter.operator.trim()) {
                    "Between two values" -> {
                        (filter.min == null || value >= filter.min) &&
                                (filter.max == null || value <= filter.max)
                    }
                    "Greater than equal to" -> filter.min?.let { value >= it } ?: true
                    "Less than equal to" -> filter.max?.let { value <= it } ?: true
                    else -> true
                }
            }
        }
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
                // Unsupported fields (not available in data class) return null
                else -> null
            }
        }

        return if (values.isNotEmpty()) values.average() else 0.0
    }
}