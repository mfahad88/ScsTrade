package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.scstrade.databinding.FragmentKycEightBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.attorneyDetail.AttorneyDetailDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter



class KycAttorneyDetailTwoFragment : Fragment() {
    lateinit var binding: FragmentKycEightBinding
    lateinit var viewModel: AofViewModel
    var nic_type:String? = null
    var nic_valid:String? = null
    var mobile_number:String? = null
    var email_address:String? = null
    var country:String? = null
    var city:String? = null
    var province:String? = null
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
            attorneyCountry.setEntries(AppConstants.COUNTRY.map { it.first }.toList())
            attorneyCity.setEntries(AppConstants.CITY.map { it.first.first }.toList())
            attorneyProvince.setEntries(AppConstants.PROVINCE.map { it.first }.toList())

            attorneyCountry.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                country= AppConstants.COUNTRY.get(i).second
            }

            attorneyProvince.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                province = AppConstants.PROVINCE.get(i).second
            }

            attorneyCity.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                city = AppConstants.CITY.get(i).first.second
            }

            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycAttorneyDetailOneFragment())
            }
            isAttorney.setOnButtonOneClickListener {
                nic_type = "N"
                isAttorney.editText.setText("")
                isAttorney.editText.isEnabled=true
            }
            isAttorney.setOnButtonTwoClickListener {
                nic_type = "Y"
                isAttorney.editText.setText("")
                isAttorney.editText.isEnabled=false
            }

            isAttorney.editText.setOnFocusChangeListener { view, b ->
                if(b){
                    Utils.showDatePicker(requireContext()){ day, month, year ->
                        val customDate = LocalDate.of(year , month, day)
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val formatted = customDate.format(formatter)
                        isAttorney.editText.setText(formatted)
                        nic_valid=formatted
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
                    && mailing_address?.isNotEmpty()?:false && residence_number?.isNotEmpty()?:false
                    && country?.isNotEmpty()?:false && city?.isNotEmpty()?:false && province?.isNotEmpty()?:false){
                    viewModel.attorneyDetail.apply {
                        attorneyNicType = nic_type
                        attorneyNicExpiry = nic_valid
                        attorneyMobileNumber = mobile_number
                        attorneyEmailAdress = email_address
                        attorneyMailingAddress = mailing_address
                        attorneyResidenceNumber = residence_number
                        attorneyCountry = country
                        attorneyCity = city
                        attorneyProvince = province

                        viewModel.attorneyDetails(
                            AttorneyDetailDto(
                                id = null,
                                attorneyType = attorneyType,
                                identificationAtr = attorneyUinType,
                                salutationAtr = attorneySalutation,
                                clientNameAtr = attorneyFullName,
                                cnicAtr = attorneyUinNumber,
                                cnicExpiryDateAtr = attorneyNicExpiry,
                                cnicLifeTimeAtr = attorneyNicType,
                                mobileAtr = attorneyMobileNumber,
                                emailAtr = attorneyEmailAdress,
                                mailingAddressAtr1 = attorneyMailingAddress,
                                mailingAddressAtr2 = "     ",
                                mailingAddressAtr3 = "     ",
                                mailingCountryAtr = attorneyCountry,
                                mailingProvinceAtr = attorneyProvince,
                                mailingCityAtr = attorneyCity,
                                otherMailingCityAtr = AppConstants.CITY.filter { it.first.second.equals(attorneyCity) }.map { it.first.first }.first(),
                                otherMailingProvAtr = AppConstants.PROVINCE.filter { it.second.equals(attorneyProvince) }.map { it.first}.first(),
                                landlineAtr = attorneyResidenceNumber
                            )
                        )
                    }
                    viewModel.saveAttorneyDetails()


                }
            }

            viewModel.mutableAttorneyDetail.observe(viewLifecycleOwner, Observer { result->

                when(result){
                    is Resource.Error -> Utils.showError(requireView(),result.message?:"An error occurred...")
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        val response=result.data
                        if(response?.statusCode==200){
                            (requireActivity() as AofActivity).loadFragment(KycNomineeDetailOneFragment())

                        }else{
                            Utils.showError(requireView(),response?.message?:"An error occurred...")
                        }
                    }
                }
            })
        }
        return binding.root
    }

    private fun initFields() {
        val attorneyDetail = viewModel.getAttorneyDetails()
        attorneyDetail.apply {
            if(attorneyNicType!=""){
                if(attorneyNicType?.equals("Y")?:false){
                    binding.isAttorney.toggleSelection(false)
                    nic_type = "Y"
                }else{
                    binding.isAttorney.toggleSelection(true)
                    nic_type = "N"
                }
            }

            if(attorneyCity!=""){

                binding.attorneyCity.dropdown.setText(AppConstants.CITY.filter { it.first.second.equals(attorneyCity) }.map { it.first.first }.first())
                city = attorneyCity
            }


            if(attorneyCountry!=""){

                binding.attorneyCountry.dropdown.setText(AppConstants.COUNTRY.filter { it.second.equals(attorneyCountry) }.map { it.first }.first())
                country= attorneyCountry
            }

            if(attorneyProvince!=""){

                binding.attorneyProvince.dropdown.setText(AppConstants.PROVINCE.filter { it.second.equals(attorneyProvince) }.map { it.first }.first())
                province=attorneyProvince
            }

            if(attorneyNicExpiry!=""){
                binding.isAttorney.editText.setText(attorneyNicExpiry)
                nic_valid = attorneyNicExpiry
            }

            if(attorneyMobileNumber!=""){
                binding.mobileNumber.textInputEditText.setText(attorneyMobileNumber)
                mobile_number = attorneyMobileNumber
            }

            if(attorneyEmailAdress!=""){
                binding.emailAddr.textInputEditText.setText(attorneyEmailAdress)
                email_address = attorneyEmailAdress
            }

            if(attorneyMailingAddress!=""){
                binding.mailingAddress.setText(attorneyMailingAddress)
                mailing_address = attorneyMailingAddress
            }

            if(attorneyResidenceNumber!=""){
                binding.residenceNumber.textInputEditText.setText(attorneyResidenceNumber)
                residence_number = attorneyResidenceNumber
            }
        }


    }


}