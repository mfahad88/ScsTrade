package com.example.scstrade.views.stock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentStockBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.repository.IndicesRepository
import com.example.scstrade.services.RetrofitInstance
import com.example.scstrade.viewmodels.IndicesViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.widgets.HorizontalDivider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StockFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StockFragment : Fragment() {
    private lateinit var binding: FragmentStockBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private var index:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchAllData()
        index=arguments?.getString("index")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStockBinding.inflate(inflater, container, false)
        binding.kse100.text=index
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.recyclerStock.apply {
            adapter=StockAdapter(emptyList())
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30))
        }
        viewModel.mutableAllData.observe(viewLifecycleOwner, Observer {result ->
            when(result){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                    val list=   result.data?.filter {it->
                     /*   if(index!!.contains("")){
                            it.iN.contains("")
                        }else*/ if(index!!.lowercase().contains("kse 100")){
                            it.iN.lowercase().contains("kse 100")
                        }else if(index!!.lowercase().contains("kse 30")){
                            it.iN.lowercase().contains("kse 30")
                        }else {
                            it.iN.lowercase().contains("kmi 30")
                        }
                    }
                    (binding.recyclerStock.adapter as StockAdapter).addItem(list?: emptyList())
                }
            }
        })
        return binding.root
    }



}