package com.example.scstrade.views.indices

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentIndicesBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.repository.IndicesRepository
import com.example.scstrade.services.RetrofitInstance
import com.example.scstrade.viewmodels.IndicesViewModel
import com.example.scstrade.views.widgets.HorizontalDivider

class IndicesFragment : Fragment() {

    lateinit var binding: FragmentIndicesBinding
    lateinit var indicesRepository: IndicesRepository
    lateinit var indicesViewModel: IndicesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        indicesRepository= IndicesRepository(RetrofitInstance.api)
        indicesViewModel= IndicesViewModel(indicesRepository)

    }

    override fun onResume() {
        super.onResume()
        indicesViewModel.fetchIndices()
    }

    override fun onPause() {

        super.onPause()
        indicesViewModel.stopIndices()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIndicesBinding.inflate(inflater,container,false)
        val appConfiguration= AppBarConfiguration(/*findNavController().graph*/setOf(
            R.id.indicesFragment
        ))

        binding.toolbar.setupWithNavController(findNavController(),appConfiguration)
        indicesViewModel.mutableLiveData.observe(viewLifecycleOwner, Observer { result ->
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