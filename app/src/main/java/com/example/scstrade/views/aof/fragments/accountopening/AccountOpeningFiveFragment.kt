package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningFiveBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.verifyOtp.VerifyOtpDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity


class AccountOpeningFiveFragment : Fragment() {
    lateinit var binding: FragmentAccountOpeningFiveBinding
    lateinit var aofViewModel: AofViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningFiveBinding.inflate(inflater,container,false)
        aofViewModel = (requireActivity() as AofActivity).viewModel
        val uin =aofViewModel.getaccountOpening().accountopeningnicNumber
        binding.apply {
            btnContinue.setOnClickListener {
                if(binding.pinview.value.isNotEmpty() && uin?.isNotEmpty()?:false){

                    aofViewModel.verifyOtp(VerifyOtpDto(pinview.value.toString(), "v",uin.toString()))

                }
            }

        }
        aofViewModel.mutableVerifyOtp.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message?:"An error occurred")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val response = result.data
                    if(response?.statusCode==200){
                        (requireActivity() as AofActivity).loadFragment(fragment = AccountOpeningSixFragment())
                    }else{
                        Utils.showError(requireView(),response?.message?:"An error occurred")
                    }
                }
            }
        })
        return binding.root
    }

}