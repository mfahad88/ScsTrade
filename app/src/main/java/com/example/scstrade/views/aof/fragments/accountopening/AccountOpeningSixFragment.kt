package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.databinding.FragmentAccountOpeningSixBinding
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.basicData.KycBasicDataOneFragment


class AccountOpeningSixFragment : Fragment() {
    lateinit var binding: FragmentAccountOpeningSixBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningSixBinding.inflate(inflater,container,false)
        binding.apply {
            btnContinue.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycBasicDataOneFragment())
            }
        }
        return binding.root
    }


}