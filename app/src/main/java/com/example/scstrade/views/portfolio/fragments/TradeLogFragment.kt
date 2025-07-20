package com.example.scstrade.views.portfolio.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.util.Util
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentTradeLogBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.portfolio.PortfolioDetails
import com.example.scstrade.viewmodels.PortFolioViewModel
import com.example.scstrade.views.portfolio.activities.BuySellActivity
import com.example.scstrade.views.portfolio.activities.StockDetailActivity
import com.example.scstrade.views.portfolio.adapter.TradingLogAdapter


class TradeLogFragment : Fragment() {

    private lateinit var binding: FragmentTradeLogBinding
    private lateinit var portFolioViewModel: PortFolioViewModel
    private lateinit var stockDetailActivity: StockDetailActivity
    private lateinit var adapter: TradingLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stockDetailActivity = requireActivity() as StockDetailActivity
        portFolioViewModel = stockDetailActivity.portFolioViewModel

    }

    override fun onResume() {
        super.onResume()
        portFolioViewModel.getPortfolioDetails(stockDetailActivity.portfolioMainID)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTradeLogBinding.inflate(inflater, container, false)

        observePortfolioDetails()

        return binding.root
    }

    private fun observePortfolioDetails() {
        portFolioViewModel.mutablePortfolioDetails.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.textNoRecords.visibility = View.GONE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = result.data
                    if (!data.isNullOrEmpty()) {
                        adapter = TradingLogAdapter(data.filter { it.portfolioSymbol.equals(stockDetailActivity.symbol,true) }, onItemEdit = {portfolioDetails,b->
                            if(b){
                                val intent = Intent(requireContext(), BuySellActivity::class.java)
                                intent.putExtra(AppConstants.IS_BUY,true)
                                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID,portfolioDetails.portfolioMainID)
                                intent.putExtra(AppConstants.SYMBOL,stockDetailActivity.symbol)
                                intent.putExtra(AppConstants.MODE,1)
                                intent.putExtra(AppConstants.PORTFOLIO_DETAIL,portfolioDetails)
                                startActivity(intent)

                            } else{
                                val intent = Intent(requireContext(), BuySellActivity::class.java)
                                intent.putExtra(AppConstants.IS_Sell,true)
                                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID,portfolioDetails.portfolioMainID)
                                intent.putExtra(AppConstants.SYMBOL,stockDetailActivity.symbol)
                                intent.putExtra(AppConstants.MODE,1)
                                intent.putExtra(AppConstants.PORTFOLIO_DETAIL,portfolioDetails)
                                startActivity(intent)
                            }
                        }, onItemDelete = {
                            portFolioViewModel.deleteTrade(it.portfolioMainID,it.portfolioDetailID)
                        })
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.textNoRecords.visibility = View.GONE
                    } else {
                        binding.recyclerView.visibility = View.GONE
                        binding.textNoRecords.visibility = View.VISIBLE
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.textNoRecords.visibility = View.GONE
                    Utils.showError(requireView(),result.message)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding
        binding.recyclerView.adapter = null
    }
}
