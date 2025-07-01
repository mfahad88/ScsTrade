package com.example.scstrade.views.aof.fragments.kyc.contactDetail

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycFourBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.aof.contactDetails.ContactDetailResponse
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.basicData.KycBasicDataThreeFragment
import com.google.gson.reflect.TypeToken
import kotlin.math.log


class KycContactDetailOneFragment : Fragment() {
    lateinit var binding:FragmentKycFourBinding
    lateinit var login:LoginDataItem
    lateinit var viewModel: AofViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycFourBinding.inflate(inflater,container,false)
        viewModel=(requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.contact_detail)
        fetchUser()
        viewModel.getcontactDetails()
        initField()



        return binding.root
    }

    private fun initField() {
        binding.apply {
            mobileNumber.textInputEditText.setText(login.registrationPhone)
            email.textInputEditText.setText(login.registrationEmail)
            mailingCountry.setListEntries(AppConstants.COUNTRY.map { (label, code) -> android.util.Pair(label, code)  })
            mailingProvince.setListEntries(AppConstants.PROVINCE.map { (label, code) -> android.util.Pair(label, code)  })
            mailingCity.setListEntries(AppConstants.CITY.map { android.util.Pair(it.first.first,it.first.second) })

            permanentCountry.setListEntries(AppConstants.COUNTRY.map { (label, code) -> android.util.Pair(label, code)  })
            permanentProvince.setListEntries(AppConstants.PROVINCE.map { (label, code) -> android.util.Pair(label, code)  })
            permanentCity.setListEntries(AppConstants.CITY.map { android.util.Pair(it.first.first,it.first.second) })
            viewModel.mutableContactDetailResponse.observe(viewLifecycleOwner, Observer { result->
                when(result){
                    is Resource.Error -> Utils.showError(requireView(),result.message)
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val data=result.data?.data
                        if(data!=null) {
                            populateRecord(data)
                        }
                    }
                }
            })
        }
    }

    private fun populateRecord(data: ContactDetailResponse) {
        binding.apply {
            if(!TextUtils.isEmpty(data.mailingAddress1)) {
                mailingAddress.setText(data.mailingAddress1)
            }
            if(!TextUtils.isEmpty(data.mailingCountryId)) {
                mailingCountry.dropdown.setText(AppConstants.COUNTRY.filter { it.first.equals(data.mailingCountryId) }
                    .map { it.second }.first(), true)
            }
            if(!TextUtils.isEmpty(data.mailingProvinceOther)){
                mailingOtherProvince.textInputEditText.setText(data.mailingProvinceOther)
                mailingProvince.visibility=View.INVISIBLE
                mailingOtherProvince.visibility = View.VISIBLE
            }

            if(!TextUtils.isEmpty(data.mailingCityOther)){
                mailingOtherCity.textInputEditText.setText(data.mailingCityOther)
                mailingCity.visibility = View.INVISIBLE
                mailingOtherCity.visibility = View.VISIBLE
            }

            if(!TextUtils.isEmpty(data.mailingphoneNo)){
                officeResidenceNumber.textview_1.setText(data.mailingphoneNo)
                officeResidenceNumber.textview_2.setText(data.mailingResidence)
            }
            parmanentAddrOption.setOnButtonOneClickListener {
                groupMailing.visibility = View.VISIBLE
            }
            parmanentAddrOption.setOnButtonTwoClickListener {
                groupMailing.visibility = View.GONE
            }
            if(!TextUtils.isEmpty(data.permanentAddress1)){
            parmanentAddr.setText(data.permanentAddress1)
            }
            if(!TextUtils.isEmpty(data.permanentCountryId)) {
                permanentCountry.dropdown.setText(AppConstants.COUNTRY.filter { it.first.equals(data.permanentCountryId) }
                    .map { it.second }.first(), true)
            }
            if(!TextUtils.isEmpty(data.permanentProvinceId)) {
                permanentProvince.dropdown.setText(AppConstants.PROVINCE.filter {
                    it.second.equals(
                        data.permanentProvinceId
                    )
                }.map { it.first }.first(), true)
                permanentProvince.visibility = View.VISIBLE
                permanentOtherProvince.visibility = View.INVISIBLE
            }

            if(!TextUtils.isEmpty(data.permanentCityId)) {
                permanentCity.dropdown.setText(AppConstants.CITY.filter { it.first.first.equals(data.permanentCityId) }
                    .map { it.first.second }.first(), true)
                permanentCity.visibility = View.VISIBLE
                permanentOtherCity.visibility = View.INVISIBLE
            }

            if(!TextUtils.isEmpty(data.permanentProvinceOther)) {
                permanentOtherProvince.textInputEditText.setText(data.permanentProvinceOther)
                permanentProvince.visibility = View.INVISIBLE
                permanentOtherProvince.visibility = View.VISIBLE
            }

            if(!TextUtils.isEmpty(data.permanentCityOther)) {
                permanentOtherCity.textInputEditText.setText(data.permanentCityOther)
                permanentCity.visibility = View.INVISIBLE
                permanentOtherCity.visibility = View.VISIBLE
            }
            if(!TextUtils.isEmpty(data.permanentphoneNo)) {
                phoneNumbers.textview_1.setText(data.permanentphoneNo)
            }
            if(!TextUtils.isEmpty(data.permanentResidence)) {
                phoneNumbers.textview_2.setText(data.permanentResidence)
            }
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