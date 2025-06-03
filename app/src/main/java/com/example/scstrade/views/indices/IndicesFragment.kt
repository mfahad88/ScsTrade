package com.example.scstrade.views.indices

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.allstock.AllStockFragment
import com.example.scstrade.views.allstock.StockActivity
import com.example.scstrade.views.landing.LandingFragment
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

//        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel = (requireActivity().application as MyApp).viewModel

        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView){v,insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }


        binding.recyclerView.apply {
            visibility= View.VISIBLE
            val typeToken = object:TypeToken<List<KSEIndices>>(){}
            adapter=IndicesAdapter(emptyList()){kseIndices ->
                var bundle=Bundle()
                bundle.putString("index",kseIndices.iNDEXCODE)
                val intent= Intent(requireContext(),StockActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
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
                        (adapter as IndicesAdapter).addItem(result.data?: emptyList())
                    }
                }
            }

        })
        return binding.root
    }


}