package com.example.scstrade.views.aof.fragments.kyc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycThirteenBinding
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity


class KycThirteenFragment : Fragment() {
    lateinit var binding:FragmentKycThirteenBinding
    lateinit var viewModel: AofViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycThirteenBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        return binding.root
    }


}