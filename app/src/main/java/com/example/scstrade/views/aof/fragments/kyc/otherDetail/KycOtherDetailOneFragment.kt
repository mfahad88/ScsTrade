package com.example.scstrade.views.aof.fragments.kyc.otherDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycTwelveBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.nomineeDetail.KycNomineeDetailThreeFragment


class KycOtherDetailOneFragment : Fragment() {
    lateinit var  binding: FragmentKycTwelveBinding
    lateinit var viewModel: AofViewModel
    var account_type:String?=null
    var income_slab:String?=null
    var source_income:String?=null
    var occup:String?=null
    var other_ocupation:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycTwelveBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.other_details)

        binding.apply {
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycNomineeDetailThreeFragment())
            }
            accountType.setOnButtonOneClickListener {
                accountType.toggleSelection(true)
                account_type=AppConstants.AccountType.filter { it.first.contains("normal",true) }.map { it.second }.first()
                grossAnnualIncomeslab.setEntries(AppConstants.AnnualIncomeNormal.map { it.first })
                grossAnnualIncomeslab.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                    income_slab = AppConstants.AnnualIncomeNormal.get(i).second
                    viewModel.otherDetail.otherDetailGrossIncomeSlab = income_slab
                    viewModel.otherDetail.otherDetailAccountType = account_type
                }
            }

            accountType.setOnButtonTwoClickListener {
                accountType.toggleSelection(false)
                account_type=AppConstants.AccountType.filter { it.first.contains("Sahulat",true) }.map { it.second }.first()
                grossAnnualIncomeslab.setEntries(AppConstants.AnnualIncomeSahulat.map { it.first })
                grossAnnualIncomeslab.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                    income_slab = AppConstants.AnnualIncomeSahulat.get(i).second
                    viewModel.otherDetail.otherDetailGrossIncomeSlab = income_slab
                    viewModel.otherDetail.otherDetailAccountType = account_type
                }
            }
            sourceOfIncome.textInputEditText.addTextChangedListener {
                source_income = it.toString()
                viewModel.otherDetail.otherDetailSourceOfIncome = source_income
            }
            occupation.textview_2.addTextChangedListener {
                other_ocupation = it.toString()
                viewModel.otherDetail.otherDetailOtherOccupation = other_ocupation
            }
            occupation.autoCompleteTextView1.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.Occupation.map { it.first }))
            occupation.autoCompleteTextView1.setOnItemClickListener { adapterView, view, i, l ->

                if((adapterView.getItemAtPosition(i) as String).equals("others",true)){
                    other_ocupation=occupation.textview_2.text.toString()
                    viewModel.otherDetail.otherDetailOtherOccupation = other_ocupation
                    binding.occupation.textview_2.isEnabled=true
                }else{
                    other_ocupation=null
                    occupation.textview_2.setText("")
                    binding.occupation.textview_2.isEnabled=false
                }

                occup = AppConstants.Occupation.get(i).second
                viewModel.otherDetail.otherDetailOccupation = occup
            }

            btnContinue.setOnClickListener {
                if(!account_type.isNullOrEmpty() && !income_slab.isNullOrEmpty()
                    && !source_income.isNullOrEmpty() && !occup.isNullOrEmpty()){
                    viewModel.saveotherDetail()
                    (requireActivity() as AofActivity).loadFragment(KycOtherDetailTwoFragment())
                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }
        }
        initFields()
        return binding.root
    }

    private fun initFields() {

        viewModel.mutableOtherDetailResponse.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val response = result.data?.data
                    viewModel.otherDetail.apply {
                        if(response!=null){
                            otherDetailAccountType= response.accountType
                            otherDetailGrossIncomeSlab=	response.annualIncomeNormal
                            otherDetailDepartment=	response.department
                            otherDetailEmployerAddress=	response.employeeAddress
                            otherDetailEmployerName=	response.employeeName
                            otherDetailJobDesignation=	response.jobTitle
                            otherDetailOccupation=	response.occupation
                            otherDetailOtherOccupation=	response.otherOccupation
                            otherDetailSourceOfIncome=	response.sourceOfIncome
                            otherDetailZakatStatus =	response.zakatStatus
                            otherDetailRemittance =	response.remittanceBasis
                            viewModel.saveotherDetail()
                        }

                        val otherDetail = viewModel.getotherDetail()
                        otherDetail.apply {
                            if(otherDetailAccountType!=""){
                                account_type = otherDetailAccountType
                                if(account_type.equals("SKA")){
                                    binding.accountType.toggleSelection(false)
                                }else{
                                    binding.accountType.toggleSelection(true)
                                }
                            }

                            if(otherDetailGrossIncomeSlab!=""){
                                income_slab = otherDetailGrossIncomeSlab
                                if(AppConstants.AnnualIncomeSahulat.filter { it.second.equals(income_slab) }.isNotEmpty()){
                                    binding.grossAnnualIncomeslab.dropdown.setText(AppConstants.AnnualIncomeSahulat.filter { it.second.equals(income_slab)}.map { it.first }.first())
                                }else{
                                    binding.grossAnnualIncomeslab.dropdown.setText(AppConstants.AnnualIncomeNormal.filter { it.second.equals(income_slab)}.map { it.first }.first())
                                }

                            }

                            if(otherDetailSourceOfIncome!="") {
                                source_income = otherDetailSourceOfIncome
                                binding.sourceOfIncome.textInputEditText.setText(source_income)
                            }

                            if(otherDetailOccupation!=""){
                                occup = otherDetailOccupation
                                binding.occupation.autoCompleteTextView1.setText(AppConstants.Occupation.filter { it.second.equals(occup) }.map { it.first }.first())
                            }

                            if(otherDetailOtherOccupation!=""){
                                other_ocupation = otherDetailOtherOccupation
                                binding.occupation.textview_2.setText(other_ocupation)
                                binding.occupation.textview_2.isEnabled=true
                            }
                        }
                    }
                }
            }
        })


    }


}