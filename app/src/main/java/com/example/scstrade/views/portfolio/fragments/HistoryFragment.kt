package com.example.scstrade.views.portfolio.fragments
import android.content.Intent
import androidx.compose.ui.res.dimensionResource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.example.scstrade.model.response.portfolio.DividendItem
import com.example.scstrade.model.response.portfolio.PortfolioItemDetail
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.activities.BuySellActivity
import com.example.scstrade.views.portfolio.activities.StockDetailActivity
import com.example.scstrade.views.portfolio.adapter.HistoryAdapter
import com.example.scstrade.views.portfolio.adapter.HoldingAdapter
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHistoryBinding.inflate(inflater)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity = (requireActivity() as StockDetailActivity)
//        sharedViewModel.getHistory(stockDetailActivity.portfolioMainID.toString())

        binding.apply {
            buyTransac.text = getString(R.string.sell_trades,stockDetailActivity.symbol)
            dividends.text = getString(R.string.dividend_trades,stockDetailActivity.symbol)
            recyclerView.apply {
                layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))
            }
        }

        sharedViewModel.mutableHistory.observe(viewLifecycleOwner, Observer { res->
            when(res.dividendItem){
                is Resource.Error -> Utils.showError(binding.root,res.dividendItem.message?:"An error occurred")
                is Resource.Loading -> {
                }
                is Resource.Success ->{
                    val result=res.dividendItem
                    binding.dividendEa.setValue( Utils.roundTwoDecimal(result.data!!.filter { it.dividendSymbol.contains(stockDetailActivity.symbol,true) }.sumOf {
                        it.dividendPerShare
                    }.toDouble()))
                    binding.mainContent.setContent {
                        populateDividend(result.data?.filter { it.dividendSymbol.contains(stockDetailActivity.symbol,true) }?: emptyList())
                        if(result.data?.isNotEmpty()?:false){
                            binding.materialCardViewDividend.visibility = View.VISIBLE
                        }else{
                            binding.materialCardViewDividend.visibility = View.GONE
                        }
                    }
                }
            }

            when(res.portfolioDetails){
                is Resource.Error -> Utils.showError(binding.root,res.portfolioDetails.message?:"An error occurred")
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val result=res.portfolioDetails
                    try {
                        binding.loader.visibility = View.GONE
                        binding.mainContainer.visibility = View.VISIBLE
                        if(!result.data?.closeTrades.isNullOrEmpty()){

                            val netPL=  result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { (it.salAmount.toDouble() - it.purAmount.toDouble()) }
                            val percentPL = (netPL.div(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { it.purAmount.toDouble() })).times(100)
                            val sumSellPrice = result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { (it.salAmount.toDouble()) }
                            val sumDividend = sharedViewModel.mutableDividend.value?.data?.filter { it.dividendSymbol.contains(stockDetailActivity.symbol,true) }?.sumOf { (it.dividendPerShare) }
                            val totalPurchase = result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { it.purAmount.toDouble()}
                            val historicalGain = sumSellPrice.plus(sumDividend!!).minus(totalPurchase)
                            val soldValue = result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { it.salAmount.toDouble()}
                            val historPL=(soldValue).minus(totalPurchase)
                            binding.profitBook.setValue(Utils.roundTwoDecimal(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf {
                                if((it.salAmount.toDouble() - it.purAmount.toDouble())>0){ (it.salAmount.toDouble() - it.purAmount.toDouble()) }else{ 0.00 } }
                            ))

                          /*  binding.netPLOnValue.text = "${Utils.commaSeparated(historPL.roundToInt())} (${
                                Utils.roundTwoDecimal((historPL.div(totalPurchase))?.times(100))
                            }%)"*/
                            binding.lossBooked.setValue(Utils.roundTwoDecimal(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf {
                                if((it.salAmount.toDouble() - it.purAmount.toDouble())<0){ (it.salAmount.toDouble() - it.purAmount.toDouble()) }else{ 0.00 } }
                            ))
                            binding.purchasedCValue.text = Utils.roundTwoDecimal(totalPurchase)

                            binding.soldValue.text = Utils.roundTwoDecimal(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf {
                                it.salAmount.toDouble()})

                            val sumPL= result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { item -> (item.salAmount.toDouble() - item.purAmount.toDouble()) }
                            if(totalPurchase!=null && totalPurchase>0.0) {
                                binding.historicalGain.setValue(Utils.roundTwoDecimal(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf {
                                    it.salAmount.toDouble()}))
                            }else{
                                binding.historicalGain.setValue("0.0 (0.0%)")
                            }
                            if(binding.historicalGain.tvValue.text.toString().contains("-")){
                                binding.historicalGain.tvValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.md_theme_errorContainer))
                            }else{
                                binding.historicalGain.tvValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.md_theme_primary))
                            }
                            /*if(percentPL!=null && percentPL>0.0){
                                binding.netPLOnValue.text = "${Utils.roundTwoDecimal(netPL)}" +
                                        "(${Utils.roundTwoDecimal(percentPL)}%)"

                            }else {
                                binding.netPLOnValue.text = "0.0 (0.0%)"
                            }*/

                            binding.recyclerView.apply {
                                adapter = HistoryAdapter(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }?.toList()?: emptyList()){
                                }
                                if(result.data?.closeTrades?.isNotEmpty()?:false){
                                    binding.materialCardViewSell.visibility = View.VISIBLE
                                }else{
                                    binding.materialCardViewSell.visibility = View.GONE
                                }
                            }
                        }


                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }

        })


        sharedViewModel.mutablePortfolioDetails.observe(viewLifecycleOwner, Observer {result->

            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.recyclerView.apply {

                        val shares=result.data?.filter { it.portfolioSymbol.equals(stockDetailActivity.symbol) }?.filter { it.portfolioType.equals("buy",true) }?.map { it.portfolioQuantity.toDouble() }?.sumOf { it }
                        val purchaseCost= result.data?.filter { it.portfolioSymbol.equals(stockDetailActivity.symbol) }?.filter { it.portfolioType.equals("buy",true) }?.map { (it.portfolioRate.times(it.portfolioQuantity.toDouble())) }?.sumOf { it }
                        val avgBuyPrice = purchaseCost?.div(shares?:0.0)
                        val currentPrice = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cL }?.first()
                        val currentMarketValue = currentPrice?.times(shares?:0.0)

                        val daysPercentPL = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cHP }?.first()
                        val totalPL= currentMarketValue?.minus(purchaseCost?:0.0)
                        val totalPercentPL= (totalPL?.div(purchaseCost?:0.0))?.times(100)
                        val fifo=sharedViewModel.mutablePortfolioFinalDetail.value?.data?.fifoPortfolio?.filter { it.symbol.equals(stockDetailActivity.symbol) }?.first()
                        val daysPL = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.map { it.cH }?.first()?.times(fifo?.quantity?.toDouble()?:0.0)

                        val list= mutableListOf<PortfolioItemDetail>()

                        result.data?.filter { it.portfolioSymbol.equals(stockDetailActivity.symbol) }?.filter { it.portfolioType.equals("buy",true) }?.forEach {
                            list.add(
                                PortfolioItemDetail(it.portfolioDate,it.portfolioQuantity.toString(),it.portfolioRate.toString(),(currentPrice!!.minus(it.portfolioRate)).times(it.portfolioQuantity.toDouble()),
                                ((currentPrice.times(it.portfolioQuantity.toInt()).minus(it.portfolioRate.toDouble().times(it.portfolioQuantity.toInt()))).div(it.portfolioRate.toDouble().times(it.portfolioQuantity.toInt()))).times(100)
                                ,null)
                            )
                        }


                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    }
                }
            }

        })

        return binding.root
    }
    @Composable
    private fun populateDividend(data: List<DividendItem>) {
        Column (modifier = Modifier
            .fillMaxSize()){
            data.forEach {item->
               Card (elevation = dimensionResource(R.dimen.dp_5).value.dp, content = {
                   Row(modifier = Modifier.background(color = colorResource(id = R.color.md_theme_background))
                   ){
                       Box(modifier = Modifier
                           .weight(1f)
                           .height(dimensionResource(R.dimen.dp_50).value.dp)){
                           Text(
                               text = Utils.convertDateString(item.dividendDate,"dd-MMM-yy"),
                               style = TextStyle(
                                   fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                                   fontFamily = FontFamily(Font(R.font.custom_font)),
                                   fontWeight = FontWeight(500),
                                   color = colorResource(R.color.black),
                               )
                           )
                       }

                       Box(modifier = Modifier
                           .weight(0.7f)
                           .height(dimensionResource(R.dimen.dp_50).value.dp)){
                           Text(
                               text = item.dividendQuantity.toString(),
                               style = TextStyle(
                                   fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                                   fontFamily = FontFamily(Font(R.font.custom_font)),
                                   fontWeight = FontWeight(500),
                                   color = colorResource(R.color.black),
                               )
                           )
                       }

                       Box(modifier = Modifier
                           .weight(1f)
                           .height(dimensionResource(R.dimen.dp_50).value.dp)){
                           Text(
                               text = item.dividendPerShare.toString(),
                               style = TextStyle(
                                   fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                                   fontFamily = FontFamily(Font(R.font.custom_font)),
                                   fontWeight = FontWeight(500),
                                   color = colorResource(R.color.black),
                               )
                           )
                       }

                       Box(modifier = Modifier
                           .weight(1f)
                           .height(dimensionResource(R.dimen.dp_50).value.dp)){
                           Text(
                               text = (item.dividendPerShare*item.dividendQuantity).toString(),
                               style = TextStyle(
                                   fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                                   fontFamily = FontFamily(Font(R.font.custom_font)),
                                   fontWeight = FontWeight(500),
                                   color = colorResource(R.color.black),
                               )
                           )
                       }

                   }
               })

            }
        }
    }


}