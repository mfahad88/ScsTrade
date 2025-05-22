package com.example.scstrade.views.aof.fragments.accountopening

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningOneBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.WelcomeFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

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
    var full_name=""
    var email_address=""
    var resident_status=""
    var issue_date=""
    var nic_type=""
    var nic_number=""
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningOneBinding.inflate(inflater,container,false)
        viewModel=(requireActivity() as AofActivity).viewModel


        binding.apply {
            fullName.textInputEditText.addTextChangedListener {
                full_name= it.toString()
                viewModel.accountOpening.accountopeningfullName = full_name
            }

            emailAddress.textInputEditText.addTextChangedListener {
                email_address=it.toString()
                viewModel.accountOpening.accountopeningemailAddress = email_address
            }

            nicCard.textInputEditText.addTextChangedListener {
                nic_number = it.toString()
                viewModel.accountOpening.accountopeningnicNumber = nic_number
            }

            nicIssueDate.textInputEditText.setOnFocusChangeListener { view, b ->
                if(b){
                    Utils.showDatePicker(requireContext()){ day, month, year ->
                        val customDate = LocalDate.of(year , month, day)
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val formatted = customDate.format(formatter)

                        binding.nicIssueDate.textInputEditText.setText(formatted)
                        issue_date = formatted
                        viewModel.accountOpening.accountopeningnicIssueDate =issue_date
                    }
                }
            }

            residentialStatus.apply {

                setOnButtonOneClickListener {
                    resident_status = AppConstants.RESIDENTIALSTATUS.get(0).second
                    viewModel.accountOpening.accountopeningresidentialStatus = resident_status
                }

                setOnButtonTwoClickListener {
                    resident_status = AppConstants.RESIDENTIALSTATUS.get(1).second
                    viewModel.accountOpening.accountopeningresidentialStatus = resident_status
                }
            }

            nicType.apply {
                setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.NIC_TYPE.map { it.first }))
                setOnItemClickListener { adapterView, view, i, l ->
                    nic_type = AppConstants.NIC_TYPE.get(i).second
                    viewModel.accountOpening.accountopeningnicType = nic_type
                }
            }

            btnContinue.setOnClickListener {
                if(full_name!="" && email_address!="" && resident_status!=""
                    && nic_type!="" && nic_number!="" && issue_date!=""){
                    viewModel.saveaccountOpening()
                    (requireActivity() as AofActivity).loadFragment(AccountOpeningTwoFragment())
                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }

            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(WelcomeFragment())
            }
        }

        initFields()




        return binding.root
    }

    private fun initFields() {
        val accountOpening= viewModel.getaccountOpening()
        accountOpening.apply {

            if(accountopeningfullName!="" && accountopeningfullName!=null){
                full_name=accountopeningfullName!!
                binding.fullName.textInputEditText.setText(full_name)
            }

            if(accountopeningemailAddress!="" &&  accountopeningemailAddress!=null){
                email_address=accountopeningemailAddress!!
                binding.emailAddress.textInputEditText.setText(email_address)
            }

            if(accountopeningnicNumber!="" && accountopeningnicNumber!=null){
                nic_number=accountopeningnicNumber!!

                binding.nicCard.textInputEditText.setText(nic_number)
            }

            if(accountopeningnicIssueDate!="" && accountopeningnicIssueDate!=null){
                issue_date=accountopeningnicIssueDate!!
                binding.nicIssueDate.textInputEditText.setText(issue_date)
            }

            if(accountopeningnicType!="" && accountopeningnicType!=null){
                nic_type=accountopeningnicType!!
                binding.nicType.setText(AppConstants.NIC_TYPE.get(AppConstants.NIC_TYPE.indexOfFirst { it.second.equals(nic_type) }).first,false)
            }


            if(accountopeningresidentialStatus!="" && accountopeningresidentialStatus!=null){
                resident_status = accountopeningresidentialStatus!!
                if(resident_status.equals("02")){
                    binding.residentialStatus.toggleSelection(false)
                }else{
                    binding.residentialStatus.toggleSelection(true)
                }
            }
        }
    }


}