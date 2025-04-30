package com.example.scstrade.views.portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.portfolio.DividendItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.activities.StockDetailActivity
import com.example.scstrade.views.portfolio.adapter.HistoryAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    lateinit var binding:FragmentHistoryBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var stockDetailActivity: StockDetailActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity = (requireActivity() as StockDetailActivity)
        sharedViewModel.getDividend(stockDetailActivity.portfolioMainID.toString())
//        sharedViewModel.stopPortfolioFinal()
        sharedViewModel.getPortfolioFinalDetail(stockDetailActivity.portfolioMainID)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater)

        binding.apply {
            buyTransac.text = getString(R.string.sell_trades,stockDetailActivity.symbol)
            dividends.text = getString(R.string.dividend_trades,stockDetailActivity.symbol)
            recyclerView.apply {
                layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))
            }
        }

        sharedViewModel.mutablePortfolioFinalDetail.observe(viewLifecycleOwner, Observer {result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    try {
                        val netPL=  result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { (it.salAmount.toDouble() - it.purAmount.toDouble()) }
                        val percentPL = (netPL.div(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { it.purAmount.toDouble() })).times(100)
                        val sumSellPrice = result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { (it.salAmount.toDouble()) }
                        val sumDividend = sharedViewModel.mutableDividend.value?.data?.filter { it.dividendSymbol.contains(stockDetailActivity.symbol,true) }?.sumOf { (it.dividendPerShare) }
                        val totalPurchase = result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { it.purAmount.toDouble()}
                        val historicalGain = sumSellPrice.plus(sumDividend!!).minus(totalPurchase)
                        binding.profitBookValue.text = Utils.roundTwoDecimal(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf {
                            if((it.salAmount.toDouble() - it.purAmount.toDouble())>0){ (it.salAmount.toDouble() - it.purAmount.toDouble()) }else{ 0.00 } }
                        )
                        binding.lossBookedValue.text =Utils.roundTwoDecimal(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf {
                            if((it.salAmount.toDouble() - it.purAmount.toDouble())<0){ (it.salAmount.toDouble() - it.purAmount.toDouble()) }else{ 0.00 } }
                        )
                        binding.purchasedCValue.text = Utils.roundTwoDecimal(totalPurchase)

                        binding.soldValue.text = Utils.roundTwoDecimal(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf {
                            it.salAmount.toDouble()})

                        val sumPL= result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }!!.toList().sumOf { item -> (item.salAmount.toDouble() - item.purAmount.toDouble()) }

                        binding.historicalValue.text = "${Utils.roundTwoDecimal(historicalGain)} (${(historicalGain.div(totalPurchase)).times(100)}%)"
                        if(binding.historicalValue.text.contains("-")){
                            binding.historicalValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.md_theme_errorContainer))
                        }else{
                            binding.historicalValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.md_theme_primary))
                        }
                        binding.netPLOnValue.text = "${Utils.roundTwoDecimal(netPL)}" +
                                "(${Utils.roundTwoDecimal(percentPL)}%)"

                        binding.recyclerView.apply {
                            adapter = HistoryAdapter(result.data?.closeTrades?.filter { it.symbol.equals(stockDetailActivity.symbol,true) }?.toList()?: emptyList()){
                            }
                            if(result.data?.closeTrades?.isNotEmpty()?:false){
                                binding.materialCardViewSell.visibility = View.VISIBLE
                            }else{
                                binding.materialCardViewSell.visibility = View.GONE
                            }
                        }
                        binding.loader.visibility = View.GONE
                        binding.mainContainer.visibility = View.VISIBLE
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
        })

        sharedViewModel.mutableDividend.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.dividendEaValue.text = Utils.roundTwoDecimal(result.data!!.filter { it.dividendSymbol.contains(stockDetailActivity.symbol,true) }.sumOf {
                        it.dividendPerShare
                    }.toDouble())
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
        })
        return binding.root
    }
    @Composable
    private fun populateDividend(data: List<DividendItem>) {
        Column (modifier = Modifier.fillMaxSize()){
            data.forEach {item->
               Card (elevation = 5.dp, content = {
                   Row {
                       Box(modifier = Modifier
                           .weight(1f)
                           .height(50.dp)){
                           Text(
                               text = Utils.convertDateString(item.dividendDate,"dd-MMM-yy"),
                               style = TextStyle(
                                   fontSize = 16.sp,
                                   fontFamily = FontFamily(Font(R.font.custom_font)),
                                   fontWeight = FontWeight(500),
                                   color = colorResource(R.color.colorDarkerr),
                               )
                           )
                       }

                       Box(modifier = Modifier
                           .weight(0.7f)
                           .height(50.dp)){
                           Text(
                               text = item.dividendQuantity.toString(),
                               style = TextStyle(
                                   fontSize = 16.sp,
                                   fontFamily = FontFamily(Font(R.font.custom_font)),
                                   fontWeight = FontWeight(500),
                                   color = colorResource(R.color.colorDarkerr),
                               )
                           )
                       }

                       Box(modifier = Modifier
                           .weight(1f)
                           .height(50.dp)){
                           Text(
                               text = item.dividendPerShare.toString(),
                               style = TextStyle(
                                   fontSize = 16.sp,
                                   fontFamily = FontFamily(Font(R.font.custom_font)),
                                   fontWeight = FontWeight(500),
                                   color = colorResource(R.color.colorDarkerr),
                               )
                           )
                       }

                       Box(modifier = Modifier
                           .weight(1f)
                           .height(50.dp)){
                           Text(
                               text = (item.dividendPerShare*item.dividendQuantity).toString(),
                               style = TextStyle(
                                   fontSize = 16.sp,
                                   fontFamily = FontFamily(Font(R.font.custom_font)),
                                   fontWeight = FontWeight(500),
                                   color = colorResource(R.color.colorDarkerr),
                               )
                           )
                       }

                   }
               })

            }
        }
    }


}