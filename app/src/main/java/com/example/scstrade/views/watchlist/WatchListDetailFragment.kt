package com.example.scstrade.views.watchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentWatchListDetailBinding
import com.example.scstrade.databinding.FragmentWatchlistBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.response.watchList.WatchListDetailItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.views.allstock.StockAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class WatchListDetailFragment : Fragment() {
    lateinit var binding:FragmentWatchListDetailBinding
    lateinit var viewModel: WatchListViewModel
    lateinit var sharedViewModel: SharedViewModel
    var list= emptyList<WatchListDetailItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchListDetailBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(requireActivity()).get(WatchListViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.getWatchListDetail(requireContext(),viewModel.selectedItem?.watchListMainID?:0)

        viewModel.mutableSymDelete.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.getWatchListDetail(requireContext(),viewModel.selectedItem?.watchListMainID?:0)
                }
            }
        })
        viewModel.mutableWatchDetail.observe(viewLifecycleOwner, Observer {
            list=it
        })
        viewModel.mutableWatchListDetail.observe(viewLifecycleOwner, Observer {detail->
            (binding.recyclerView.adapter as WatchListDetailAdapter).addItems(detail.data?: emptyList())
        })
        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(20))
            adapter=WatchListDetailAdapter( emptyList()){str,item->
                if(str.contains("delete",true)){
                    val stockItem=list.filter { it.watchListSymbol.equals(item.sYM,true) }.first()
                    viewModel.deleteSymbol(stockItem.watchListDetailID)
                }
            }
            (binding.recyclerView.adapter as WatchListDetailAdapter).getItemTouchHelper().attachToRecyclerView(this)
        }

        sharedViewModel.mutableIndices.observe(requireActivity(), Observer {
            updateMarket(it)
        })
        return binding.root
    }
    private fun updateMarket(it: Resource<List<KSEIndices>>) {
        binding.mMarket.apply {
            if(it.data?.isNotEmpty()?:false){
                if(it.data?.first()?.marketStatus.equals("CLOSE",true)){
                    close.visibility= View.VISIBLE
                    open.visibility = View.GONE
                }else{
                    close.visibility= View.GONE
                    open.visibility = View.VISIBLE
                }
                var sdf = SimpleDateFormat("dd MMM yyyy | hh:mma", Locale.ENGLISH);

                // Get the current date and time
                var formattedDate = sdf.format(Date())
                dateTime.text = formattedDate
            }
        }
    }

}