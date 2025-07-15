package com.example.scstrade.views.stockscreener.customscreener

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentCustomScreenerBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stockscreener.StockScreenerItem
import com.example.scstrade.viewmodels.HomeViewModel
import com.example.scstrade.viewmodels.StockScreenerViewModel
import com.example.scstrade.views.widgets.HorizontalDivider


class CustomScreenerFragment : Fragment() {
    lateinit var binding:FragmentCustomScreenerBinding
    lateinit var viewModel:StockScreenerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(StockScreenerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomScreenerBinding.inflate(inflater,container,false)
        setupRecycler()
        viewModel.getStockScreener()
        viewModel.mutableStockScreener.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(requireView(),result.message)
                }
                is Resource.Loading ->{}
                is Resource.Success -> {
                   /* binding.recyclerView.adapter=CustomScreenerAdapter(result.data?.toMutableList()){

                    }*/
                }
            }
        })
        return binding.root
    }

    private fun setupRecycler() {
        /*binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(10.dp))

        }*/
    }


}