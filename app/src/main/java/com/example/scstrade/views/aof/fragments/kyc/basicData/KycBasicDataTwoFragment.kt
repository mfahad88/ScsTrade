package com.example.scstrade.views.aof.fragments.kyc.basicData

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycTwoBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity

class KycBasicDataTwoFragment : Fragment() {
    lateinit var binding: FragmentKycTwoBinding
    var martialStatus = ""
    var relationship = ""
    lateinit var viewModel: AofViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycTwoBinding.inflate(inflater, container, false)
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.basic_data)
        initFields()

        binding.maritalStatus.apply {
            setOnButtonOneClickListener {
                martialStatus= AppConstants.MARITAL_STATUS.get(0).values.first()
            }
            setOnButtonTwoClickListener{
                martialStatus= AppConstants.MARITAL_STATUS.get(1).values.first()
            }
        }



        binding.apply {
            Utils.filterTextField(name, Regex("[^A-Za-z ]"))
            back.setOnClickListener {
                (requireActivity() as AofActivity).supportFragmentManager.popBackStack()
            }
            btnFather.setOnClickListener {
                btnFather.isSelected=true
                btnHusband.isSelected=false
                binding.textFather.setTextColor(Color.parseColor("#ffffff"))
                binding.textHusband.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.md_theme_primary
                    )
                )
                relationship= AppConstants.RELATIONSHIP.get(0).values.first()

            }

            btnHusband.setOnClickListener {
                btnFather.isSelected=false
                btnHusband.isSelected=true
                binding.textHusband.setTextColor(Color.parseColor("#ffffff"))
                binding.textFather.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.md_theme_primary
                    )
                )
                relationship= AppConstants.RELATIONSHIP.get(1).values.first()
            }

            btnContinue.setOnClickListener {
                if(martialStatus.isNotEmpty() && relationship.isNotEmpty() && name.text.isNotEmpty()){
                    viewModel.basicData.maritalStatus = martialStatus
                    viewModel.basicData.relationShip = relationship
                    viewModel.basicData.relationshipName = name.text.toString()
//                    viewModel.saveBasicData()
                    (requireActivity() as AofActivity).loadFragment(KycBasicDataThreeFragment())
                }else{
                    Utils.showError(requireView(), getString(R.string.empty_fields_not_allowed))
                }
            }
        }
        return binding.root
    }

    private fun initFields() {
       val basicData = viewModel.getbasicData()
        if(basicData!=null){
            binding.name.setText(basicData.relationshipName)
            if(basicData.maritalStatus.equals("s",true)){
                martialStatus= AppConstants.MARITAL_STATUS.get(0).values.first()
                binding.maritalStatus.toggleSelection(true)
            }else if(basicData.maritalStatus.equals("m",true)){
                martialStatus= AppConstants.MARITAL_STATUS.get(1).values.first()
                binding.maritalStatus.toggleSelection(false)
            }

            if(basicData.relationShip.equals("f",true)){
                binding.btnFather.isSelected = true
                binding.btnHusband.isSelected = false
                relationship= AppConstants.RELATIONSHIP.get(0).values.first()
                binding.textFather.setTextColor(Color.parseColor("#ffffff"))
                binding.textHusband.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.md_theme_primary
                    )
                )
            }else if(basicData.relationShip.equals("h",true)){
                binding.btnFather.isSelected = false
                binding.btnHusband.isSelected = true
                relationship= AppConstants.RELATIONSHIP.get(1).values.first()
                binding.textHusband.setTextColor(Color.parseColor("#ffffff"))
                binding.textFather.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.md_theme_primary
                    )
                )
            }
        }


    }

}