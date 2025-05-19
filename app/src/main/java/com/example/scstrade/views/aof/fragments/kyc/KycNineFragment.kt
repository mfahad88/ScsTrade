package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycNineBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import okhttp3.internal.notify

class KycNineFragment : Fragment() {
    lateinit var binding:FragmentKycNineBinding
    lateinit var viewModel: AofViewModel
    var is_nominee:String?=null
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
        initFields()
        populationDropdown()

        binding.apply {
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycEightFragment())
            }
            nominee.setOnButtonOneClickListener {
                is_nominee="false"
                nomineeView.visibility=View.GONE
                nominee_relation=null
                nominee_name=null
                nominee_uin_type=null
                nominee_uin_number=null
            }

            nominee.setOnButtonTwoClickListener {
                is_nominee="true"
                nomineeView.visibility=View.VISIBLE
            }

            nomineeName.textInputEditText.addTextChangedListener {
                nominee_name = it.toString()
            }

            uinNumber.textInputEditText.addTextChangedListener {
                nominee_uin_number = it.toString()
            }

            btnContinue.setOnClickListener {
                viewModel.nominee.isNominee = is_nominee
                viewModel.savenominee()
                if(is_nominee=="true"){
                    if(!nominee_name.isNullOrEmpty() && !nominee_relation.isNullOrEmpty()
                        && !nominee_uin_type.isNullOrEmpty() && !nominee_uin_number.isNullOrEmpty()){

                        viewModel.nominee.apply {
                            nomineeRelation = nominee_relation
                            nomineeName = nominee_name
                            nomineeUinType = nominee_uin_type
                            nomineeUinNumber = nominee_uin_number
                        }
                        viewModel.savenominee()
                        (requireActivity() as AofActivity).loadFragment(KycTenFragment())
                    }else{
                        Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                    }
                }
            }

        }

        return binding.root
    }

    private fun initFields() {
        val nominee = viewModel.getnominee()

        nominee.apply {
            if(isNominee!=null){
                is_nominee = isNominee
                if(is_nominee=="true"){
                    binding.nominee.toggleSelection(false)
                    binding.nomineeView.visibility = View.VISIBLE
                }else{
                    binding.nominee.toggleSelection(true)
                    binding.nomineeView.visibility = View.GONE
                }
            }

            if(nomineeRelation!=""){
                nominee_relation =nomineeRelation
                binding.nomineeRelation.dropdown.setText(AppConstants.NomineeRelation.filter { it.second.equals(nominee_relation) }.map { it.first }.toString().replace("[","").replace("]",""))
            }

            if(nomineeName!=""){
                nominee_name =nomineeName
                binding.nomineeName.textInputEditText.setText(nominee_name)
            }

            if(nomineeUinType!=""){
                nominee_uin_type =nomineeUinType
                binding.uinType.dropdown.setText(AppConstants.IDTYPE.filter { it.second.equals(nominee_uin_type) }.map { it.first }.toString().replace("[","").replace("]",""))
            }

            if(nomineeUinNumber!=""){
                nominee_uin_number =nomineeUinNumber
                binding.uinNumber.textInputEditText.setText(nominee_uin_number)
            }


        }


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