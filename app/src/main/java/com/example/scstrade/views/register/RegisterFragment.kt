package com.example.scstrade.views.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.FragmentRegisterBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.RetrofitInstance
import com.example.scstrade.views.main.MainViewModel
import com.example.scstrade.views.widgets.VerticalDivider


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit  var viewModel:MainViewModel
    private lateinit  var mainRepository: MainRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentRegisterBinding.inflate(inflater,container,false)
        mainRepository= MainRepository(RetrofitInstance.api)
        viewModel=MainViewModel(mainRepository)
        viewModel.fetchIndices()


        viewModel.mutableIndices.observe(viewLifecycleOwner, Observer { resource ->
            System.out.println(resource.data.toString())
            when (resource){
                is Resource.Loading ->{
                    binding.loader.visibility=View.VISIBLE
                    binding.container.visibility=View.GONE
                }

                is Resource.Error ->{

                }

                is Resource.Success -> {
                    binding.loader.visibility=View.GONE
                    binding.container.visibility=View.VISIBLE
                    bindView(resource.data)
                }
            }
        })

        return binding.root
    }

    private fun bindView(data: List<KSEIndices>?) {
        binding.recyclerIndices.apply {
            adapter=IndexAdapter(data?: emptyList())
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(VerticalDivider())


        }

    }

}