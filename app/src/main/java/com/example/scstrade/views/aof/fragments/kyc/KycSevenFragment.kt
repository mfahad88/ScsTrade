package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycSevenBinding
import com.example.scstrade.databinding.FragmentKycSixBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity


class KycSevenFragment : Fragment() {
    lateinit var viewModel: AofViewModel
    lateinit var binding: FragmentKycSevenBinding
    var attorney_type:String?=null
    var attorney_saluation:String?=null
    var attorney_FullName:String?=null
    var attorney_Uin_Type:String?=null
    var attorney_Uin_Number:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycSevenBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        populateDropdown()
        binding.apply {
            back.setOnClickListener {
                if(viewModel.getContactDetails().parmanentAddress?.isNotEmpty()?:false){
                    (requireActivity() as AofActivity).loadFragment(KycSixFragment())
                }else{
                    (requireActivity() as AofActivity).loadFragment(KycFiveFragment())
                }
            }

            isTheAtto.apply {
                setOnButtonOneClickListener {
                    viewModel.attorneyDetail.attorneyType="self"
                    attorney_type = "self"
                    binding.someElseContainer.visibility= View.GONE
                }

                setOnButtonTwoClickListener {
                    viewModel.attorneyDetail.attorneyType="someone else"
                    attorney_type = "someone else"
                    binding.someElseContainer.visibility= View.VISIBLE
                }
            }

            fullName.textInputEditText.addTextChangedListener {
                attorney_FullName = it.toString()
            }

            uinNumber.textInputEditText.addTextChangedListener {
                attorney_Uin_Number = it.toString()
            }

            btnContinue.setOnClickListener {
                viewModel.attorneyDetail.attorneyType=attorney_type
                if(someElseContainer.visibility==View.VISIBLE){
                    viewModel.attorneyDetail.apply {
                        attorneySalutation = attorney_saluation
                        attorneyFullName=attorney_FullName
                        attorneyUinType = attorney_Uin_Type
                        attorneyUinNumber = attorney_Uin_Number
                    }
                }
                viewModel.saveAttorneyDetails()
                (requireActivity() as AofActivity).loadFragment(KycEightFragment())
            }
        }
        return binding.root
    }

    private fun initFields() {
        val attorneyDetail=viewModel.getAttorneyDetails()
        attorney_type=attorneyDetail.attorneyType
        attorney_saluation = attorneyDetail.attorneySalutation.toString()
        attorney_FullName = attorneyDetail.attorneyFullName
        attorney_Uin_Type = attorneyDetail.attorneyUinType
        attorney_Uin_Number = attorneyDetail.attorneyUinNumber

        binding.apply {
            if(attorney_saluation!="") {
                labelledSpinner.dropdown.setText(AppConstants.SALUTATION.filter {
                    it.second.equals(
                        attorney_saluation
                    )
                }.map { it.first }.first())
            }
            if(attorney_Uin_Type!="") {
                uinType.dropdown.setText(AppConstants.IDTYPE.filter {
                    it.second.equals(
                        attorney_Uin_Type
                    )
                }.map { it.first }.first())
            }
            if(attorney_FullName!="") {
                fullName.textInputEditText.setText(attorney_FullName)
            }
            if(attorney_Uin_Number!="") {
                uinNumber.textInputEditText.setText(attorney_Uin_Number)
            }
            if(attorney_type!="") {
                if (attorney_type.equals("someone else")) {
                    isTheAtto.toggleSelection(false)
                    someElseContainer.visibility = View.VISIBLE
                } else {
                    isTheAtto.toggleSelection(true)
                    someElseContainer.visibility = View.GONE
                }
            }
        }

    }

    private fun populateDropdown() {
        binding.apply {
            labelledSpinner.setEntries(AppConstants.SALUTATION.map { it.first })
            uinType.setEntries(AppConstants.IDTYPE.map { it.first })

            labelledSpinner.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                attorney_saluation=AppConstants.SALUTATION.get(i).second
            }

            uinType.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                attorney_Uin_Type=AppConstants.IDTYPE.get(i).second
            }
        }
    }


}