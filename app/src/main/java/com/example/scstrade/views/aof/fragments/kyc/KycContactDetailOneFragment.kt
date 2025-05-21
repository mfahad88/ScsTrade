package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycFourBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.google.gson.reflect.TypeToken


class KycContactDetailOneFragment : Fragment() {
    lateinit var binding:FragmentKycFourBinding
    lateinit var login:LoginDataItem
    lateinit var viewModel: AofViewModel
    var mobile_Number:String?=null
    var email_Address:String?=null
    var mailing_Address:String?=null
    var mailing_Country:String?=null
    var mailing_Province:String?=null
    var mailing_Province_Other:String?=null
    var mailing_City:String?=null
    var mailing_City_Other:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycFourBinding.inflate(inflater,container,false)
        viewModel=(requireActivity() as AofActivity).viewModel
        initDetails()
        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(KycBasicDataThreeFragment())
        }
        fetchUser()

        populateDropdown()
        binding.apply {
            mobileNumber.textInputEditText.setText(login.registrationPhone)
            mobileNumber.textInputEditText.isEnabled=false
            mobile_Number = login.registrationPhone
            email.textInputEditText.setText(login.registrationEmail)
            email.textInputEditText.isEnabled=false
            email_Address = login.registrationEmail
            mailingAddress.addTextChangedListener {
                mailing_Address=it.toString()
            }
            btnContinue.setOnClickListener {
                if(mobile_Number!!.isNotEmpty() && email_Address!!.isNotEmpty() && mailing_Address!!.isNotEmpty() &&
                    mailing_Country!!.isNotEmpty() && mailing_Province!!.isNotEmpty() && mailing_City!!.isNotEmpty()){
                    viewModel.contactDetail.apply {
                        mobileNumber = mobile_Number
                        emailAddress = email_Address
                        mailingAddress = mailing_Address
                        mailingCountry = mailing_Country
                        mailingProvince = mailing_Province
                        mailingProvinceOther = mailing_Province_Other
                        mailingCity = mailing_City
                        mailingCityOther = mailing_City_Other
                    }
                    viewModel.saveContactDetails()
                    (requireActivity() as AofActivity).loadFragment(KycContactDetailTwoFragment())

                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }
        }
        return binding.root
    }

    private fun initDetails() {
        val contactDetail = viewModel.getContactDetails()
        contactDetail.apply {
            mobile_Number = mobileNumber
            email_Address = emailAddress
            mailing_Address = mailingAddress
            mailing_Country = mailingCountry
            mailing_Province = mailingProvince
            mailing_Province_Other = mailingProvinceOther
            mailing_City = mailingCity
            mailing_City_Other = mailingCityOther

            binding.apply {
                if(mobile_Number!="") {
                    mobileNumber.textInputEditText.setText(mobile_Number)
                }
                if(email_Address!="") {
                    email.textInputEditText.setText(email_Address)
                }
                if(mailing_Address!="") {
                    mailingAddress.setText(mailing_Address)
                }
                if(mailing_Country!="") {
                    mailingCountry.dropdown.setText(AppConstants.COUNTRY.filter {
                        it.second.equals(
                            mailing_Country,
                            true
                        )
                    }.map { it.first }.first())
                }
                if(mailing_City!="") {
                    mailingCity.dropdown.setText(AppConstants.CITY.filter {
                        it.first.second.equals(
                            mailing_City,
                            true
                        )
                    }.map { it.first.first }.first())
                }

                if(mailing_Province!="") {
                    mailingProvince.dropdown.setText(AppConstants.PROVINCE.filter {
                        it.second.equals(
                            mailing_Province,
                            true
                        )
                    }.map { it.first }.first())
                }
            }
        }
    }

    private fun populateDropdown() {
        binding.mailingCountry.setEntries(AppConstants.COUNTRY.map { it.first }.toList())
        binding.mailingProvince.setEntries(AppConstants.PROVINCE.map { it.first }.toList())
        binding.mailingCity.setEntries(AppConstants.CITY.map { it.first.first }.toList())

        binding.mailingCountry.dropdown.setOnItemClickListener { adapterView, view, i, l ->
            mailing_Country=AppConstants.COUNTRY.get(i).second
        }

        binding.mailingProvince.dropdown.setOnItemClickListener { adapterView, view, i, l ->
            mailing_Province=AppConstants.PROVINCE.get(i).second
            mailing_Province_Other = AppConstants.PROVINCE.get(i).first
        }

        binding.mailingCity.dropdown.setOnItemClickListener { adapterView, view, i, l ->
            mailing_City=AppConstants.CITY.get(i).first.second
            mailing_City_Other = AppConstants.CITY.get(i).first.first
        }
    }

    private fun fetchUser() {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(requireContext(), emptyList<LoginDataItem>(),
            AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }

}