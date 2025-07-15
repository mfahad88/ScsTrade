package com.example.scstrade.views.stockscreener.fundamental

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityFundamentalBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.stockscreener.fundamental.adapter.FundamentalAdapter
import com.example.scstrade.views.widgets.VerticalSpaceItemDecoration

class FundamentalFragment : Fragment() {
    lateinit var binding: ActivityFundamentalBinding
    lateinit var viewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ActivityFundamentalBinding.inflate(inflater,container,false)
        viewModel = (requireActivity().application as MyApp).viewModel

        // binding.toolbar.toggleToolbar(false)


        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(
                VerticalSpaceItemDecoration(1,
                    ContextCompat.getColor(requireContext(),R.color.md_theme_outline))
            )
        }
        viewModel.getFundamental()
        viewModel.mutableFundamental.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility  = View.GONE
                    Utils.showError(binding.root,result.message?:"An Error Occurred")
                }
                is Resource.Loading -> binding.loader.visibility  = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility  = View.GONE
                    binding.recyclerView.apply {
                        adapter = FundamentalAdapter(result.data?: emptyList()) {
                            val intent = Intent(
                                requireContext(),
                                FundamentalDetailActivity::class.java
                            )
                            intent.putExtra(AppConstants.TECHNICAL_SELECTION, it.fundamentals)
                            startActivity(intent)
                        }


                    }
                }
            }
        })
        return binding.root
    }


}