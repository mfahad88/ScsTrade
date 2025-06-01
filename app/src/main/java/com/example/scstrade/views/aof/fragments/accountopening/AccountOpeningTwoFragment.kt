package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
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
    var mobile_number = ""
    var mobile_register = ""
    var relative_name = ""
    var uin_number = ""
    var bank_code = ""
    var bank_name = ""
    var iban_number = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningTwoBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        populateDropdown()
        binding.apply {
            mobileNumber.textInputEditText.addTextChangedListener {
                mobile_number = it.toString()
                viewModel.accountOpening.accountopeningmobileNumber = mobile_number
            }

            relative.apply {
                text1.addTextChangedListener {
                    relative_name=it.toString()
                    viewModel.accountOpening.accountopeningrelativeName=relative_name

                }

                text2.addTextChangedListener {
                    uin_number = it.toString()
                    viewModel.accountOpening.accountopeningrelativeUin=uin_number
                }
            }

            cCode.text= countryCode.text
            code.addTextChangedListener {
                bank_code=it.toString()
                bankCode.text=bank_code
            }
            iban.textInputEditText.addTextChangedListener {
                iban_number= it.toString()
                ibanCode.text=iban_number
            }

            btnContinue.setOnClickListener {
                if(mobile_number!="" && mobile_register!=""
                    && bank_code!="" && bank_name!=""
                    && iban_number!=""){
                    viewModel.accountOpening.accountopeningibanNumber = "${countryCode.text}|${bank_code}|${bank_name}|${iban_number}"
                    viewModel.saveaccountOpening()
                    (requireActivity() as AofActivity).loadFragment(AccountOpeningThreeFragment())

                }
            }

        }

        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(AccountOpeningOneFragment())
        }
        return binding.root
    }

    private fun populateDropdown() {
        binding.apply {
            textInputMobile.dropdown.apply {
                setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.RELATIVE_RELATION.map { it.first }))
                setOnItemClickListener { adapterView, view, i, l ->
                    if(i>0){
                        binding.cardRelationship.visibility = View.VISIBLE
                    }else{
                        binding.cardRelationship.visibility = View.GONE
                    }
                   mobile_register = AppConstants.RELATIVE_RELATION.get(i).second
                    viewModel.accountOpening.accountopeningrelationshipType = mobile_register
                }
            }

            textInputBank.dropdown.apply {
                setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.BANK_SWIFT_CODES.map { it.first }))
                setOnItemClickListener { adapterView, view, i, l ->

                    bank_name = AppConstants.BANK_SWIFT_CODES.get(i).second
                    bankSwift.text = bank_name
                }
            }


        }
    }

    private fun initFields(){
        val accountOpening=viewModel.accountOpening
        accountOpening.apply {
            if(accountopeningmobileNumber!="" && accountopeningmobileNumber!=null){
                mobile_number = accountopeningmobileNumber!!
                binding.mobileNumber.textInputEditText.setText(mobile_number)
            }

            if(accountopeningibanNumber!="" && accountopeningibanNumber!=null){
                val iban = accountopeningibanNumber!!.split("|")
                bank_code=iban[1]
                bank_name = iban[2]
                iban_number = iban[3]
                binding.apply {
                    code.setText(bank_code)
                    bankCode.setText(bank_code)
                    bankSwift.setText(bank_name)
                    ibanCode.setText(iban_number)
                    binding.iban.textInputEditText.setText(iban_number)

                    textInputBank.dropdown.setText(AppConstants.BANK_SWIFT_CODES.get(AppConstants.BANK_SWIFT_CODES.indexOfFirst { it.second.equals(bank_name) }).first,false)
                }
            }

            if(accountopeningrelationshipType!="" && accountopeningrelationshipType!=null){
                mobile_register = accountopeningrelationshipType!!
                binding.textInputMobile.dropdown.setText(AppConstants.RELATIVE_RELATION.get(AppConstants.RELATIVE_RELATION.indexOfFirst { it.second.equals(mobile_register) }).first)
                if(AppConstants.RELATIVE_RELATION.indexOfFirst { it.second.equals(mobile_register) }>0){
                    binding.cardRelationship.visibility = View.VISIBLE
                }else{
                    binding.cardRelationship.visibility = View.GONE
                }
            }

            if(accountopeningrelativeName!="" && accountopeningrelativeName!=null){
                relative_name=accountopeningrelativeName!!
                binding.relative.text1.setText(relative_name)
                binding.relative.visibility = View.VISIBLE
            }

            if(accountopeningrelativeUin!="" && accountopeningrelativeUin!=null){
                uin_number=accountopeningrelativeUin!!
                binding.relative.text2.setText(uin_number)
                binding.relative.visibility = View.VISIBLE
            }
        }
    }

}