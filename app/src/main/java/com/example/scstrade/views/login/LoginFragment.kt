package com.example.scstrade.views.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentLoginBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.ChartRepository
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.RetrofitInstance
import com.example.scstrade.viewmodels.ChartViewModel
import com.example.scstrade.viewmodels.MainViewModel
import com.example.scstrade.views.register.IndexAdapter
import com.example.scstrade.views.widgets.VerticalDivider


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private  val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLoginBinding.inflate(inflater,container,false)

        binding.button.setOnClickListener {
            val navController=Navigation.findNavController(requireActivity(),R.id.nav_host_fragment)

            navController.navigate(R.id.action_loginFragment_to_landingActivity)
            requireActivity().finish()
        }

        binding.recyclerIndices.apply {
            adapter= IndexAdapter(emptyList())
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(VerticalDivider())


        }

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
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return binding.root
    }



}