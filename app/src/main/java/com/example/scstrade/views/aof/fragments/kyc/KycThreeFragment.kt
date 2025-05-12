package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycThreeBinding
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

    lateinit var binding:FragmentKycThreeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycThreeBinding.inflate(inflater,container,false)


        binding.placeBirth.apply {

            setList1(listOf("A","B","C"))
            setList2(listOf("X","Y","Z"))
            autoCompleteTextView1.setOnItemClickListener { adapterView, view, i, l ->
                Toast.makeText(requireContext(),adapterView.getItemAtPosition(i) as String,Toast.LENGTH_SHORT).show()
            }
            autoCompleteTextView2.setOnItemClickListener { adapterView, view, i, l ->
                Toast.makeText(requireContext(),adapterView.getItemAtPosition(i) as String,Toast.LENGTH_SHORT).show()
            }
        }
        binding.ivrService.apply {
            setOnButtonOneClickListener {
                Toast.makeText(requireContext(),"Button1",Toast.LENGTH_SHORT).show()
            }
            setOnButtonTwoClickListener {
                Toast.makeText(requireContext(),"Button2",Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}