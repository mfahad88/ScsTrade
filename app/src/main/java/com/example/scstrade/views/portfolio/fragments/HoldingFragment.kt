package com.example.scstrade.views.portfolio.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentHoldingBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.activities.StockDetailActivity
import com.example.scstrade.views.portfolio.adapter.HoldingAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [HoldingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HoldingFragment : Fragment() {
    lateinit var binding:FragmentHoldingBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var stockDetailActivity: StockDetailActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        stockDetailActivity=(requireActivity() as StockDetailActivity)
        sharedViewModel.getPortfolioItemDetail(stockDetailActivity.portfolioMainID,stockDetailActivity.symbol)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHoldingBinding.inflate(inflater)

        binding.buyTransac.text = getString(R.string.buy_transac,stockDetailActivity.symbol)
        sharedViewModel.mutablePortfolioItemDetail.observe(viewLifecycleOwner, Observer {result->

            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding.recyclerView.apply {

                        adapter = HoldingAdapter(result.data?: emptyList()){

                        }
                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    }
                }
            }

        })
        return binding.root
    }


}