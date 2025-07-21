package com.example.scstrade.views.portfolio.fragments
import android.content.Intent
import android.graphics.Color
import androidx.compose.ui.res.dimensionResource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentHistoryBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.portfolio.CloseTrade
import com.example.scstrade.model.response.portfolio.DividendItem
import com.example.scstrade.model.response.portfolio.FifoPortfolio
import com.example.scstrade.model.response.portfolio.PortfolioItemDetail
import com.example.scstrade.viewmodels.PortFolioViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.activities.BuySellActivity
import com.example.scstrade.views.portfolio.activities.StockDetailActivity
import com.example.scstrade.views.portfolio.adapter.HistoryAdapter
import com.example.scstrade.views.portfolio.adapter.HoldingAdapter
import com.example.scstrade.views.widgets.SideBarDivider
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    lateinit var binding:FragmentHistoryBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var stockDetailActivity: StockDetailActivity
    lateinit var portFolioViewModel: PortFolioViewModel

    override fun onResume() {
        super.onResume()
        portFolioViewModel.getDividend(stockDetailActivity.portfolioMainID.toString())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHistoryBinding.inflate(inflater)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity = (requireActivity() as StockDetailActivity)
        portFolioViewModel = stockDetailActivity.portFolioViewModel

//        sharedViewModel.getHistory(stockDetailActivity.portfolioMainID.toString())

        binding.apply {
            buyTransac.text = getString(R.string.sell_trades,stockDetailActivity.symbol)
            dividends.text = getString(R.string.dividend_trades,stockDetailActivity.symbol)
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                val divider = SideBarDivider(
                    dividerColor = Color.parseColor("#B3C6C6CD"),
                    marginEnd = 10
                )
                addItemDecoration(divider)

            }
        }


        portFolioViewModel.mutablePortfolioFinalDetailOnce.observe(viewLifecycleOwner, Observer { res->
            when(res){
                is Resource.Error -> {
                    Utils.showError(requireView(),res.message)
                }
                is Resource.Loading -> {}
                is Resource.Success ->
                {
                    if(!res.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }?.toList().isNullOrEmpty()){
                        binding.materialCardViewSell.visibility = View.VISIBLE
                        binding.recyclerView.adapter=HistoryAdapter(res.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }?.toList()?: emptyList()){

                        }
                    }else{
                        binding.noRecordFound.visibility = View.VISIBLE
                    }

                    portFolioViewModel.mutableDividend.observe(viewLifecycleOwner, Observer {result->
                        when(result){
                            is Resource.Error -> {
                                Utils.showError(requireView(),result.message)
                                binding.loader.visibility = View.GONE
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                binding.apply {


                                    val data=result.data
                                    if(!data.isNullOrEmpty()){
                                        binding.materialCardViewDividend.visibility = View.VISIBLE
                                        mainContent.setContent {
                                            populateDividend(data.filter { it.dividendSymbol.substringBefore("-").equals(stockDetailActivity.symbol,true) })
                                        }


                                    }
                                    val summaryMap =   calculateHistoryAndDividend(stockDetailActivity.symbol,res.data?.closeTrades?: emptyList(),result.data?: emptyList())

                                    val purchaseCost = summaryMap["PurchaseCost"] ?: 0.0
                                    val soldValue = summaryMap["SoldValue"] ?: 0.0
                                    val pnl = summaryMap["P&L"] ?: 0.0
                                    val profitBooked = summaryMap["ProfitBooked"] ?: 0.0
                                    val lossBooked = summaryMap["LossBooked"] ?: 0.0
                                    val historicalGain = summaryMap["HistoricalGain"] ?: 0.0
                                    val dividendEarning = summaryMap["DividendEarning"] ?: 0.0

                                    binding.historicalGain.tvTitle.text = getString(R.string.historical_,stockDetailActivity.symbol)
                                    binding.historicalGain.setValue("%,d".format(historicalGain.roundToInt()))
                                    binding.profitBook.setValue("%,d".format(profitBooked.roundToInt()))
                                    binding.lossBooked.setValue("%,d".format(lossBooked.roundToInt()))
                                    binding.dividendEa.setValue("%,d".format(dividendEarning.roundToInt()))
                                    binding.purchasedCValue.text=String.format("%,d",purchaseCost.roundToInt())
                                    binding.soldValue.text = String.format("%,d",soldValue.roundToInt())
                                    loader.visibility = View.GONE
                                    mainContainer.visibility = View.VISIBLE
                                }
                            }
                        }
                    })
                }
            }

        })





        return binding.root
    }

    fun calculateHistoryAndDividend(
        symbol: String,
        closeTrades: List<CloseTrade>,
        dividendMap: List<DividendItem>
    ): Map<String, Double> {
        var totalPur = 0.0
        var totalSal = 0.0
        var profitBooked = 0.0
        var lossBooked = 0.0

     /*   val historicalGain = closeTrades.filter { it.symbol.equals(symbol,true) }.sumOf {
            it.salAmount.toDouble().minus(it.purAmount.toDouble())
        }*/

        closeTrades.filter { it.symbol.equals(symbol,true) }.forEach { trade ->
            val pur = trade.purAmount.toDoubleOrNull() ?: 0.0
            val sal = trade.salAmount.toDoubleOrNull() ?: 0.0
            val pnl = sal - pur

            totalPur += pur
            totalSal += sal

            if (pnl > 0) profitBooked += pnl
            else lossBooked += pnl
        }


        val historicalGain = profitBooked + lossBooked // net P&L

        // Dividend Calculation
        val totalDividend= dividendMap.filter { it.dividendSymbol.substringBefore("-").equals(stockDetailActivity.symbol,true) }.sumOf { (it.dividendPerShare * it.dividendQuantity)}
        /*   val dividendPerShare = dividendMap[symbol] ?: 0.0
           val totalDividend = fifoList.filter { it.symbol == symbol }
               .sumOf { it.quantity.toDoubleOrNull() ?: 0.0 } * dividendPerShare*/

        return mapOf(
            "PurchaseCost" to totalPur,
            "SoldValue" to totalSal,
            "P&L" to totalSal - totalPur,
            "ProfitBooked" to profitBooked,
            "LossBooked" to lossBooked,
            "HistoricalGain" to historicalGain,
            "DividendEarning" to totalDividend
        )
    }
    @Composable
    private fun populateDividend(data: List<DividendItem>) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.md_theme_surface))
                .padding(8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HeaderText("Date", Modifier.weight(1f))
                HeaderText("Shares", Modifier.weight(0.7f))
                HeaderText("Div Per Share", Modifier.weight(1f))
                HeaderText("Total Dividend", Modifier.weight(1f))
            }



            // Data rows
            data.forEach { item ->
                Row(
                    modifier = Modifier

                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DataText(
                        text = Utils.convertDateString(item.dividendDate, "dd-MMM-yyyy"),
                        modifier = Modifier.weight(1f)
                    )
                    DataText(
                        text = item.dividendQuantity.toString(),
                        modifier = Modifier.weight(0.7f)
                    )
                    DataText(
                        text = item.dividendPerShare.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    DataText(
                        text = String.format("%,d", (item.dividendPerShare * item.dividendQuantity).roundToInt()),
                        modifier = Modifier.weight(1f)
                    )
                }
                Divider(color = colorResource(R.color.gray_400), thickness = 1.dp)
            }

        }
    }

    @Composable
    private fun HeaderText(text: String, modifier: Modifier) {
        Text(
            text = text,
            modifier = modifier,
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
            color = colorResource(R.color.gray_600)
        )
    }

    @Composable
    private fun DataText(text: String, modifier: Modifier) {
        Text(
            text = text,
            modifier = modifier,
            fontSize = dimensionResource(R.dimen.sp_16).value.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.custom_font)),
            color = colorResource(R.color.black)
        )
    }


}