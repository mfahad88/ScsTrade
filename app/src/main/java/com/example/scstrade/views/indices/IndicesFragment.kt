package com.example.scstrade.views.indices

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentIndicesBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.allstock.AllStockFragment
import com.example.scstrade.views.allstock.StockActivity
import com.example.scstrade.views.market.MarketFragment
import com.google.gson.reflect.TypeToken

class IndicesFragment : Fragment() {

    lateinit var binding: FragmentIndicesBinding
    private lateinit var viewModel:  SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIndicesBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        binding.recyclerView.apply {
            visibility= View.VISIBLE
            val typeToken = object:TypeToken<List<KSEIndices>>(){}
            adapter=IndicesAdapter(emptyList()){ kseIndices ->
                var bundle=Bundle()
                bundle.putString("index",kseIndices.iNDEXCODE)
                val fragment =AllStockFragment()
                fragment.arguments=bundle
//                findNavController().navigate(R.id.stockFragment,bundle)
                (parentFragment as MarketFragment).loadFragment(fragment)
            }
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }

        viewModel.mutableIndices.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility= View.GONE
                    binding.recyclerView.visibility= View.GONE
                }
                is Resource.Loading -> {
                    binding.loader.visibility= View.VISIBLE
                    binding.recyclerView.visibility= View.GONE
                }
                is Resource.Success -> {
                    binding.loader.visibility= View.GONE

                    binding.recyclerView.apply {
                        visibility= View.VISIBLE
                        adapter=IndicesAdapter(result.data?: emptyList()){kseIndices ->
                            var bundle=Bundle()
                            bundle.putString("index",kseIndices.iNDEXCODE)
                            val intent= Intent(requireContext(),StockActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
//                            findNavController().navigate(R.id.stockFragment,bundle)
                        }
                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    }
                }
            }

        })
        return binding.root
    }


}