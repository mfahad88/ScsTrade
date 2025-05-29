package com.example.scstrade.views.aof.fragments.kyc.basicData

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycOneBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.LoginAOFFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [KycBasicDataOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KycBasicDataOneFragment : Fragment() {
    lateinit var binding: FragmentKycOneBinding
    lateinit var viewModel: AofViewModel
    var nationalityId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycOneBinding.inflate(inflater, container, false)
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.basic_data)
        populateDropdown()
        initFields()
        binding.apply {
            Utils.filterTextField(fullName.textInputEditText,Regex("[^A-Za-z ]"))
            Utils.filterTextField(motherName.textInputEditText,Regex("[^A-Za-z ]"))
            Utils.filterTextField(uinNumber.textInputEditText,Regex("[^\\d]"))
            uinNumber.textInputEditText.apply {
                inputType = InputType.TYPE_CLASS_NUMBER
                filters = arrayOf(InputFilter.LengthFilter(13))
            }
            fullName.textInputEditText.apply {
                inputType = InputType.TYPE_CLASS_TEXT
                filters = arrayOf(InputFilter.LengthFilter(30))
            }

            motherName.textInputEditText.apply {
                inputType = InputType.TYPE_CLASS_TEXT
                filters = arrayOf(InputFilter.LengthFilter(30))
            }

            dobInputLayout.setOnFocusListener {
                if(it){
                    Utils.showDatePicker(requireContext()) { day, month, year ->
                        val customDate = LocalDate.of(year, month, day)
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val formatted = customDate.format(formatter)
                        dobInputLayout.textInputEditText.setText(formatted)
                    }
                }

            }

            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(LoginAOFFragment())
            }

            btnContinue.setOnClickListener {
                if(uinType.text.isNotEmpty() && uinNumber.textInputEditText.text?.isNotEmpty()?:false && fullName.textInputEditText.text?.isNotEmpty()?:false
                    && dropdownTitle.text.isNotEmpty() && dobInputLayout.textInputEditText.text?.isNotEmpty()?:false
                    && motherName.textInputEditText.text?.isNotEmpty()?:false && !nationalityId.isNullOrEmpty()){

                    viewModel.basicData.uinType= uinType.text.toString()
                    viewModel.basicData.uinNumber= uinNumber.text.toString()
                    viewModel.basicData.salutation= dropdownTitle.text.toString()
                    viewModel.basicData.fullNicName= fullName.text.toString()
                    viewModel.basicData.dob= dobInputLayout.textInputEditText.text.toString()
                    viewModel.basicData.motherMaidenName= motherName.textInputEditText.text.toString()
                    viewModel.basicData.nationality= nationalityId
                    viewModel.saveBasicData()
                    (requireActivity() as AofActivity).loadFragment(KycBasicDataTwoFragment())

                }else{
                    Utils.showError(requireView(), getString(R.string.empty_fields_not_allowed))
                }
            }
        }

        return binding.root
    }

    private fun populateDropdown() {

        binding.uinType.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                AppConstants.IDTYPE.map { it.first })
        )
        binding.dropdownTitle.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                AppConstants.SALUTATION.map { it.first })
        )
        binding.dropdownNationality.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                AppConstants.COUNTRY.map { it.first })
        )
        binding.dropdownNationality.setOnItemClickListener { adapterView, view, i, l ->
            nationalityId = AppConstants.COUNTRY.get(i).second
        }
    }

    private fun initFields() {

        viewModel.mutableBasicDataResponse.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val response = result.data?.data
                    viewModel.basicData.apply {
                        if(response!=null){
                            salutation = response.salutation
                            nicType = response.lifeTime
                            relationShip = response.relationship
                            relationshipName = response.fatherHusbandName
                            motherMaidenName = response.motherMaidenName
                            nationality = response.nationalityId
                            maritalStatus = response.maritalStatus
                            pobCountry = response.placeOfBirth
                            pobCity  = response.placeOfBirthCity
                            ivrService = response.ivrstatus
                            dob = Utils.convertIsoToDate(response.dateOfBirth)
                            nicValid = Utils.convertIsoToDate(response.uinExpiryDate)
                            viewModel.saveBasicData()
                        }
                        val basicData =viewModel.getbasicData()
                        binding.apply {
                            uinType.setText(basicData?.uinType,false)
                            uinNumber.textInputEditText.setText(basicData?.uinNumber)
                            dropdownTitle.setText(basicData?.salutation,false)
                            fullName.textInputEditText.setText(basicData?.fullNicName)
                            dobInputLayout.textInputEditText.setText(basicData?.dob)
                            motherName.textInputEditText.setText(basicData?.motherMaidenName)

                            if(basicData?.nationality?.isNotEmpty() == true){
                                nationalityId = AppConstants.COUNTRY.get(AppConstants.COUNTRY.indexOfFirst {  it.second.equals(basicData?.nationality)}).second
                                dropdownNationality.setText(AppConstants.COUNTRY.get(AppConstants.COUNTRY.indexOfFirst {  it.second.equals(basicData?.nationality)}).first,false)
                            }


                        }
                    }
                }
            }
        })

    }


}