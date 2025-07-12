package com.example.scstrade.views.portfolio.fragments
import androidx.compose.ui.res.dimensionResource

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentSellBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.portfolio.activities.BuySellActivity
import com.example.scstrade.views.portfolio.activities.EditBuySellActivity
import com.example.scstrade.views.portfolio.adapter.BuyAdapter
import com.example.scstrade.views.portfolio.adapter.SellAdapter

class SellFragment : Fragment() {
    lateinit var binding:FragmentSellBinding
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSellBinding.inflate(inflater,container,false)
        sharedViewModel = (requireActivity() as EditBuySellActivity).sharedViewModel
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

        }
        sharedViewModel.mutablePortfolioDetails.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.recyclerView.apply {

                        adapter = SellAdapter(result.data?.filter { it.portfolioSymbol.equals((requireActivity() as EditBuySellActivity).symbol,true) }?.filter { it.portfolioType.equals("SELL",true) }?: emptyList(),
                            onItemEdit = {
                                val intent = Intent(requireContext(), BuySellActivity::class.java)
                                intent.putExtra(AppConstants.IS_Sell,true)
                                intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID,it.portfolioMainID)
                                intent.putExtra(AppConstants.MODE,1)
                                intent.putExtra(AppConstants.PORTFOLIO_DETAIL,it)
                                startActivity(intent)
                            },
                            onItemDelete = {
                                sharedViewModel.deleteTrade(it.portfolioMainID,it.portfolioDetailID)
                            })
                    }
                    binding.apply {
                        (requireActivity() as EditBuySellActivity).binding.apply {
                            loader.visibility = View.GONE
                            mainContainer.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
        return binding.root
    }

}