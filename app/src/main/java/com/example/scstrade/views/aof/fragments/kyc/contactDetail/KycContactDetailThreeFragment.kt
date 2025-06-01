package com.example.scstrade.views.aof.fragments.kyc.contactDetail

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycSixBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.contactDetails.ContactDetailDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.attorneyDetail.KycAttorneyDetailOneFragment

/**
 * A simple [Fragment] subclass.
 * Use the [KycContactDetailThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KycContactDetailThreeFragment : Fragment() {
    lateinit var viewModel: AofViewModel
    lateinit var binding: FragmentKycSixBinding
    var parmanent_address:String?=null
    var parmanent_country:String?=null
    var parmanent_province:String?=null
    var parmanent_province_other:String?=null
    var parmanent_city:String?=null
    var parmanent_city_other:String?=null
    var parmanent_office_number:String?=null
    var parmanent_residence_number:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.contact_detail)
        binding = FragmentKycSixBinding.inflate(inflater,container,false)
        initFields()
        populateDropdown()

        binding.apply {
            phoneNumbers.textview_1.inputType=InputType.TYPE_CLASS_PHONE
            phoneNumbers.textview_2.inputType=InputType.TYPE_CLASS_PHONE
            parmanentAddr.addTextChangedListener {
                parmanent_address=it.toString()
            }

            phoneNumbers.textview_1.addTextChangedListener {
                parmanent_office_number=it.toString()
            }

            phoneNumbers.textview_2.addTextChangedListener {
                parmanent_residence_number=it.toString()
            }

            Utils.filterTextField(permanentOtherProvince.textInputEditText,Regex("[^A-Za-z ]"))
            Utils.filterTextField(permanentOtherCity.textInputEditText,Regex("[^A-Za-z ]"))


            permanentOtherProvince.textInputEditText.addTextChangedListener {
                parmanent_province_other = it.toString()
            }

            permanentOtherCity.textInputEditText.addTextChangedListener {
                parmanent_city_other = it.toString()
            }

            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycContactDetailTwoFragment())
            }

            btnContinue.setOnClickListener {
                if(parmanent_address?.isNotEmpty()?:false && parmanent_country?.isNotEmpty()?:false &&
                    parmanent_province?.isNotEmpty()?:false && parmanent_city?.isNotEmpty()?:false){
                    viewModel.contactDetail.apply {
                        parmanentAddress=parmanent_address
                        parmanentCountry=parmanent_country
                        parmanentProvince = parmanent_province
                        parmanentCity = parmanent_city
                        parmanentOfficeNumber= parmanent_office_number
                        parmanentResidenceNumber = parmanent_residence_number
                        permanentCityOther= parmanent_city_other
                        permanentProvinceOther = parmanent_province_other

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
                                permanentAddress1 = parmanentAddress,
                                permanentCountryId = parmanentCountry,
                                permanentCityId = parmanentCity,
                                permanentCityOther = permanentCityOther,
                                permanentProvinceId = parmanentProvince,
                                permanentProvinceOther = permanentProvinceOther,
                                permanentphoneNo = parmanentOfficeNumber,
                                permanentResidence = parmanentResidenceNumber,
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

                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }

            }

            viewModel.mutableCreateContactDetail.observe(viewLifecycleOwner, Observer { result->
                when(result){
                    is Resource.Error -> {
                        Utils.showError(requireView(),result.message?:"An error occurred")
                        binding.loader.visibility = View.GONE
                    }
                    is Resource.Loading ->{
                        binding.loader.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        if(result.data?.statusCode==200){
                            (requireActivity() as AofActivity).loadFragment(
                                KycAttorneyDetailOneFragment()
                            )
                        }else{
                            Utils.showError(requireView(),result.data?.message?:"An error occurred")
                        }

                        binding.loader.visibility = View.GONE
                    }
                }
            })
        }

        return binding.root
    }

    private fun initFields() {
        val contactDetail=viewModel.contactDetail
        contactDetail.apply {
            binding.parmanentAddr.setText(parmanentAddress )
            parmanent_address = parmanentAddress
            binding.permanentCountry.dropdown.setText(parmanentCountry,false)
            parmanent_country = parmanentCountry
            binding.permanentProvince.dropdown.setText(parmanentProvince,false)
            parmanent_province = parmanentProvince
            binding.permanentCity.dropdown.setText(parmanentCity,false)
            parmanent_city = parmanentCity
            binding.phoneNumbers.textview_1.setText(parmanentOfficeNumber)
            parmanent_office_number = parmanentOfficeNumber
            binding.phoneNumbers.textview_2.setText(parmanentResidenceNumber)
            parmanent_residence_number = parmanentResidenceNumber
            binding.permanentOtherCity.textInputEditText.setText(permanentCityOther)
            binding.permanentOtherProvince.textInputEditText.setText(permanentProvinceOther)
        }
    }

    private fun populateDropdown() {
        binding.apply {
            permanentCountry.setEntries(AppConstants.COUNTRY.map { it.first }.toList())
            permanentProvince.setEntries(AppConstants.PROVINCE.map { it.first }.toList())
            permanentCity.setEntries(AppConstants.CITY.map { it.first.first }.toList())

            permanentCountry.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                parmanent_country=AppConstants.COUNTRY.get(i).second

                if(parmanent_country.equals("pak",true)){
                    binding.apply {
                        permanentProvince.visibility = View.VISIBLE
                        permanentCity.visibility = View.VISIBLE

                        permanentOtherProvince.visibility = View.INVISIBLE
                        permanentOtherCity.visibility = View.INVISIBLE
                    }
                }else{
                    permanentProvince.visibility = View.INVISIBLE
                    permanentCity.visibility = View.INVISIBLE

                    permanentOtherProvince.visibility = View.VISIBLE
                    permanentOtherCity.visibility = View.VISIBLE
                }
            }

            permanentProvince.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                parmanent_province=AppConstants.PROVINCE.get(i).second
                parmanent_province_other = AppConstants.PROVINCE.get(i).first
            }

            permanentCity.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                parmanent_city=AppConstants.CITY.get(i).first.second
                parmanent_city_other=AppConstants.CITY.get(i).first.first
            }
        }
    }

}