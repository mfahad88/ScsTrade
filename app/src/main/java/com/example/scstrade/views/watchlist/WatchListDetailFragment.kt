package com.example.scstrade.views.watchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentWatchListDetailBinding
import com.example.scstrade.databinding.FragmentWatchlistBinding
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.views.allstock.StockAdapter
import com.example.scstrade.views.widgets.HorizontalDivider


class WatchListDetailFragment : Fragment() {
    lateinit var binding:FragmentWatchListDetailBinding
    lateinit var viewModel: WatchListViewModel
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchListDetailBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(requireActivity()).get(WatchListViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.getWatchListDetail(viewModel.selectedItem?.watchListMainID?:0)
        viewModel.mutableWatchListDetail.observe(viewLifecycleOwner, Observer {detail->

            (binding.recyclerView.adapter as StockAdapter).addItems(detail.data?: emptyList())
        })
        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations=false
            addItemDecoration(HorizontalDivider(20))
            adapter=StockAdapter( emptyList(),true)
        }
        return binding.root
    }


}