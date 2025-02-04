package com.example.scstrade.views.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.FragmentRegisterBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.RetrofitInstance
import com.example.scstrade.viewmodels.MainViewModel
import com.example.scstrade.views.widgets.VerticalDivider


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentRegisterBinding.inflate(inflater,container,false)

        bindView()
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
                   (binding.recyclerIndices.adapter as IndexAdapter).addItems(resource.data?: emptyList())
                }
            }
        })

        return binding.root
    }

    private fun bindView() {
        binding.recyclerIndices.apply {
            adapter= IndexAdapter(emptyList())
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(VerticalDivider())


        }

    }

}