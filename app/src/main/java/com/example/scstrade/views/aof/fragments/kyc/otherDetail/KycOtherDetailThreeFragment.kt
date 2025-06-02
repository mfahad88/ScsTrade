package com.example.scstrade.views.aof.fragments.kyc.otherDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycFourteenBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.otherDetail.OtherDetailDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.KycDocumentFragment


class KycOtherDetailThreeFragment : Fragment() {
    lateinit var binding:FragmentKycFourteenBinding
    lateinit var viewModel: AofViewModel
    var zakat_status:String?=null
    var bank_name:String?=null
    var remittance:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycFourteenBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.progressBar.progress=6
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.other_details)
        initFields()
        binding.apply {
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycOtherDetailTwoFragment())
            }

            zakatStatus.setList1(AppConstants.ZakatType.map { it.first })

            zakatStatus.autoCompleteTextView1.setOnItemClickListener { adapterView, view, i, l ->
                zakat_status=AppConstants.ZakatType.get(i).second
                viewModel.otherDetail.otherDetailZakatStatus=zakat_status
            }

//            bank.setList1(AppConstants.BANK_SWIFT_CODES.map { it.first }.toList())

           /* bank.autoCompleteTextView1.setOnItemClickListener { adapterView, view, i, l ->
                bank_name=AppConstants.BANK_SWIFT_CODES.get(i).second
                viewModel.otherDetail.otherDetailBank = bank_name
            }*/
            remittanceBasis.setList1(AppConstants.RemittanceDescription.map { it.first }.toList())

            remittanceBasis.autoCompleteTextView1.setOnItemClickListener { adapterView, view, i, l ->
                remittance = AppConstants.RemittanceDescription.get(i).second
                viewModel.otherDetail.otherDetailRemittance = remittance
            }

            btnContinue.setOnClickListener {
                if(!zakat_status.isNullOrEmpty()
                    && !remittance.isNullOrEmpty()){
                    viewModel.otherDetails(
                        OtherDetailDto(
                            accountType = viewModel.otherDetail.otherDetailAccountType?:"",
                            annualIncomeNormal = viewModel.otherDetail.otherDetailGrossIncomeSlab?:"",
                            department = viewModel.otherDetail.otherDetailDepartment?:"",
                            employeeAddress = viewModel.otherDetail.otherDetailEmployerAddress?:"",
                            employeeName = viewModel.otherDetail.otherDetailEmployerName?:"",
                            id=null,
                            jobTitle = viewModel.otherDetail.otherDetailJobDesignation?:"",
                            occupation = viewModel.otherDetail.otherDetailOccupation?:"",
                            otherOccupation = viewModel.otherDetail.otherDetailOtherOccupation?:"",
                            sourceOfIncome = viewModel.otherDetail.otherDetailSourceOfIncome?:"",
                            zakatStatus = zakat_status?:"",
                            remittanceBasis = remittance?:""

                        )
                    )
//                    viewModel.saveotherDetail()

                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }

        }
        viewModel.mutableOtherDetail.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(requireView(),result.message?:"An error occurred")
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    val response = result.data
                    if(response?.isSuccess?:false && response?.statusCode==200){
                        (requireActivity() as AofActivity).loadFragment(KycDocumentFragment())
                    }else{
                        Utils.showError(requireView(),response?.message?:"An error occurred")
                    }
                    binding.loader.visibility = View.GONE
                }
            }
        })

        return binding.root
    }

    private fun initFields() {
        val otherDetail = viewModel.otherDetail
        otherDetail.apply {
            zakat_status = otherDetailZakatStatus
            bank_name = otherDetailBank
            remittance = otherDetailRemittance
            binding.apply {
                if(zakat_status!="" && zakat_status!=null){

                    zakatStatus.autoCompleteTextView1.setText(AppConstants.ZakatType.filter { it.second.equals(zakat_status) }.map { it.first }.first())
                }

                if(bank_name!="" && bank_name!=null){


                    bank.autoCompleteTextView1.setText(AppConstants.BANK_SWIFT_CODES.filter { it.second.equals(bank_name) }.map { it.first }.first())
                }

                if(remittance!="" && remittance!=null){
                    remittanceBasis.autoCompleteTextView1.setText(AppConstants.RemittanceDescription.filter { it.second.equals(remittance) }.map { it.first }.first())
                }


            }
        }

    }


}