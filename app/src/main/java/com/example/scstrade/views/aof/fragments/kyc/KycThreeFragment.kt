package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycThreeBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.widgets.DualDropdownSelectorView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [KycThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KycThreeFragment : Fragment() {
    lateinit var viewModel: AofViewModel
    lateinit var binding:FragmentKycThreeBinding
    var country:String?=null
    var city:String?=null
    var ivrStatus:String?=null
    var nicType:String?=null
    var nicExpiry:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycThreeBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        populateDropdown()

        binding.cardNic.apply {
            setOnButtonOneClickListener {
                nicType=AppConstants.LIFETIMECNICSTATUS.get(1).values.first()
                editText.isEnabled=true
            }
            setOnButtonTwoClickListener {
                nicType=AppConstants.LIFETIMECNICSTATUS.get(0).values.first()
                editText.setText("")
                editText.isEnabled=false
            }
           editText.setOnFocusChangeListener { view, b ->
               if(b){
                   Utils.showDatePicker(requireContext()){ day, month, year ->
                       val selectedDate = "$day-$month-$year"
                       editText.setText(selectedDate)
                       nicExpiry = selectedDate
                   }
               }
           }
        }
        binding.placeBirth.autoCompleteTextView1.setOnItemClickListener { adapterView, view, i, l ->
            country=AppConstants.COUNTRY.get(i).second
        }
        binding.placeBirth.autoCompleteTextView2.setOnItemClickListener { adapterView, view, i, l ->
            city=AppConstants.CITY.get(i).second
        }


        binding.ivrService.apply {
            setOnButtonOneClickListener {
                ivrStatus = AppConstants.IVRSTATUS.get(1).values.first()
            }
            setOnButtonTwoClickListener {
                ivrStatus = AppConstants.IVRSTATUS.get(0).values.first()
            }
        }
        binding.apply {
            btnContinue.setOnClickListener {
                if(country?.isNotEmpty()?:false && city?.isNotEmpty()?:false && ivrStatus?.isNotEmpty()?:false){
                    viewModel.basicData.pobCountry=country
                    viewModel.basicData.pobCity=city
                    viewModel.basicData.ivrService=ivrStatus
                    viewModel.basicData.nicType = nicType
                    viewModel.basicData.nicValid = nicExpiry
                    viewModel.saveBasicData()
                    (requireActivity() as AofActivity).loadFragment(KycFourFragment())
                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycTwoFragment())
            }
        }
        return binding.root
    }

    private fun initFields() {
        val basicData = viewModel.getbasicData()

        nicExpiry=basicData.nicValid
        nicType=basicData.nicType
        country=basicData.pobCountry
        city=basicData.pobCity
        ivrStatus = basicData.ivrService

        binding.apply {
            cardNic.toggleSelection(if(nicType.equals("y",true)) false else true)
            cardNic.editText.setText(nicExpiry)
            placeBirth.autoCompleteTextView1.setText(country)
            placeBirth.autoCompleteTextView2.setText(city)
            ivrService.toggleSelection(if(ivrStatus.equals("y",true)) false else true)
        }
    }

    private fun populateDropdown() {
        binding.placeBirth.autoCompleteTextView1.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.COUNTRY.map { it.first }))
        binding.placeBirth.autoCompleteTextView2.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.CITY.map { it.first }))

    }

}