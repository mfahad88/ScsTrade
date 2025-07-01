package com.example.scstrade.views.aof.fragments.kyc.basicData

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycOneBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.basicDetails.BasicDetailDto
import com.example.scstrade.model.response.aof.basicDetails.BasicDetailResponse
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.LoginAOFFragment
import com.example.scstrade.views.aof.fragments.kyc.contactDetail.KycContactDetailOneFragment
import com.example.scstrade.views.widgets.DualOptionToggleView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class KycBasicDataOneFragment : Fragment() {
    lateinit var binding: FragmentKycOneBinding
    lateinit var viewModel: AofViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycOneBinding.inflate(inflater, container, false)
        viewModel = (requireActivity() as AofActivity).viewModel
        viewModel.protectedAppId()
        viewModel.getBasicData()
        initFields()
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.basic_data)
        viewModel.mutableBasicData.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Log.e("Aof",result.message?:"")
                    Utils.showError(requireView(), result.message)
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    (requireActivity() as AofActivity).loadFragment(KycContactDetailOneFragment())
                }
            }
        })

        binding.cardNic.setOnButtonOneClickListener {
            binding.cardNic.editText.isEnabled=false
        }
        binding.cardNic.setOnButtonTwoClickListener {
            binding.cardNic.editText.isEnabled=true
        }

        binding.cardNic.editText.setOnFocusChangeListener { view, b ->
            if(b){
                Toast.makeText(requireContext(),binding.cardNic.selectedOption,Toast.LENGTH_SHORT).show()
                Utils.showDatePicker(requireContext()){
                    binding.cardNic.textFieldValue= it
                }

            }

            /*  if(binding.cardNic.selectedOption.equals("n",true)){

              }*/
        }
        binding.dobInputLayout.setOnFocusListener {
            if(it) {
                Utils.showDatePicker(requireContext(), "yyyy-MM-dd") { date ->
                    binding.dobInputLayout.textInputEditText.setText(date)
                }
            }
        }
        binding.btnContinue.setOnClickListener {
          binding.apply {
           /*   if(TextUtils.isEmpty(dropdownTitle.selectedDropDown)){
                    dropdownTitle.dropdown.error="Please select Saluation"
              }
              if(TextUtils.isEmpty(dobInputLayout.textInputEditText.text.toString())){
                  dobInputLayout.textInputEditText.error="Please select Dob"
              }

              if(TextUtils.isEmpty(motherName.textInputEditText.text.toString())){
                  motherName.textInputEditText.error= "Please provide mother maiden name"
              }

              if(TextUtils.isEmpty(dropdownNationality.selectedDropDown)){
                  motherName.textInputEditText.error= "Please select nationality"
              }

              if(TextUtils.isEmpty(maritalStatus.textFieldValue)){
                  motherName.textInputEditText.error= "Please select nationality"
              }

              if(TextUtils.isEmpty(relationship.selectedOption)){
                  relationship.editText.error = "Please select relation"
              }

              if(TextUtils.isEmpty(cardNic.selectedOption)){
                  cardNic.editText.error = "Please select nic expiry"
              }*/

                if(!TextUtils.isEmpty(uinType.selectedDropDown) &&
                    !TextUtils.isEmpty(uinNumber.textInputEditText.text) &&
                    !TextUtils.isEmpty(dropdownTitle.selectedDropDown) &&
                    !TextUtils.isEmpty(dobInputLayout.textInputEditText.text) &&
                    !TextUtils.isEmpty(motherName.textInputEditText.text) &&
                    !TextUtils.isEmpty(dropdownNationality.selectedDropDown) &&
                    !TextUtils.isEmpty(maritalStatus.selectedOption) &&
                    !TextUtils.isEmpty(relationship.selectedOption) &&
                    !TextUtils.isEmpty(cardNic.selectedOption) &&
                    !TextUtils.isEmpty(placeBirth.autoCompleteTextView1.text) &&
                    !TextUtils.isEmpty(placeBirth.autoCompleteTextView2.text) &&
                    !TextUtils.isEmpty(ivrService.selectedOption) ){
                    viewModel.basicData(BasicDetailDto(
                        id = 0,
                        salutation = dropdownTitle.selectedDropDown,
                        lifeTime = cardNic.selectedOption,
                        gender = if (dropdownTitle.selectedDropDown.equals("mr",true)) "M" else "F",
                        relationship = relationship.selectedOption,
                        fatherHusbandName = relationship.textFieldValue,
                        motherMaidenName = motherName.textInputEditText.text.toString(),
                        nationalityId = AppConstants.COUNTRY.filter { it.second.equals(dropdownNationality.selectedDropDown) }.map { it.second }.first(),
                        maritalStatus = maritalStatus.selectedOption,
                        placeOfBirth = AppConstants.COUNTRY.filter { it.second.equals(placeBirth.selectedDropDown1) }.map { it.first }.first(),
                        placeOfBirthCity = AppConstants.CITY
                                        .filter { it.first.first.equals(placeBirth.autoCompleteTextView2.text.toString(),true) }
                            .map { it.first.second }.first(),
                        ivrstatus = ivrService.selectedOption,
                        uinExpiryDate = cardNic.textFieldValue,
                        dateOfBirth = dobInputLayout.textInputEditText.text.toString()


                    ))
                }
          }
        }


        return binding.root
    }

    private fun initFields() {
        binding.apply {
            cardNic.editText.isEnabled=false
            uinType.setListEntries(AppConstants.NIC_TYPE_LIST.map { (label, code) -> android.util.Pair(label, code) })
            dropdownTitle.setListEntries(AppConstants.SALUTATION.map { (label, code) -> android.util.Pair(label, code) })
            dropdownNationality.setListEntries(AppConstants.COUNTRY.map { (label, code) -> android.util.Pair(label, code)  })
            placeBirth.setListEntriesFirst(AppConstants.COUNTRY.map { (label, code) -> android.util.Pair(label, code)  })
            placeBirth.setListEntriesSecond(AppConstants.CITY.map { android.util.Pair(it.first.first,it.first.second) })
            cardNic.setList(AppConstants.LIFETIMECNICSTATUSLIST.map { (label, code) -> android.util.Pair(label, code)  })
            relationship.setList(AppConstants.RELATIONSHIP_LIST.map { (label, code) -> android.util.Pair(label, code)  })
            maritalStatus.setList(AppConstants.MARITAL_STATUS_LIST.map { (label, code) -> android.util.Pair(label, code)  })
            ivrService.setList(AppConstants.IVRSTATUSLIST.map { (label, code) -> android.util.Pair(label, code)  })
        }

        viewModel.mutableProtected.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message)
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val data=result.data?.user
                    binding.apply {
                        uinType.dropdown.setText(AppConstants.NIC_TYPE_LIST.filter { it.second.equals(data?.identificationType) }.map { it.second }.first(),true)
                        uinType.isEnabled = false
                        fullName.textInputEditText.setText(data?.name)
                        fullName.textInputEditText.isEnabled = false
                        uinNumber.textInputEditText.setText(data?.uin)
                        uinNumber.textInputEditText.isEnabled = false
                    }


                }
            }
        })

        viewModel.mutableBasicDataResponse.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(requireView(),result.message)
                    binding.apply {
                        loader.visibility = View.GONE
                        groupMain.visibility = View.GONE
                    }
                }
                is Resource.Loading -> {
                    binding.apply {
                        loader.visibility = View.VISIBLE
                        groupMain.visibility = View.GONE
                    }
                }
                is Resource.Success -> {
                    binding.apply {
                        loader.visibility = View.GONE
                        groupMain.visibility = View.VISIBLE
                        val basicData = result.data?.data
                        if(basicData!=null) {
                            populateRecord(basicData)
                        }
                    }
                }
            }
        })
    }

    private fun populateRecord(basicData: BasicDetailResponse) {
        if(!TextUtils.isEmpty(basicData.salutation)){
            binding.dropdownTitle.dropdown.setText(basicData.salutation,true)
        }
        if(!TextUtils.isEmpty(basicData.dateOfBirth)){
            binding.dobInputLayout.textInputEditText.setText(basicData.dateOfBirth)
        }
        if(!TextUtils.isEmpty(basicData.motherMaidenName)){
            binding.motherName.textInputEditText.setText(basicData.motherMaidenName)
        }
        if(!TextUtils.isEmpty(basicData.nationalityId)){
            binding.dropdownNationality.dropdown.setText(AppConstants.COUNTRY.filter { it.second.equals(basicData.nationalityId) }.map { it.first }.first(),true)
        }
        if(!TextUtils.isEmpty(basicData.maritalStatus)){
            binding.maritalStatus.setSelectedOption(basicData.maritalStatus)
        }
        if(!TextUtils.isEmpty(basicData.relationship)){
            binding.relationship.setSelectedOption(basicData.relationship)
            if(!TextUtils.isEmpty(basicData.fatherHusbandName)){
                binding.relationship.textFieldValue=basicData.fatherHusbandName
            }
        }

        if(!TextUtils.isEmpty(basicData.lifeTime)){
            binding.cardNic.setSelectedOption(basicData.lifeTime)
            if(!TextUtils.isEmpty(basicData.uinExpiryDate)){
                binding.cardNic.textFieldValue = basicData.uinExpiryDate
            }
        }

        if(!TextUtils.isEmpty(basicData.placeOfBirth)){
            binding.placeBirth.autoCompleteTextView1.setText(
                AppConstants
                    .COUNTRY
                    .filter { it.second.equals(basicData.placeOfBirth,true)}
                    .map { it.first }.toString(),true)
        }

        if(!TextUtils.isEmpty(basicData.placeOfBirthCity)){
            binding.placeBirth.autoCompleteTextView2.setText(AppConstants
                .CITY
                .filter { it.first.equals(basicData.placeOfBirthCity) }
                .map { it.second }.first(),true)
        }

        if(!TextUtils.isEmpty(basicData.ivrstatus)){
            binding.ivrService.selectedOption=basicData.ivrstatus
        }

    }




}