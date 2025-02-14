package com.example.scstrade.views.allstock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.FragmentAllStockBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class AllStockFragment : Fragment() {
    private lateinit var binding: FragmentAllStockBinding
    private val viewModel:SharedViewModel by activityViewModels()
    private var selectedSector:String?=null
    var sector:String?=null
    var kmi:String?=null
    private var isFirst=true
    private var list= emptyList<StockItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllStockBinding.inflate(inflater,container,false)
        initSelection()
        sector=arguments?.getString("sector")?:null
        kmi=arguments?.getString("kmi")?:null
        binding.recyclerIndices.apply {
            adapter= StockAdapter(emptyList())
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30))
        }

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(binding.tabLayout.selectedTabPosition){

                    0-> (binding.recyclerIndices.adapter as StockAdapter).addItems(list?: emptyList())
                    1 -> (binding.recyclerIndices.adapter as StockAdapter).addItems(list.sortedByDescending { it.v }?: emptyList())
                    2 -> (binding.recyclerIndices.adapter as StockAdapter).addItems(list.sortedByDescending { it.cHP }?: emptyList())
                    3 -> (binding.recyclerIndices.adapter as StockAdapter).addItems(list.sortedBy { it.cHP }?: emptyList())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        viewModel.mutableAllData.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if(sector!=null){
                        list=result.data?.filter { it.sN.equals(sector) }?: emptyList()
                    }else if(kmi!=null){
                        list = result.data?.filter { it.iN.contains("kmi",true) }?: emptyList()
                    } else {
                        list = result.data ?: emptyList()
                    }
                    when(binding.tabLayout.selectedTabPosition){

                        0-> (binding.recyclerIndices.adapter as StockAdapter).addItems(list?: emptyList())
                        1 -> (binding.recyclerIndices.adapter as StockAdapter).addItems(list.sortedByDescending { it.v }?: emptyList())
                        2 -> (binding.recyclerIndices.adapter as StockAdapter).addItems(list.sortedByDescending { it.cHP }?: emptyList())
                        3 -> (binding.recyclerIndices.adapter as StockAdapter).addItems(list.sortedBy { it.cHP }?: emptyList())
                    }
//                    (binding.recyclerIndices.adapter as StockAdapter).addItems(list?: emptyList())
                    binding.loader.visibility=View.GONE
                    binding.recyclerIndices.visibility = View.VISIBLE

                }
            }
        })


        return binding.root
    }

    private fun initSelection() {
        binding.tabLayout.getTabAt(0)?.select()
    }


}