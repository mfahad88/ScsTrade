package com.example.scstrade.views.aof.fragments.kyc.otherDetail

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycThirteenBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity


class KycOtherDetailTwoFragment : Fragment() {
    lateinit var binding:FragmentKycThirteenBinding
    lateinit var viewModel: AofViewModel
    var job_description:String?=null
    var department:String?=null
    var employer_name:String?=null
    var employer_address:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycThirteenBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.other_details)
        initFields()
        binding.apply {
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycOtherDetailOneFragment())
            }

            jobDetails.apply {
                textview_1.filters = arrayOf(InputFilter.LengthFilter(20))
                textview_2.filters = arrayOf(InputFilter.LengthFilter(20))
                textview_1.addTextChangedListener {
                    job_description=it.toString()
                    viewModel.otherDetail.otherDetailJobDesignation=job_description
                }

                textview_2.addTextChangedListener {
                    department=it.toString()
                    viewModel.otherDetail.otherDetailDepartment=department
                }
            }



            employerDetails.apply {
                textview_1.filters = arrayOf(InputFilter.LengthFilter(20))
                textview_2.filters = arrayOf(InputFilter.LengthFilter(20))
                textview_1.addTextChangedListener {
                    employer_name=it.toString()
                    viewModel.otherDetail.otherDetailEmployerName=employer_name
                }

                textview_2.addTextChangedListener {
                    employer_address = it.toString()
                    viewModel.otherDetail.otherDetailEmployerAddress=employer_address
                }
            }

            btnContinue.setOnClickListener {
                if(!job_description.isNullOrEmpty() && !department.isNullOrEmpty()
                    && !employer_name.isNullOrEmpty() && !employer_address.isNullOrEmpty()){
                    viewModel.saveotherDetail()
                    (requireActivity() as AofActivity).loadFragment(KycOtherDetailThreeFragment())
                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }
        }

        return binding.root
    }

    private fun initFields() {
        val otherDetail = viewModel.getotherDetail()

        otherDetail.apply {
            if(otherDetailJobDesignation!=""){
                job_description = otherDetailJobDesignation
                binding.jobDetails.textview_1.setText(job_description)
            }

            if(otherDetailDepartment!=""){
                department = otherDetailDepartment
                binding.jobDetails.textview_2.setText(department)
            }

            if(otherDetailEmployerName!=""){
                employer_name = otherDetailEmployerName
                binding.employerDetails.textview_1.setText(employer_name)
            }

            if(otherDetailEmployerAddress!=""){
                employer_address = otherDetailEmployerAddress
                binding.employerDetails.textview_2.setText(employer_address)
            }
        }
    }


}