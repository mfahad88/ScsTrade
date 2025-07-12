package com.example.scstrade.views.aof.fragments.kyc.contactDetail
import androidx.compose.ui.res.dimensionResource

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycFourBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.contactDetails.ContactDetailDto
import com.example.scstrade.model.response.aof.contactDetails.ContactDetailResponse
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.attorneyDetail.KycAttorneyDetailOneFragment
import com.example.scstrade.views.aof.fragments.kyc.basicData.KycBasicDataOneFragment
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
        (requireActivity() as AofActivity).binding.apply {
            welcome.text = getString(R.string.contact_detail)
            steps.text="2/6"
            progressBar.setProgress(2)
            progressValue.setText("Progress (20%)")
        }
        fetchUser()
        viewModel.getcontactDetails()
        initField()




        binding.mailingCountry.dropdown.addTextChangedListener {
            if(it.toString().equals("Pakistan",true)){
                binding.mailingProvince.visibility=View.VISIBLE
                binding.mailingCity.visibility=View.VISIBLE
                binding.mailingOtherProvince.visibility=View.GONE
                binding.mailingOtherCity.visibility=View.GONE
            }else{
                binding.mailingProvince.visibility=View.GONE
                binding.mailingCity.visibility=View.GONE
                binding.mailingOtherProvince.visibility=View.VISIBLE
                binding.mailingOtherCity.visibility=View.VISIBLE
            }
        }

        binding.permanentCountry.dropdown.addTextChangedListener {
            if(it.toString().equals("Pakistan",true)){
                binding.permanentProvince.visibility=View.VISIBLE
                binding.permanentCity.visibility=View.VISIBLE
                binding.permanentOtherProvince.visibility=View.GONE
                binding.permanentOtherCity.visibility=View.GONE
            }else{
                binding.permanentProvince.visibility=View.GONE
                binding.permanentCity.visibility=View.GONE
                binding.permanentOtherProvince.visibility=View.VISIBLE
                binding.permanentOtherCity.visibility=View.VISIBLE
            }
        }
        binding.btnCopy.setOnClickListener {
            binding.apply {
                parmanentAddr1.text=mailingAddress1.text
                parmanentAddr2.text=mailingAddress2.text
                parmanentAddr3.text=mailingAddress3.text

                if(mailingCountry.visibility==View.VISIBLE){
                    permanentCountry.setSelectedDropDown(mailingCountry.selectedDropDown.first)
                }
                if(mailingProvince.visibility==View.VISIBLE){
                    permanentProvince.setSelectedDropDown(mailingProvince.selectedDropDown.second)
                }

                if(mailingCity.visibility==View.VISIBLE){
                    permanentCity.setSelectedDropDown(mailingCity.selectedDropDown.first.first)
                }


                permanentOtherProvince.selectedOption=mailingOtherProvince.selectedOption
                permanentOtherCity.selectedOption=mailingOtherCity.selectedOption
                phoneNumbers.textview_1.setText(officeResidenceNumber.textview_1.text)
                phoneNumbers.textview_2.setText(officeResidenceNumber.textview_2.text)
                if(permanentCountry.selectedDropDown.first.equals("pak",true)){
                    permanentProvince.visibility=View.VISIBLE
                    permanentCity.visibility = View.VISIBLE
                    permanentOtherProvince.visibility = View.GONE
                    permanentOtherCity.visibility = View.GONE
                }else{
                    permanentProvince.visibility=View.GONE
                    permanentCity.visibility = View.GONE
                    permanentOtherProvince.visibility = View.VISIBLE
                    permanentOtherCity.visibility = View.VISIBLE
                }
            }
        }


        binding.mailingProvince.dropdown.addTextChangedListener {province->

            val provinceId=AppConstants.PROVINCE.find { it.first.equals(province.toString()) }?.second
            if(!TextUtils.isEmpty(provinceId)) {
                binding.mailingCity.setListEntries(AppConstants.CITY.filter {
                    it.second.equals(
                        provinceId
                    )
                }.map {
                    android.util.Pair(
                        android.util.Pair(it.first.first, it.first.second),
                        it.second
                    )
                })
            }
        }

        binding.permanentProvince.dropdown.addTextChangedListener {province->

            val provinceId=AppConstants.PROVINCE.find { it.first.equals(province.toString()) }?.second
            if(!TextUtils.isEmpty(provinceId)) {
                binding.permanentCity.setListEntries(AppConstants.CITY.filter {
                    it.second.equals(
                        provinceId
                    )
                }.map {
                    android.util.Pair(
                        android.util.Pair(it.first.first, it.first.second),
                        it.second
                    )
                })
            }
        }

        viewModel.mutableCreateContactDetail.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Log.e("Aof",result.message?:"")
                    Utils.showError(requireView(), result.message)
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    (requireActivity() as AofActivity).loadFragment(KycAttorneyDetailOneFragment())
                }
            }
        })

        binding.apply {
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycBasicDataOneFragment())
            }

            btnContinue.setOnClickListener {




                if(!isEmpty(mailingAddress1) &&
                    !isEmpty(mailingAddress2) &&
                    !isEmpty(mailingAddress3) &&
                    !mailingCountry.isEmpty &&
                    !isEmpty(parmanentAddr1) &&
                    !isEmpty(parmanentAddr2) &&
                    !isEmpty(parmanentAddr3) &&
                    !permanentCountry.isEmpty &&
                    !isEmpty(officeResidenceNumber.textview_1)&&
                    !isEmpty(officeResidenceNumber.textview_2) &&
                    !isEmpty(phoneNumbers.textview_1)&&
                    !isEmpty(phoneNumbers.textview_2)
                    ){

                    viewModel.createContactDetail(
                        ContactDetailDto(
                            id = null,
                            mailingAddress1 = mailingAddress1.text.toString(),
                            mailingAddress2 = mailingAddress2.text.toString(),
                            mailingAddress3 = mailingAddress3.text.toString(),
                            mailingCountryId = mailingCountry.selectedDropDown.first,
                            mailingProvinceId =  if (mailingProvince.visibility == View.VISIBLE) mailingProvince.selectedDropDown.second else null,
                            mailingCityId = if (mailingCity.visibility == View.VISIBLE) mailingCity.selectedDropDown.first.first else null,
                            mailingCityOther = mailingOtherCity.selectedOption,
                            mailingProvinceOther = mailingOtherProvince.selectedOption,
                            mailingphoneNo = officeResidenceNumber.textview_1.text.toString(),
                            mailingResidence = officeResidenceNumber.textview_2.text.toString(),
                            mailingProof = mailingAddress1.text.toString(),
                            permanentProof = parmanentAddr1.text.toString(),
                            permanentAddress1 = parmanentAddr1.text.toString(),
                            permanentAddress2 = parmanentAddr2.text.toString(),
                            permanentAddress3 = parmanentAddr3.text.toString(),
                            permanentCountryId = permanentCountry.selectedDropDown.first,
                            permanentProvinceOther = permanentOtherProvince.selectedOption,
                            permanentCityOther = permanentOtherCity.selectedOption,
                            permanentProvinceId = if (permanentProvince.visibility == View.VISIBLE) permanentProvince.selectedDropDown.second else null,
                            permanentCityId = if (permanentCity.visibility == View.VISIBLE) permanentCity.selectedDropDown.first.first else null,
                            permanentphoneNo = phoneNumbers.textview_1.text.toString(),
                            permanentResidence = phoneNumbers.textview_1.text.toString(),
                        )
                    )

                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }
        }
        return binding.root
    }


    private fun isEmpty(editText: EditText): Boolean {
        return TextUtils.isEmpty(editText.toString())
    }

    private fun initField() {
        binding.apply {
            mobileNumber.textInputEditText.setText(login.registrationPhone)
            email.selectedOption=login.registrationEmail
            mailingCountry.setListEntries(AppConstants.COUNTRY.map { (label, code) -> android.util.Pair(label, code)  })
            mailingProvince.setListEntries(AppConstants.PROVINCE.map { (label, code) -> android.util.Pair(label, code)  })
            mailingCity.setListEntries(AppConstants.CITY.map { android.util.Pair(android.util.Pair(it.first.first,it.first.second),it.second) })

            permanentCountry.setListEntries(AppConstants.COUNTRY.map { (label, code) -> android.util.Pair(label, code)  })
            permanentProvince.setListEntries(AppConstants.PROVINCE.map { (label, code) -> android.util.Pair(label, code)  })
            permanentCity.setListEntries(AppConstants.CITY.map { android.util.Pair(android.util.Pair(it.first.first,it.first.second),it.second) })
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
                mailingAddress1.setText(data.mailingAddress1)
            }
            if(!TextUtils.isEmpty(data.mailingAddress2)) {
                mailingAddress2.setText(data.mailingAddress2)
            }
            if(!TextUtils.isEmpty(data.mailingAddress3)) {
                mailingAddress3.setText(data.mailingAddress3)
            }

            mailingCountry.setSelectedDropDown(data.mailingCountryId)
            mailingProvince.setSelectedDropDown(data.mailingProvinceId)
            mailingCity.setSelectedDropDown(data.mailingCityId)

            if(!TextUtils.isEmpty(data.mailingphoneNo)){
                officeResidenceNumber.textview_1.setText(data.mailingphoneNo)
            }

            if(!TextUtils.isEmpty(data.mailingResidence)){
                officeResidenceNumber.textview_2.setText(data.mailingResidence)
            }

            if(!TextUtils.isEmpty(data.permanentAddress1)){
                parmanentAddr1.setText(data.permanentAddress1)
            }


            if(!TextUtils.isEmpty(data.permanentAddress2)){
                parmanentAddr2.setText(data.permanentAddress2)
            }

            if(!TextUtils.isEmpty(data.permanentAddress3)){
                parmanentAddr3.setText(data.permanentAddress3)
            }

            mailingOtherProvince.selectedOption=data.mailingProvinceOther
            mailingOtherCity.selectedOption=data.mailingCityOther
            permanentCountry.setSelectedDropDown(data.permanentCountryId)
            permanentProvince.setSelectedDropDown(data.permanentProvinceId)
            permanentCity.setSelectedDropDown(data.permanentCityId)
            permanentOtherProvince.selectedOption=data.permanentProvinceOther
            permanentOtherCity.selectedOption=data.permanentCityOther
            phoneNumbers.textview_1.setText(data.permanentphoneNo)
            phoneNumbers.textview_2.setText(data.permanentResidence)
        }
    }


    private fun fetchUser() {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(requireContext(), emptyList<LoginDataItem>(),
            AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }

    override fun onDestroyView() {
        viewModel.mutableCreateContactDetail.value=null
        super.onDestroyView()
    }

}