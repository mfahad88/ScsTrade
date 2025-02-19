package com.example.scstrade.views.watchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentWatchListDetailBinding
import com.example.scstrade.databinding.FragmentWatchlistBinding
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
        viewModel=ViewModelProvider(this).get(WatchListViewModel::class.java)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
//        viewModel.getWatchListDetail()
        sharedViewModel.mutableAllData.observe(viewLifecycleOwner, Observer { result->

        })
        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(20))
        }
        return binding.root
    }


}