package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningTwoBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity

/**
 * A simple [Fragment] subclass.
 * Use the [AccountOpeningTwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountOpeningTwoFragment : Fragment() {
    lateinit var binding: FragmentAccountOpeningTwoBinding
    lateinit var viewModel: AofViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningTwoBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        binding.textInputMobile.setEntries(AppConstants.RELATIVE_RELATION.map { it.keys.toString().replace("[","").replace("]","") })
        binding.textInputBank.setEntries(AppConstants.BANK_SWIFT_CODES.map { it.keys.toString().replace("[","").replace("]","") })

        binding.textInputBank.dropdown.setOnItemClickListener { adapterView, view, i, l ->
            binding.bankSwift.text = adapterView.getItemAtPosition(i) as String
        }
        binding.cCode.text = binding.countryCode.text
        binding.code.addTextChangedListener {
            binding.bankCode.text = it
        }
        binding.iban.textInputEditText.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                binding.ibanCode.text = p0
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.btnContinue.setOnClickListener {
            val mobileNumber = binding.mobileNumber.text
            val registerUnder= binding.textInputMobile.dropdown.text.toString()
            val countryCode = binding.countryCode.text
            val bankCode = binding.bankCode.text
            val bankSwift=binding.bankSwift.text
            val bankIban=binding.ibanCode.text

            if(mobileNumber.isNotEmpty() && registerUnder.isNotEmpty() && countryCode.isNotEmpty() && bankCode.isNotEmpty() && bankSwift.isNotEmpty() && bankIban.isNotEmpty()){
                val iban="${countryCode}|${bankCode}|${bankSwift}|${bankIban}"
                viewModel.saveContactIban(mobileNumber,registerUnder,iban)
                (requireActivity() as AofActivity).loadFragment(AccountOpeningThreeFragment())
            }else{
                Utils.showError(requireView(),"Empty Fields not allowed...")
            }

        }

        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(AccountOpeningOneFragment())
        }
        return binding.root
    }

    private fun initFields() {
        binding.apply {
            mobileNumber.text = viewModel.getContactIban().mobileNumber
            textInputMobile.dropdown.setText(viewModel.getContactIban().registerUnder)
            if(viewModel.getContactIban().ibanNumber?.isNotEmpty()?:false) {
                val ibanC = viewModel.getContactIban().ibanNumber?.split("|")
                countryCode.text = ibanC?.get(0) ?: ""
                code.setText(ibanC?.get(1) ?: "")
                textInputBank.dropdown.setText(ibanC?.get(2))
                iban.textInputEditText.setText(ibanC?.get(3))
                cCode.text=countryCode.text
                bankCode.text = code.text
                bankSwift.text = textInputBank.dropdown.text
                ibanCode.text = iban.textInputEditText.text
            }

        }
    }


}