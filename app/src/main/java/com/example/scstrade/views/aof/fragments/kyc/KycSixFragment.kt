package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycFiveBinding
import com.example.scstrade.databinding.FragmentKycSixBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity

/**
 * A simple [Fragment] subclass.
 * Use the [KycSixFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KycSixFragment : Fragment() {
    lateinit var viewModel: AofViewModel
    lateinit var binding: FragmentKycSixBinding
    var parmanent_address:String?=null
    var parmanent_country:String?=null
    var parmanent_province:String?=null
    var parmanent_city:String?=null
    var parmanent_office_number:String?=null
    var parmanent_residence_number:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = (requireActivity() as AofActivity).viewModel
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

            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycFiveFragment())
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
                    }
                    (requireActivity() as AofActivity).loadFragment(KycSevenFragment())
                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }

            }
        }

        return binding.root
    }

    private fun initFields() {
        val contactDetail=viewModel.getContactDetails()
        contactDetail.apply {
            binding.parmanentAddr.setText(parmanentAddress )
            parmanent_address = parmanentAddress
            binding.permanentCountry.dropdown.setText(parmanentCountry)
            parmanent_country = parmanentCountry
            binding.permanentProvince.dropdown.setText(parmanentProvince)
            parmanent_province = parmanentProvince
            binding.permanentCity.dropdown.setText(parmanentCity)
            parmanent_city = parmanentCity
            binding.phoneNumbers.textview_1.setText(parmanentOfficeNumber)
            parmanent_office_number = parmanentOfficeNumber
            binding.phoneNumbers.textview_2.setText(parmanentResidenceNumber)
            parmanent_residence_number = parmanentResidenceNumber
        }
    }

    private fun populateDropdown() {
        binding.apply {
            permanentCountry.setEntries(AppConstants.COUNTRY.map { it.first }.toList())
            permanentProvince.setEntries(AppConstants.PROVINCE.map { it.first }.toList())
            permanentCity.setEntries(AppConstants.CITY.map { it.first }.toList())

            permanentCountry.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                parmanent_country=AppConstants.COUNTRY.get(i).first
            }

            permanentProvince.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                parmanent_province=AppConstants.PROVINCE.get(i).first
            }

            permanentCity.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                parmanent_city=AppConstants.CITY.get(i).first
            }
        }
    }

}