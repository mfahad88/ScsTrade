package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycOneBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass.
 * Use the [KycOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KycOneFragment : Fragment() {
    lateinit var binding: FragmentKycOneBinding
    lateinit var viewModel: AofViewModel
    var nationalityId:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycOneBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        populateDropdown()

        binding.apply {

            dobInputLayout.setOnFocusListener {
                if(it){
                    Utils.showDatePicker(requireContext()){ day, month, year ->
                        val customDate = LocalDate.of(year , month, day)
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val formatted = customDate.format(formatter)
                        dobInputLayout.textInputEditText.setText(formatted)
                    }
                }

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
                    (requireActivity() as AofActivity).loadFragment(KycTwoFragment())

                }else{
                    Utils.showError(requireView(), getString(R.string.empty_fields_not_allowed))
                }
            }
        }

        return binding.root
    }

    private fun populateDropdown() {

        binding.uinType.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.IDTYPE.map { it.first }))
        binding.dropdownTitle.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.SALUTATION.map { it.first }))
        binding.dropdownNationality.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.COUNTRY.map { it.first }))
        binding.dropdownNationality.setOnItemClickListener { adapterView, view, i, l ->
            nationalityId = AppConstants.COUNTRY.get(i).second
        }
    }

    private fun initFields() {
        val basicData =viewModel.getbasicData()
        binding.apply {
            uinType.setText(basicData.uinType)
            uinNumber.textInputEditText.setText(basicData.uinNumber)
            dropdownTitle.setText(basicData.salutation)
            fullName.textInputEditText.setText(basicData.fullNicName)
            dobInputLayout.textInputEditText.setText(basicData.dob)
            motherName.textInputEditText.setText(basicData.motherMaidenName)

            dropdownNationality.setText(AppConstants.COUNTRY.find { it.second.equals(basicData.nationality) }?.first)
        }
    }


}