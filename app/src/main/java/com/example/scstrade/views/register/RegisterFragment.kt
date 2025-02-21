package com.example.scstrade.views.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentRegisterBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.widgets.VerticalDivider


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentRegisterBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
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

        viewModel.mutableRegister.observe(viewLifecycleOwner, Observer { resource->
            when(resource){
                is Resource.Error -> Utils.showError(binding.root,"Unable to Register")
                is Resource.Loading -> binding.loader.visibility=View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility=View.GONE
                    if(resource.data?.isNotEmpty()?:false){
                        Utils.showSuccess(binding.root,"Successfully Register")
                        Utils.saveSharedPreference(requireContext(),AppConstants.USER,resource.data?: emptyList())
                        loadFragment(LandingFragment(),false)
                    }else{
                        Utils.showError(binding.root,"Unable to Register")
                    }
                }
            }
        })

        binding.button.setOnClickListener {
            if(binding.fullName.text.isNotEmpty() && binding.email.text.isNotEmpty() && binding.mobileNumber.text.isNotEmpty() && binding.password.text.isNotEmpty()){
                binding.apply {
                    viewModel.registerUser(
                        fullName = fullName.text,
                        email = email.text,
                        password = password.text,
                        mobile = mobileNumber.text
                    )
                }
            }else{
                Utils.showError(binding.root,"Provide data for all fields")
            }
        }

        return binding.root
    }

    private fun loadFragment(fragment: Fragment, isBackStack: Boolean=false) {
        if(isBackStack){

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            val manager= parentFragmentManager
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            manager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun bindView() {
        binding.recyclerIndices.apply {
            adapter= IndexAdapter(emptyList(), viewModel, viewLifecycleOwner)
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(VerticalDivider())


        }

    }

}