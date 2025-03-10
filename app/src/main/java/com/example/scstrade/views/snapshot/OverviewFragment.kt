package com.example.scstrade.views.snapshot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivitySnapshotBinding
import com.example.scstrade.databinding.FragmentOverviewBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OverviewFragment : Fragment() {
    lateinit var binding: FragmentOverviewBinding
    lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOverviewBinding.inflate(inflater,container,false)

        sharedViewModel= (requireActivity().application as MyApp).viewModel
        sharedViewModel.mutableOverview.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    sharedViewModel.mutableAllData.observe(viewLifecycleOwner, Observer {res->
                        binding.dayRange.setLow(result.data?.oneMonthLow?.toFloat()?:0f,result.data?.oneMonthHigh?.toFloat()?:0f,res.data?.filter { it.sYM.equals(requireActivity().intent.extras?.getString(AppConstants.SYMBOL),true) }?.first()?.cL?.toFloat()?:0f)
                    })
                }
            }
        })

        return binding.root
    }


}