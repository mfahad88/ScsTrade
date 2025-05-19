package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycTenBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity


class KycTenFragment : Fragment() {
    lateinit var binding:FragmentKycTenBinding
    lateinit var viewModel: AofViewModel
    var nominee_address:String? = null
    var nominee_nic_type:String? = null
    var nominee_nic_expiry:String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentKycTenBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        toggleNicValidity(false)
        binding.apply {
            nomineeAddress.addTextChangedListener {
                nominee_address = it.toString()
                viewModel.nominee.nomineeAddress = nominee_address
            }
            nomineeNic.setOnButtonOneClickListener {
                toggleNicValidity(false)
                viewModel.nominee.nomineeNicType= nominee_nic_type
            }

            nomineeNic.setOnButtonTwoClickListener(){
                toggleNicValidity(true)
                viewModel.nominee.nomineeNicType= nominee_nic_type
            }
            nomineeNic.editText.setOnFocusChangeListener { view, b ->
                if(b){
                    Utils.showDatePicker(requireContext()){ day, month, year ->
                        val selectedDate = "$day-$month-$year"
                        nomineeNic.editText.setText(selectedDate)
                        nominee_nic_expiry=selectedDate
                        viewModel.nominee.nomineeNicExpiry = nominee_nic_expiry
                    }
                }
            }
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycNineFragment())
            }

            btnContinue.setOnClickListener {
                if(!nominee_address.isNullOrEmpty()){
                    if(nominee_nic_type?.equals("Lifetime")?:false){
                        viewModel.savenominee()
                    }else{
                        if(!nominee_nic_expiry.isNullOrEmpty()){
                            viewModel.savenominee()
                        }else{
                            Utils.showError(requireView(),
                                getString(R.string.please_provide_nic_expiry_date))
                        }
                    }
                    (requireActivity() as AofActivity).loadFragment(KycElevenFragment())
                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }

        }


        return binding.root
    }

    private fun initFields() {
        val nominee = viewModel.getnominee()

        nominee.apply {
            if(nomineeAddress!=""){
                nominee_address = nomineeAddress
                binding.nomineeAddress.setText(nominee_address)
            }

            if(nomineeNicType!=""){
                if(nomineeNicType.equals("Lifetime")){
                    toggleNicValidity(true)
                }else{
                    toggleNicValidity(false)
                }
            }

            if(nomineeNicExpiry!=""){
                nominee_nic_expiry = nomineeNicExpiry
                binding.nomineeNic.editText.setText(nominee_nic_expiry)
            }
        }
    }

    private fun toggleNicValidity(isLifetime:Boolean){
        if(isLifetime){
            binding.nomineeNic.toggleSelection(false)
            binding.nomineeNic.editText.apply {
                setText("")
                isEnabled=false
            }
            nominee_nic_type="Lifetime"
        }else{
            binding.nomineeNic.toggleSelection(true)
            binding.nomineeNic.editText.apply {
             //   setText("")
                isEnabled=true
            }
            nominee_nic_type="Non-Lifetime"
        }
    }
}