package com.example.scstrade.views.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.FragmentLoginBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.register.IndexAdapter
import com.example.scstrade.views.register.RegisterFragment
import com.example.scstrade.views.widgets.VerticalDivider


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private  lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLoginBinding.inflate(inflater,container,false)
        viewModel=(requireActivity().application as MyApp).viewModel


        binding.google.setOnClickListener {

       }
        binding.button.setOnClickListener {
            if(binding.userName.text.isNotEmpty() && binding.password.text.isNotEmpty()){
                viewModel.fetchLogin(binding.userName.text,binding.password.text)
            }else{
                Utils.showError(binding.root,"Please provide valid username and password")
            }
        }
        viewModel.fetchIndices()
        binding.recyclerIndices.apply {
            adapter= IndexAdapter(emptyList(),viewModel,viewLifecycleOwner)
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(VerticalDivider())


        }

        viewModel.mutableLogin.observe(viewLifecycleOwner, Observer { resource->
            when(resource){
                is Resource.Error -> {
                    binding.loader.visibility=View.GONE
                    Utils.showError(requireView(),"Invalid login")
                }
                is Resource.Loading -> binding.loader.visibility=View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility=View.GONE
                    if(!resource.data.isNullOrEmpty()){
                        if(binding.rememberMe.isChecked) {
                           Utils.saveSharedPreference(requireContext(),AppConstants.IS_REMEMBER,
                               listOf(true)
                           )
                        }


                        Utils.saveSharedPreference(
                            requireContext(),
                            AppConstants.USER,
                            resource.data
                        )
                        Utils.showSuccess(requireView(),"Success")
                        viewModel.mutableLogin.removeObservers(this)
                        viewModel.mutableLogin.value=null
                        binding.userName.text=null
                        binding.password.text= null
                        (requireActivity() as MainActivity).loadFragment(LandingFragment(),false)
                    }
                }
            }
        })

        viewModel.mutableIndices.observe(viewLifecycleOwner, Observer { resource ->
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
                    binding.recyclerIndices.visibility=View.VISIBLE
                    (binding.recyclerIndices.adapter as IndexAdapter).addItems(
                        resource.data ?: emptyList()
                    )
                    /*if(resource.data?.first()?.marketStatus?.lowercase()=="close"){
                        binding.recyclerIndices.visibility=View.GONE
                    }else {
                        binding.recyclerIndices.visibility=View.VISIBLE
                        (binding.recyclerIndices.adapter as IndexAdapter).addItems(
                            resource.data ?: emptyList()
                        )
                    }*/
                }
            }
        })
        binding.signUp.setOnClickListener {

            (requireActivity() as MainActivity).loadFragment(RegisterFragment(),true)
        }
        return binding.root
    }



}