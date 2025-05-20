package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycFourteenBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity


class KycFourteenFragment : Fragment() {
    lateinit var binding:FragmentKycFourteenBinding
    lateinit var viewModel: AofViewModel
    var zakat_status:String?=null
    var bank_name:String?=null
    var resident:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycFourteenBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        binding.apply {
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycThirteenFragment())
            }

            zakatStatus.setList1(AppConstants.ZakatType.map { it.first })

            zakatStatus.autoCompleteTextView1.setOnItemClickListener { adapterView, view, i, l ->
                zakat_status=AppConstants.ZakatType.get(i).second
                viewModel.otherDetail.otherDetailZakatStatus=zakat_status
            }

            bank.setList1(AppConstants.BANK_SWIFT_CODES.map { it.first }.toList())

            bank.autoCompleteTextView1.setOnItemClickListener { adapterView, view, i, l ->
                bank_name=AppConstants.BANK_SWIFT_CODES.get(i).second
                viewModel.otherDetail.otherDetailBank = bank_name
            }

            residentStatus.setOnButtonOneClickListener {
                resident = AppConstants.RESIDENTIALSTATUS.get(0).second
                viewModel.otherDetail.otherDetailResident=resident
            }
            residentStatus.setOnButtonTwoClickListener {
                resident = AppConstants.RESIDENTIALSTATUS.get(1).second
                viewModel.otherDetail.otherDetailResident=resident
            }

            btnContinue.setOnClickListener {
                if(!zakat_status.isNullOrEmpty() && !bank_name.isNullOrEmpty()
                    && !resident.isNullOrEmpty()){
                    viewModel.saveotherDetail()
                    (requireActivity() as AofActivity).loadFragment(KycFifteenFragment())
                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }

        }

        initFields()
        return binding.root
    }

    private fun initFields() {
        val otherDetail = viewModel.getotherDetail()
        otherDetail.apply {
            zakat_status = otherDetailZakatStatus
            bank_name = otherDetailBank
            resident = otherDetailResident
            binding.apply {
                if(zakat_status!=""){

                    zakatStatus.autoCompleteTextView1.setText(AppConstants.ZakatType.filter { it.second.equals(zakat_status) }.map { it.first }.first())
                }

                if(bank_name!=""){


                    bank.autoCompleteTextView1.setText(AppConstants.BANK_SWIFT_CODES.filter { it.second.equals(bank_name) }.map { it.first }.first())
                }

                if(resident!=""){
                    if(AppConstants.RESIDENTIALSTATUS.get(1).second.equals(resident)){
                        residentStatus.toggleSelection(false)
                    }else{
                        residentStatus.toggleSelection(true)
                    }
                }
            }
        }

    }


}