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
    var resident=""
    var issue_date=""
    var nType=""
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningOneBinding.inflate(inflater,container,false)
        viewModel=(requireActivity() as AofActivity).viewModel

        initFields()
        binding.nicType.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.NIC_TYPE.map { it.first }))
        binding.nicType.setOnItemClickListener { adapterView, view, i, l ->
            nType = AppConstants.NIC_TYPE.get(i).second
        }
        binding.nicIssueDate.textInputEditText.setOnFocusChangeListener { view, b ->
            if(b){
                Utils.showDatePicker(requireContext()){ day, month, year ->
                    val customDate = LocalDate.of(year , month, day)
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val formatted = customDate.format(formatter)
                    /*val date = Date(year, month, day)
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val selectedDate =sdf.format(date)*/
                    binding.nicIssueDate.textInputEditText.setText(formatted)
                    issue_date = formatted
                }
            }
        }
        binding.residentialStatus.apply {
            setOnButtonOneClickListener {

                resident= AppConstants.RESIDENTIALSTATUS[0].second
            }
            setOnButtonTwoClickListener {
                resident= AppConstants.RESIDENTIALSTATUS[1].second
            }
        }

        binding.btnContinue.setOnClickListener {
            val fname=binding.fullName.text
            val email=binding.emailAddress.text
//            val nType=AppConstants.NIC_TYPE.firstNotNullOfOrNull { binding.nicType.text.toString() }
            val nicNumber=binding.nicCard.text

            if(fname.isNotEmpty() && email.isNotEmpty() && nType.isNotEmpty() && nicNumber.isNotEmpty() && issue_date!=""){
                viewModel.saveSelfInfo(fname,email,resident,nType,nicNumber,issue_date)
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
            if(viewModel.getSelfInfo().nicType!="" && viewModel.getSelfInfo().nicType!=null ) {
//                val nic=AppConstants.NIC_TYPE.filter { it.second.equals(nicType) }.map { it.first }

                nicType.setText(AppConstants.NIC_TYPE.find { it.second.equals(viewModel.getSelfInfo().nicType) }?.first)
            }
            nicCard.text=viewModel.getSelfInfo().nicNumber

            issue_date=viewModel.getSelfInfo().nicIssueDate.toString()

            if(issue_date!="" && issue_date!=null){
                binding.nicIssueDate.textInputEditText.setText(issue_date)
            }
        }
    }


}