package com.example.scstrade.views.indices

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentIndicesBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.google.gson.reflect.TypeToken

class IndicesFragment : Fragment() {

    lateinit var binding: FragmentIndicesBinding
    private val indicesViewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIndicesBinding.inflate(inflater,container,false)

        val simpleCallback=object:ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,0){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                (recyclerView.adapter as IndicesAdapter).swapItems(recyclerView.context,fromPosition, toPosition)
                return  true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.apply {
            visibility= View.VISIBLE
            val typeToken = object:TypeToken<List<KSEIndices>>(){}
            adapter=IndicesAdapter(Utils.getSharedPreference(requireContext(), emptyList<KSEIndices>(),"kseIndices",typeToken)){ kseIndices ->
                var bundle=Bundle()
                bundle.putString("index",kseIndices.iNDEXCODE)
                findNavController().navigate(R.id.stockFragment,bundle)
            }
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }

        indicesViewModel.mutableIndices.observe(viewLifecycleOwner, Observer { result ->
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
                            findNavController().navigate(R.id.stockFragment,bundle)
                        }
                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    }
                }
            }

        })
        return binding.root
    }


}