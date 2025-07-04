package com.example.scstrade.views.aof.fragments.kyc.attorneyDetail

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
import com.example.scstrade.databinding.FragmentKycSevenBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.attorneyDetail.AttorneyDetailDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.contactDetail.KycContactDetailOneFragment
import com.example.scstrade.views.aof.fragments.kyc.contactDetail.KycContactDetailThreeFragment
import com.example.scstrade.views.aof.fragments.kyc.contactDetail.KycContactDetailTwoFragment
import com.example.scstrade.views.aof.fragments.kyc.nomineeDetail.KycNomineeDetailOneFragment


class KycAttorneyDetailOneFragment : Fragment() {
    lateinit var viewModel: AofViewModel
    lateinit var binding: FragmentKycSevenBinding

    override fun onDestroyView() {
        viewModel.mutableAttorneyDetail.value=null
        super.onDestroyView()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycSevenBinding.inflate(inflater,container,false)
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.attorney_details)
        viewModel = (requireActivity() as AofActivity).viewModel
        viewModel.getattorneyDetails()
        initFields()

        viewModel.mutableAttorneyDetail.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Log.e("Aof",result.message?:"")
                    Utils.showError(requireView(), result.message)
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    (requireActivity() as AofActivity).loadFragment(KycNomineeDetailOneFragment())
                }
            }
        })

        binding.apply {
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycContactDetailOneFragment())
            }
            btnContinue.setOnClickListener {
                if(someElseContainer.visibility == View.GONE){
                    viewModel.attorneyDetails(
                        AttorneyDetailDto(
                            isTheAtto.selectedOption.second,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                        )
                    )
                }else{
                    if(!labelledSpinner.isEmpty &&
                       !fullName.isEmpty &&
                        !uinType.isEmpty &&
                        !uinNumber.isEmpty &&
                        !isAttorney.isSelectedOtionEmpty &&
                        !mobileNumber.isEmpty &&
                        !emailAddr.isEmpty &&
                        !TextUtils.isEmpty(mailingAddress1.text.toString()) &&
                        !TextUtils.isEmpty(mailingAddress2.text.toString()) &&
                        !TextUtils.isEmpty(mailingAddress3.text.toString()) &&
                        !residenceNumber.isEmpty &&
                        !attorneyCountry.isEmpty){
                        viewModel.attorneyDetails(
                            AttorneyDetailDto(
                                isTheAtto.selectedOption.second,
                                fullName.selectedOption,
                                uinNumber.selectedOption,
                                isAttorney.textFieldValue,
                                isAttorney.selectedOption.second,
                                emailAddr.selectedOption,
                                null,
                                uinType.selectedDropDown.second,
                                residenceNumber.selectedOption,
                                mailingAddress1.text.toString(),
                                mailingAddress2.text.toString(),
                                mailingAddress3.text.toString(),
                                if(attorneyCountry.selectedDropDown.first.equals("pak",true))attorneyCity.selectedDropDown.first.first else null,
                                attorneyCountry.selectedDropDown.first,
                                if(attorneyCountry.selectedDropDown.first.equals("pak",true))attorneyProvince.selectedDropDown.second else null,
                                mobileNumber.selectedOption,
                                if(attorneyCountry.selectedDropDown.first.equals("pak",true))null else attorneyOtherCity.selectedOption,
                                if(attorneyCountry.selectedDropDown.first.equals("pak",true))null else attorneyOtherProvince.selectedOption,
                                labelledSpinner.selectedDropDown.second,
                            )
                        )
                    }else{
                        Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                    }

                }
            }
        }

        binding.isTheAtto.setOnButtonOneClickListener {
            binding.someElseContainer.visibility = View.GONE
        }
        binding.isTheAtto.setOnButtonTwoClickListener {
            binding.someElseContainer.visibility = View.VISIBLE
        }
        viewModel.mutableAttorneyDetail.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(requireView(),result.message?:"An error occurred...")
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    val response=result.data
                    if(response?.isSuccess?:false){
                        (requireActivity() as AofActivity).loadFragment(KycNomineeDetailOneFragment())
                    }else{
                        Utils.showError(requireView(),response?.message?:"An error occurred...")
                    }
                    binding.loader.visibility = View.GONE
                }
            }
        })
        return binding.root
    }

    private fun initFields() {
        binding.isTheAtto.setList(AppConstants.ATTORNEYTYPE.map { android.util.Pair(it.first,it.second) })
        binding.labelledSpinner.setListEntries(AppConstants.SALUTATION.map { android.util.Pair(it.first,it.second) })
        binding.uinType.setListEntries(AppConstants.NIC_TYPE_LIST.map { android.util.Pair(it.first,it.second) })
        binding.isAttorney.setList(AppConstants.LIFETIMECNICSTATUSLIST.map { android.util.Pair(it.first,it.second) })
        binding.attorneyCountry.setListEntries(AppConstants.COUNTRY.map { android.util.Pair(it.first,it.second) })
        binding.attorneyProvince.setListEntries(AppConstants.PROVINCE.map { android.util.Pair(it.first,it.second)  })
        binding.attorneyCity.setListEntries(AppConstants.CITY.map { android.util.Pair(android.util.Pair(it.first.first,it.first.second),it.second) })
        viewModel.mutableAttorneyDetailResponse.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val response = result.data?.data
                    binding.isTheAtto.setSelectedOption(response?.attorneyType)
                    if(response?.attorneyType.equals("S",true)){
                        binding.someElseContainer.visibility=View.GONE
                    }else{
                        binding.someElseContainer.visibility=View.VISIBLE
                    }
                    binding.labelledSpinner.setSelectedDropDown(response?.salutationAtr)
                    binding.fullName.selectedOption=response?.clientNameAtr
                    binding.uinType.setSelectedDropDown(response?.identificationAtr)
                    binding.uinNumber.selectedOption=response?.cnicAtr
                    binding.isAttorney.setSelectedOption(response?.cnicLifeTimeAtr)
                    binding.isAttorney.textFieldValue= response?.cnicExpiryDateAtr?.let { Utils.formatDateString(inputDate = it) }
                    binding.mobileNumber.selectedOption=response?.mobileAtr
                    binding.emailAddr.selectedOption=response?.emailAtr
                    if(!TextUtils.isEmpty(response?.mailingAddressAtr1)) {
                        binding.mailingAddress1.setText(response?.mailingAddressAtr1)
                    }
                    if(!TextUtils.isEmpty(response?.mailingAddressAtr2)) {
                        binding.mailingAddress2.setText(response?.mailingAddressAtr2)
                    }
                    if(!TextUtils.isEmpty(response?.mailingAddressAtr3)) {
                        binding.mailingAddress3.setText(response?.mailingAddressAtr3)
                    }

                    binding.residenceNumber.selectedOption=response?.landlineAtr

                    binding.attorneyCountry.setSelectedDropDown(response?.mailingCountryAtr)

                    binding.attorneyProvince.setSelectedDropDown(response?.mailingProvinceAtr)

                    binding.attorneyCity.setSelectedDropDown(response?.mailingCityAtr)

                }
            }
        })


    }



}