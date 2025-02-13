package com.example.scstrade.views.allstock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAllStockBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.stock.StockAdapter
import com.example.scstrade.views.widgets.HorizontalDivider

class AllStockFragment : Fragment() {
    private lateinit var binding: FragmentAllStockBinding
    private val viewModel:SharedViewModel by activityViewModels()
    private var selectedSector:String?=null
    private var isFirst=true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllStockBinding.inflate(inflater,container,false)
        binding.recyclerIndices.apply {
            adapter=StockAdapter(emptyList())
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30))
        }
        viewModel.mutableAllData.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {


                    var sectors=result.data?.distinctBy {
                        it.sN
                    }?.map {
                        it.sN
                    }
                    if(binding.sector.adapter==null){
                        val adapter=ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line,sectors?: emptyList())
//                        adapter.setDropDownViewResource(R.layout.custom_spinner)
                        binding.sector.adapter=adapter
                        selectedSector=binding.sector.getItemAtPosition(0).toString()
                    }
                    binding.sector.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            selectedSector = p0?.adapter?.getItem(p2).toString()
                            (binding.recyclerIndices.adapter as StockAdapter).addItems(result.data?.filter { it.sN==selectedSector }?.toList()?: emptyList())
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }

                    }
//                    (binding.recyclerIndices.adapter as StockAdapter).addItems(result.data?.filter { it.sN==selectedSector }?.toList()?: emptyList())
                    (binding.recyclerIndices.adapter as StockAdapter).addItems(result.data?: emptyList())
                }
            }
        })


        return binding.root
    }


}