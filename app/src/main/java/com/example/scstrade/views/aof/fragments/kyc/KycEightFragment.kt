package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycEightBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class KycEightFragment : Fragment() {
    lateinit var binding: FragmentKycEightBinding
    lateinit var viewModel: AofViewModel
    var nic_type:String? = null
    var nic_valid:String? = null
    var mobile_number:String? = null
    var email_address:String? = null
    var mailing_address:String? = null
    var residence_number:String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentKycEightBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        binding.apply {
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycSevenFragment())
            }
            isAttorney.setOnButtonOneClickListener {
                nic_type = "No-lifetime"
                isAttorney.editText.setText("")
                isAttorney.editText.isEnabled=true
            }
            isAttorney.setOnButtonTwoClickListener {
                nic_type = "Lifetime"
                isAttorney.editText.setText("")
                isAttorney.editText.isEnabled=false
            }

            isAttorney.setOnFocusListener {
                if(it){
                    Utils.showDatePicker(requireContext()){ day, month, year ->
                        val selectedDate = "$day-$month-$year"
                        isAttorney.editText.setText(selectedDate)
                        nic_valid=selectedDate
                    }
                }

            }

            mobileNumber.textInputEditText.addTextChangedListener {
                mobile_number=mobileNumber.textInputEditText.text.toString()
            }

            emailAddr.textInputEditText.addTextChangedListener {
                email_address=emailAddr.textInputEditText.text.toString()
            }

            mailingAddress.addTextChangedListener {
                mailing_address=mailingAddress.text.toString()
            }

            residenceNumber.textInputEditText.addTextChangedListener {
                residence_number = residenceNumber.textInputEditText.text.toString()
            }
            btnContinue.setOnClickListener {
                if(mobile_number?.isNotEmpty()?:false && email_address?.isNotEmpty()?:false
                    && mailing_address?.isNotEmpty()?:false && residence_number?.isNotEmpty()?:false){
                    viewModel.attorneyDetail.apply {
                        attorneyNicType = nic_type
                        attorneyNicExpiry = mobile_number
                        attorneyEmailAdress = email_address
                        attorneyMailingAddress = mailing_address
                        attorneyResidenceAddress = residence_number
                    }
                    viewModel.saveAttorneyDetails()
                }
            }
        }
        return binding.root
    }

    private fun initFields() {
        val attorneyDetail = viewModel.getAttorneyDetails()
        if(attorneyDetail.attorneyType!=""){

        }
    }


}