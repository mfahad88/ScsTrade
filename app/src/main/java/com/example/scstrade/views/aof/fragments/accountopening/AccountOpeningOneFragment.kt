package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningOneBinding
import com.example.scstrade.helper.AppConstants

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningOneBinding.inflate(inflater,container,false)

        binding.nicType.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,AppConstants.NIC_TYPE.map { it.keys.toString().replace("[","").replace("]","") }))
        binding.nicType.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(requireContext(),AppConstants.NIC_TYPE.get(i).values.first(),Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }


}