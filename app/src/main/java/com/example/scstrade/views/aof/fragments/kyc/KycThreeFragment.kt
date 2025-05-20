package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycThreeBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.aof.basicDetails.BasicDetailDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.widgets.DualDropdownSelectorView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [KycThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KycThreeFragment : Fragment() {
    lateinit var viewModel: AofViewModel
    lateinit var binding:FragmentKycThreeBinding
    var country:String?=null
    var city:String?=null
    var ivrStatus:String?=null
    var nicType:String?=null
    var nicExpiry:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycThreeBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        populateDropdown()

        binding.cardNic.apply {
            setOnButtonOneClickListener {
                nicType=AppConstants.LIFETIMECNICSTATUS.get(1).values.first()
                editText.isEnabled=true
            }
            setOnButtonTwoClickListener {
                nicType=AppConstants.LIFETIMECNICSTATUS.get(0).values.first()
                editText.setText("")
                editText.isEnabled=false
            }
           editText.setOnFocusChangeListener { view, b ->
               if(b){
                   Utils.showDatePicker(requireContext()){ day, month, year ->
                       val customDate = LocalDate.of(year , month, day)
                       val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                       val formatted = customDate.format(formatter)
                       editText.setText(formatted)
                       nicExpiry = formatted
                   }
               }
           }
        }
        binding.placeBirth.autoCompleteTextView1.setOnItemClickListener { adapterView, view, i, l ->
            country=AppConstants.COUNTRY.get(i).second
        }
        binding.placeBirth.autoCompleteTextView2.setOnItemClickListener { adapterView, view, i, l ->
            city=AppConstants.CITY.get(i).first.second
        }


        binding.ivrService.apply {
            setOnButtonOneClickListener {
                ivrStatus = AppConstants.IVRSTATUS.get(1).values.first()
            }
            setOnButtonTwoClickListener {
                ivrStatus = AppConstants.IVRSTATUS.get(0).values.first()
            }
        }
        binding.apply {
            btnContinue.setOnClickListener {
                if(country?.isNotEmpty()?:false && city?.isNotEmpty()?:false && ivrStatus?.isNotEmpty()?:false){
                    viewModel.basicData.pobCountry=country
                    viewModel.basicData.pobCity=city
                    viewModel.basicData.ivrService=ivrStatus
                    viewModel.basicData.nicType = nicType
                    viewModel.basicData.nicValid = nicExpiry
                    viewModel.saveBasicData()
                    viewModel.basicData(
                        BasicDetailDto(
                            id = null,
                            salutation = viewModel.basicData.salutation?:"",
                            lifeTime = viewModel.basicData.nicType?:"",
                            gender = if(viewModel.basicData.salutation.equals("MR")) "M" else "F",
                            relationship = viewModel.basicData.relationShip?:"",
                            fatherHusbandName = viewModel.basicData.relationshipName?:"",
                            motherMaidenName = viewModel.basicData.motherMaidenName?:"",
                            nationalityId = viewModel.basicData.nationality?:"",
                            maritalStatus =  viewModel.basicData.maritalStatus?:"",
                            placeOfBirth = viewModel.basicData.pobCountry?:"",
                            placeOfBirthCity = viewModel.basicData.pobCity?:"",
                            ivrstatus = viewModel.basicData.ivrService?:"",
                            dateOfBirth = viewModel.basicData.dob?:"",
                            uinExpiryDate = viewModel.basicData.nicValid
                        )
                    )

                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycTwoFragment())
            }
        }

        viewModel.mutableBasicData.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val response=result.data
                    if(response?.isSuccess?:false && response?.statusCode==200){
                        (requireActivity() as AofActivity).loadFragment(KycFourFragment())
                    }else{
                        Utils.showError(requireView(),response?.message?:"An error occurred...")
                    }
                }
            }
        })
        return binding.root
    }

    private fun initFields() {
        val basicData = viewModel.getbasicData()

        nicExpiry=basicData.nicValid
        nicType=basicData.nicType
        country=basicData.pobCountry
        city=basicData.pobCity
        ivrStatus = basicData.ivrService

        binding.apply {
            cardNic.toggleSelection(if(nicType.equals("y",true)) false else true)
            cardNic.editText.setText(nicExpiry)
            if(country?.isNotEmpty()?:false) {
                placeBirth.autoCompleteTextView1.setText(AppConstants.COUNTRY.filter { it.second.equals(country,true) }.map { it.first }.first())
            }

            if(city?.isNotEmpty()?:false){
                placeBirth.autoCompleteTextView2.setText(AppConstants.CITY.filter { it.first.second.equals(city,true) }.map { it.first.first }.first())
            }
            ivrService.toggleSelection(if(ivrStatus.equals("y",true)) false else true)
        }
    }

    private fun populateDropdown() {
        binding.placeBirth.autoCompleteTextView1.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.COUNTRY.map { it.first }))
        binding.placeBirth.autoCompleteTextView2.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.CITY.map { it.first.first }))

    }

}