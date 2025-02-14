package com.example.scstrade.views.sector

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentSectorBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.allstock.StockActivity


class SectorFragment : Fragment() {
    private val sharedViewModel:SharedViewModel by activityViewModels()
    lateinit var binding: FragmentSectorBinding
    lateinit var observer: Observer<Resource<List<StockItem>>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSectorBinding.inflate(inflater,container,false)
        binding.recyclerViewSector.apply {
            adapter=SectorAdapter(emptyList()){
                val intent= Intent(requireContext(),StockActivity::class.java)
                val bundle =Bundle()
                bundle.putString("sector",it)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
        observeData()
        sharedViewModel.mutableAllData.observe(viewLifecycleOwner, observer)

        return binding.root
    }

    private fun observeData(){
        observer=Observer {

            (binding.recyclerViewSector.adapter as SectorAdapter).addItems(it.data?.map { it.sN }?.distinct()?.sortedBy { it }?: emptyList())
            sharedViewModel.mutableAllData.removeObserver(observer)
        }
    }
}