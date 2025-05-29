package com.example.scstrade.views.aof.fragments.kyc.contactDetail

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycFiveBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.request.aof.contactDetails.ContactDetailDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.attorneyDetail.KycAttorneyDetailOneFragment


class KycContactDetailTwoFragment : Fragment() {
    lateinit var binding: FragmentKycFiveBinding
    lateinit var viewModel: AofViewModel
    var office_number:String?=null
    var residence_number:String?=null
    var isPermanentAddressSame:Boolean=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycFiveBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.contact_detail)
        initFields()
        binding.apply {
            Utils.filterTextField(officeResidenceNumber.textview_1, Regex("[^0-9]"))
            Utils.filterTextField(officeResidenceNumber.textview_2, Regex("[^0-9]"))
            officeResidenceNumber.apply {
                textview_1.filters= arrayOf(InputFilter.LengthFilter(13))
                textview_2.filters= arrayOf(InputFilter.LengthFilter(13))
                textview_1.inputType = InputType.TYPE_CLASS_PHONE
                textview_2.inputType = InputType.TYPE_CLASS_PHONE
            }
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycContactDetailOneFragment())
            }
            btnContinue.setOnClickListener {
                if(office_number!!.isNotEmpty() && residence_number!!.isNotEmpty()){
                    viewModel.contactDetail.apply {
                        officeNumber=office_number
                        residenceNumber=residence_number
                    }
                    viewModel.saveContactDetails()
                    if(isPermanentAddressSame){
                        viewModel.getContactDetails().apply {
                            viewModel.createContactDetail(
                                ContactDetailDto(
                                    id = null,
                                    mailingAddress1 = mailingAddress,
                                    mailingCountryId = mailingCountry,
                                    mailingProvinceId = mailingProvince,
                                    mailingProvinceOther = mailingProvinceOther,
                                    mailingCityId = mailingCity,
                                    mailingphoneNo = officeNumber,
                                    mailingResidence = residenceNumber,
                                    permanentAddress1 = mailingAddress,
                                    permanentCountryId = mailingCountry,
                                    permanentCityId = mailingCity,
                                    permanentCityOther = mailingCityOther,
                                    permanentProvinceId = mailingProvince,
                                    permanentProvinceOther = mailingProvinceOther,
                                    permanentphoneNo = officeNumber,
                                    permanentResidence = residenceNumber,
                                    mailingCityOther = mailingCityOther,
                                    permanentAddress2 = "     ",
                                    permanentAddress3 = "     ",
                                    mailingAddress2 = "     ",
                                    mailingAddress3 = "     ",
                                    mailingProof = mailingAddress,
                                    permanentProof  = mailingAddress,
                                )
                            )
                        }

                        (requireActivity() as AofActivity).loadFragment(KycAttorneyDetailOneFragment())
                    }else{
                        (requireActivity() as AofActivity).loadFragment(
                            KycContactDetailThreeFragment()
                        )
                    }
                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }

            }

            officeResidenceNumber.textview_1.addTextChangedListener {
                office_number = it.toString()
            }
            officeResidenceNumber.textview_2.addTextChangedListener {
                residence_number = it.toString()
            }

            parmanentAddr.apply {
                setOnButtonOneClickListener {
                    isPermanentAddressSame= false
                }
                setOnButtonTwoClickListener {
                    isPermanentAddressSame=true
                }
            }
        }
        return binding.root
    }

    private fun initFields() {
        val contactDetail = viewModel.getContactDetails()
        contactDetail.apply {
            if(officeNumber?.isNotEmpty()?:false){
                binding.officeResidenceNumber.textview_1.setText(officeNumber)
                office_number=officeNumber
            }

            if(residenceNumber?.isNotEmpty()?:false){
                binding.officeResidenceNumber.textview_2.setText(residenceNumber)
                residence_number=residenceNumber
            }

            if(parmanentAddress?.isNotEmpty()?:false){
                binding.parmanentAddr.toggleSelection(false)
                isPermanentAddressSame=true
            }else{
                binding.parmanentAddr.toggleSelection(true)
                isPermanentAddressSame=false
            }
        }
    }


}