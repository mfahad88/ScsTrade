package com.example.scstrade.views.aof.fragments.accountopening
import androidx.compose.ui.res.dimensionResource

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
import com.example.scstrade.views.aof.fragments.kyc.basicData.KycBasicDataOneFragment


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
                if(!binding.pinview.value.isNullOrEmpty() && binding.pinview.value.length==6){

                    aofViewModel.verifyOtp(VerifyOtpDto(pinview.value.toString()/*, "v",uin.toString()*/))

                }else{
                    Utils.showError(requireView(),"Please input correct OTP...")
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
                    if(response?.isSuccess == true){
                        (requireActivity() as AofActivity).loadFragment(fragment = KycBasicDataOneFragment())
                    }else{
                        Utils.showError(requireView(),response?.message?:"An error occurred")
                    }
                }
            }
        })
        return binding.root
    }

}