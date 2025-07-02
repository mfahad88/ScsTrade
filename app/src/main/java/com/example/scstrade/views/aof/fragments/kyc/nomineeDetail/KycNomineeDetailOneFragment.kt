package com.example.scstrade.views.aof.fragments.kyc.nomineeDetail

import android.os.Bundle
import android.text.TextUtils
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


        return binding.root
    }

    private fun initFields() {
        binding.nominee.setList(AppConstants.NomineeType.map { android.util.Pair(it.first,it.second) })
        binding.nomineeRelation.setListEntries(AppConstants.NomineeRelation.map {android.util.Pair(it.first,it.second) })
        binding.uinType.setListEntries(AppConstants.NIC_TYPE_LIST.map { android.util.Pair(it.first,it.second) })
        binding.nomineeNic.setList(AppConstants.LIFETIMECNICSTATUSLIST.map { android.util.Pair(it.first,it.second) })
        viewModel.mutableNomineeDetailResponse.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val response = result.data?.data
                    binding.nominee.setSelectedOption(response?.nomineeType)
                    if(!TextUtils.isEmpty(response?.nomineeType)) {
                        if (response?.nomineeType.equals("Y")){
                            binding.nomineeView.visibility = View.VISIBLE
                            binding.nomineeRelation.setSelectedDropDown(response?.relationShipNmn)
                            binding.nomineeName.selectedOption=response?.nameNmn
                            binding.nomineeMobile.selectedOption = response?.mobileNoNmn
                            binding.uinType.setSelectedDropDown(response?.identificationNmn)
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