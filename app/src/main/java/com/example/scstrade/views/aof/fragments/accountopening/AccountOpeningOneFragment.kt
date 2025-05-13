package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningOneBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.repository.AofRepository
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.WelcomeFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountOpeningOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountOpeningOneFragment : Fragment() {
    lateinit var binding: FragmentAccountOpeningOneBinding
    lateinit var viewModel: AofViewModel
    var resident=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningOneBinding.inflate(inflater,container,false)
        viewModel=(requireActivity() as AofActivity).viewModel

        initFields()
        binding.nicType.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.NIC_TYPE.map { it.keys.toString().replace("[","").replace("]","") }))

        binding.residentialStatus.apply {
            setOnButtonOneClickListener {

                resident= AppConstants.RESIDENTIAL_STATUS.firstNotNullOfOrNull { it["Resident"] }.toString()
            }
            setOnButtonTwoClickListener {
                resident= AppConstants.RESIDENTIAL_STATUS.firstNotNullOfOrNull { it["Non-Resident"] }
                    .toString()
            }
        }

        binding.btnContinue.setOnClickListener {
            val fname=binding.fullName.text
            val email=binding.emailAddress.text
            val nType=AppConstants.NIC_TYPE.firstNotNullOfOrNull { binding.nicType.text.toString() }.toString()
            val nicNumber=binding.nicCard.text

            if(fname.isNotEmpty() && email.isNotEmpty() && nType.isNotEmpty() && nicNumber.isNotEmpty() ){
                viewModel.saveSelfInfo(fname,email,resident,nType,nicNumber)
                binding.apply {
                    fullName.text=""
                    emailAddress.text=""
                    nicType.setText("")
                    nicCard.text=""
                }
                (requireActivity() as AofActivity).loadFragment(AccountOpeningTwoFragment())
            }else{
                Utils.showError(requireView(),"Empty fields not allowed")
            }


        }
        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(WelcomeFragment())
        }
        return binding.root
    }

    private fun initFields() {
        binding.apply {
            fullName.text = viewModel.getSelfInfo().fullName
            emailAddress.text = viewModel.getSelfInfo().emailAddress
            if(viewModel.getSelfInfo().residentialStatus.equals("01")){
                residentialStatus.toggleSelection(true)
                resident="01"
            }else if(viewModel.getSelfInfo().residentialStatus.equals("02")){
                residentialStatus.toggleSelection(false)
                resident="02"
            }

            nicType.setText(viewModel.getSelfInfo().nicType)
            nicCard.text=viewModel.getSelfInfo().nicNumber
        }
    }


}