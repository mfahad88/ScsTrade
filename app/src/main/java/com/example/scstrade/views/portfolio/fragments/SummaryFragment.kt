package com.example.scstrade.views.portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentSummaryBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.activities.StockDetailActivity


/**
 * A simple [Fragment] subclass.
 * Use the [SummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SummaryFragment : Fragment() {
    lateinit var binding : FragmentSummaryBinding
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var stockDetailActivity: StockDetailActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(inflater)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity= requireActivity() as StockDetailActivity
        sharedViewModel.mutablePortfolioDetail.observe(viewLifecycleOwner, Observer { result->
            when (result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                  val holdingCost=  result.data?.groupBy { it.portfolioSymbol }?.mapValues { (_,items) -> items.sumOf { if(it.portfolioType.equals("buy",true)){
                        (it.portfolioQuantity.times(it.portfolioRate))+it.portfolioCommission
                    }else{
                        0.0
                    }
                    } }?.values?.first()
                    val currentPrice=sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(stockDetailActivity.symbol,true) }?.first()?.cL
                    val shares=  result.data?.groupBy { it.portfolioSymbol }?.mapValues { (_,items) -> items.sumOf { if(it.portfolioType.equals("buy",true)){
                        it.portfolioQuantity
                    }else{
                        0
                    }
                    } }?.values?.first()
                    val historyCost= result.data?.groupBy { it.portfolioSymbol }?.mapValues { (_,items) -> items.sumOf { if(it.portfolioType.equals("sell",true)){
                        it.portfolioRate.times(it.portfolioQuantity)
                    }else{
                        0.0
                    }
                    } }?.values?.first()
                    binding.historyCost.text = "${historyCost}"
                    binding.holdingCost.text = "${holdingCost}"
                    binding.holdingValue.text = "${currentPrice?.times(shares!!)}"
                    binding.holdingPL.text = "${(currentPrice!!.times(shares!!))- (holdingCost!!)}"
                    if(binding.holdingPL.text.contains("-")){
                        binding.holdingPL.setTextColor(requireContext().getColor(R.color.md_theme_errorContainer))
                    }else{
                        binding.holdingPL.setTextColor(requireContext().getColor(R.color.md_theme_primary))
                    }
                }
            }
        })
        return binding.root
    }


}