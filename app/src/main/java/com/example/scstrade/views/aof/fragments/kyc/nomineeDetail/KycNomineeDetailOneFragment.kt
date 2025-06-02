package com.example.scstrade.views.aof.fragments.kyc.nomineeDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycNineBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.nomineeDetail.NomineeDetailDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.attorneyDetail.KycAttorneyDetailOneFragment
import com.example.scstrade.views.aof.fragments.kyc.otherDetail.KycOtherDetailOneFragment
import com.example.scstrade.views.aof.fragments.kyc.attorneyDetail.KycAttorneyDetailTwoFragment

class KycNomineeDetailOneFragment : Fragment() {
    lateinit var binding:FragmentKycNineBinding
    lateinit var viewModel: AofViewModel
    var is_nominee:String?=null
    var nominee_mobile:String? = null
    var nominee_relation:String? = null
    var nominee_name:String? = null
    var nominee_uin_type:String? = null
    var nominee_uin_number:String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycNineBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.nominee_details)
        initFields()
        populationDropdown()

        binding.apply {
            back.setOnClickListener {
                if(viewModel.attorneyDetail.attorneyType.equals("s",true)){
                    (requireActivity() as AofActivity).loadFragment(KycAttorneyDetailOneFragment())
                }else {
                    (requireActivity() as AofActivity).loadFragment(KycAttorneyDetailTwoFragment())
                }
            }
            Utils.filterTextField(nomineeName.textInputEditText, Regex("^[A-Za-z] "))
            Utils.filterTextField(nomineeMobile.textInputEditText, Regex("^[0-9]"))
            Utils.filterTextField(uinNumber.textInputEditText, Regex("^[0-9]"))
            nomineeName.textInputEditText
            nomineeMobile.textInputEditText.addTextChangedListener {
                nominee_mobile = it.toString()
            }
            nominee.setOnButtonOneClickListener {
                is_nominee=AppConstants.NomineeType.get(1).second
                nomineeView.visibility=View.GONE
                viewModel.nominee.isNominee = is_nominee
                nominee_relation=null
                nominee_name=null
                nominee_uin_type=null
                nominee_uin_number=null


            }

            nominee.setOnButtonTwoClickListener {
                is_nominee=AppConstants.NomineeType.get(0).second
                nomineeView.visibility=View.VISIBLE
                viewModel.nominee.isNominee = is_nominee
            }

            nomineeName.textInputEditText.addTextChangedListener {
                nominee_name = it.toString()
            }

            uinNumber.textInputEditText.addTextChangedListener {
                nominee_uin_number = it.toString()
            }

            btnContinue.setOnClickListener {

//                viewModel.savenominee()
                if(is_nominee.equals("Y")){
                    if(!nominee_name.isNullOrEmpty() && !nominee_relation.isNullOrEmpty()
                        && !nominee_uin_type.isNullOrEmpty() && !nominee_uin_number.isNullOrEmpty() && !nominee_mobile.isNullOrEmpty()){

                        viewModel.nominee.apply {
                            nomineeRelation = nominee_relation
                            nomineeMobileNumber = nominee_mobile
                            nomineeName = nominee_name
                            nomineeUinType = nominee_uin_type
                            nomineeUinNumber = nominee_uin_number
                        }
//                        viewModel.savenominee()
                        (requireActivity() as AofActivity).loadFragment(KycNomineeDetailTwoFragment())
                    }else{
                        Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                    }
                }else{
                    viewModel.nomineeDetails(
                        NomineeDetailDto(
                            id = null,
                            cnicNmn = null,
                            addressNmn = null,
                            mobileNoNmn = null,
                            nicBackNmn = null,
                            nicFrontNmn = null,
                            nameNmn = null,
                            relationShipNmn = null,
                            cnicLifeTimeNmn = null,
                            cnicExpiryDateNmn = null,
                            identificationNmn = null,
                            nomineeType = is_nominee
                        )
                    )
                }
            }

            viewModel.mutableNomineeDetail.observe(viewLifecycleOwner, Observer { result->
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
                            (requireActivity() as AofActivity).loadFragment(KycOtherDetailOneFragment())
                        }else{
                            Utils.showError(requireView(),response?.message?:"An error occurred...")
                        }
                        binding.loader.visibility = View.GONE
                        viewModel.mutableNomineeDetail.value = null
                    }
                }
            })

        }

        return binding.root
    }

    private fun initFields() {

        viewModel.mutableNomineeDetailResponse.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val response = result.data?.data
                    viewModel.nominee.apply {
                        if(response!=null){
                            response.apply {
                                nomineeAddress=	addressNmn
                                nomineeNicExpiry=	cnicExpiryDateNmn
                                nomineeNicType=	cnicLifeTimeNmn
                                nomineeUinNumber=	cnicNmn
                                nomineeUinType=	identificationNmn
                                nomineeMobileNumber=	mobileNoNmn
                                nomineeName=	nameNmn
                                nomineeNicBack=	nicBackNmn
                                nomineeNicFront=	nicFrontNmn
                                isNominee=	nomineeType
                                nomineeRelation=	relationShipNmn
                                viewModel.savenominee()
                            }
                        }
                        val nominee =  viewModel.nominee

                        nominee.apply {
                            if(isNominee!=null){
                                is_nominee = isNominee
                                if(is_nominee.equals("Y")){
                                    binding.nominee.toggleSelection(false)
                                    binding.nomineeView.visibility = View.VISIBLE
                                }else{
                                    binding.nominee.toggleSelection(true)
                                    binding.nomineeView.visibility = View.GONE
                                }
                            }
                            if(nomineeMobileNumber!="" && nomineeMobileNumber!=null){
                                nominee_mobile = nomineeMobileNumber
                                binding.nomineeMobile.textInputEditText.setText(nominee_mobile)
                            }
                            if(nomineeRelation!="" && nomineeRelation!=null){
                                nominee_relation =nomineeRelation
                                binding.nomineeRelation.dropdown.setText(AppConstants.NomineeRelation.filter { it.second.equals(nominee_relation) }.map { it.first }.toString().replace("[","").replace("]",""))
                            }

                            if(nomineeName!="" && nomineeName!=null){
                                nominee_name =nomineeName
                                binding.nomineeName.textInputEditText.setText(nominee_name)
                            }

                            if(nomineeUinType!="" && nomineeUinType!=null){
                                nominee_uin_type =nomineeUinType
                                binding.uinType.dropdown.setText(AppConstants.IDTYPE.filter { it.second.equals(nominee_uin_type) }.map { it.first }.toString().replace("[","").replace("]",""))
                            }

                            if(nomineeUinNumber!="" && nomineeUinNumber!=null){
                                nominee_uin_number =nomineeUinNumber
                                binding.uinNumber.textInputEditText.setText(nominee_uin_number)
                            }


                        }
                    }
                }
            }
        })



    }

    private fun populationDropdown() {
        binding.apply {
            nomineeRelation.setEntries(AppConstants.NomineeRelation.map { it.first }.toList())
            uinType.setEntries(AppConstants.IDTYPE.map { it.first }.toList())

            nomineeRelation.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                nominee_relation = AppConstants.NomineeRelation.get(i).second
            }

            uinType.dropdown.setOnItemClickListener { adapterView, view, i, l ->
                nominee_uin_type = AppConstants.IDTYPE.get(i).second
            }
        }
    }


}