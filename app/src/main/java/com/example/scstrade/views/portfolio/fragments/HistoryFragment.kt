package com.example.scstrade.views.portfolio.fragments
import android.content.Intent
import android.graphics.Color
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHistoryBinding.inflate(inflater)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity = (requireActivity() as StockDetailActivity)
        portFolioViewModel = stockDetailActivity.portFolioViewModel
        portFolioViewModel.getDividend(stockDetailActivity.portfolioMainID.toString())
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
                          mainContent.setContent {
                              populateDividend(data)
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
                                 binding.recyclerView.adapter=HistoryAdapter(res.data?.closeTrades?: emptyList()){

                                 }
                                }
                            }

                        })
                        loader.visibility = View.GONE
                        mainContainer.visibility = View.VISIBLE
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