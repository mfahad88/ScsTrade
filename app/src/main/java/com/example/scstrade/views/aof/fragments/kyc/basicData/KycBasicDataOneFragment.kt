package com.example.scstrade.views.aof.fragments.kyc.basicData
import androidx.compose.ui.res.dimensionResource

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.text.TextUtils
import android.text.TextWatcher
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
//        viewModel.protectedAppId()
        viewModel.getBasicData()
        initFields()
        binding.ivrService.apply {
            setSelectedOption(AppConstants.IVRSTATUSLIST.filter { it.second.equals("y",true) }.map { it.second }.first())
            binding.btnSingle.isEnabled=false
            binding.btnMarried.isEnabled=false
        }
        (requireActivity() as AofActivity).binding.apply {
            welcome.text = getString(R.string.basic_data)
            progressBar.setProgress(1)
            steps.text="1/6"
            progressValue.setText("Progress (10%)")
        }
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
                Utils.showDatePicker(requireContext()){
                    binding.cardNic.textFieldValue= it
                }

            }

            /*  if(binding.cardNic.selectedOption.equals("n",true)){

              }*/
        }

        binding.placeBirth.autoCompleteTextView1.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0?.toString().equals("pakistan",true)){
                    binding.placeBirth.autoCompleteTextView2.isEnabled=false
                }else{
                    binding.placeBirth.autoCompleteTextView2.isEnabled=true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.dobInputLayout.setOnFocusListener {
            if(it) {
                Utils.showDatePicker(requireContext(), "dd-MM-yyyy") { date ->
                    binding.dobInputLayout.selectedOption=date
                }
            }
        }
        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(LoginAOFFragment())
        }

        binding.btnContinue.setOnClickListener {
          binding.apply {


                if(!uinType.isEmpty &&
                    !uinNumber.isEmpty &&
                    !dropdownTitle.isEmpty &&
                    !dobInputLayout.isEmpty &&
                    !motherName.isEmpty &&
                    !dropdownNationality.isEmpty &&
                    !maritalStatus.isSelectedOtionEmpty &&
                    !relationship.isSelectedOtionEmpty &&
                    !cardNic.isSelectedOtionEmpty &&
                    !TextUtils.isEmpty(placeBirth.autoCompleteTextView1.text) &&
                    !ivrService.isSelectedOtionEmpty ){
                    viewModel.basicData(BasicDetailDto(
                        salutation = dropdownTitle.selectedDropDown.second,
                        lifeTime = cardNic.selectedOption.second,
                        gender = if (dropdownTitle.selectedDropDown.second.equals("mr",true)) "M" else "F",
                        relationship = relationship.selectedOption.second,
                        fatherHusbandName = relationship.textFieldValue,
                        motherMaidenName = motherName.selectedOption,
                        nationalityId = dropdownNationality.selectedDropDown.first,
                        maritalStatus = maritalStatus.selectedOption.second,
                        placeOfBirth = AppConstants.COUNTRY.filter { it.second.equals(placeBirth.autoCompleteTextView1.text.toString()) }.map { it.first }.first(),
                        placeOfBirthCity = if(binding.placeBirth.autoCompleteTextView2.isEnabled) AppConstants.CITY
                                        .filter { it.first.second.equals(placeBirth.autoCompleteTextView2.text.toString(),true) }
                            .map { it.first.first }.first() else null,
                        ivrstatus = ivrService.selectedOption.second,
                        uinExpiryDate = cardNic.textFieldValue,
                        dateOfBirth = Utils.formatDateString(dobInputLayout.selectedOption,"dd-MM-yyyy","yyyy-MM-dd")


                    ))
                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
          }
        }


        return binding.root
    }

    private fun initFields() {

        binding.apply {
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
                        uinType.setListEntries(AppConstants.NIC_TYPE_LIST.map { (label, code) -> android.util.Pair(label, code) })
                        uinType.setSelectedDropDown(data?.identificationType)
                        uinType.isEnabled = false
                        fullName.selectedOption = data?.name
                        fullName.textInputEditText.isEnabled = false
                        uinNumber.selectedOption = data?.uin
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
        binding.dropdownTitle.setSelectedDropDown(basicData.salutation)
        binding.dobInputLayout.selectedOption=Utils.formatDateString(inputDate = basicData.dateOfBirth.toString())
        binding.motherName.selectedOption = basicData.motherMaidenName
        binding.dropdownNationality.setSelectedDropDown(basicData.nationalityId)
        binding.maritalStatus.setSelectedOption(basicData.maritalStatus)
        binding.relationship.setSelectedOption(basicData.relationship)
        binding.relationship.setSelectedOption(basicData.relationship)
        binding.relationship.textFieldValue=basicData.fatherHusbandName

        if(basicData.lifeTime?.equals("Y",true)?:false){
            binding.cardNic.editText.isEnabled=false
        }
        binding.cardNic.setSelectedOption(basicData.lifeTime)
        if(!TextUtils.isEmpty(basicData.uinExpiryDate)) {
            binding.cardNic.textFieldValue =
                Utils.formatDateString(inputDate = basicData.uinExpiryDate.toString())
        }
        if(!TextUtils.isEmpty(basicData.placeOfBirthCity)){
            binding.placeBirth.autoCompleteTextView2.setText(AppConstants
                .CITY
                .filter { it.first.first.equals(basicData.placeOfBirthCity) }
                .map { it.first.second }.first(),false)
        }

        if(!TextUtils.isEmpty(basicData.placeOfBirth)){
            if(basicData.placeOfBirth?.equals("pak",true)?:false){
                binding.placeBirth.dropdown_2.isEnabled=true
                binding.placeBirth.autoCompleteTextView2.isEnabled=true
            }else{
                binding.placeBirth.dropdown_2.isEnabled=false
                binding.placeBirth.autoCompleteTextView2.isEnabled=false
            }
            binding.placeBirth.setSelectedDropDown1(basicData.placeOfBirth)
        }



        binding.ivrService.setSelectedOption(basicData.ivrstatus)

    }


    override fun onDestroyView() {
        viewModel.mutableBasicData.value=null
        viewModel.mutableBasicDataResponse.value=null
        super.onDestroyView()
    }

}