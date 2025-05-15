package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningFiveBinding
import com.example.scstrade.views.aof.AofActivity


class AccountOpeningFiveFragment : Fragment() {
    lateinit var binding: FragmentAccountOpeningFiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningFiveBinding.inflate(inflater,container,false)
        binding.apply {
            btnContinue.setOnClickListener {
                if(binding.pinview.value.isNotEmpty()){
                    (requireActivity() as AofActivity).loadFragment(AccountOpeningSixFragment())
                }
            }
        }
        return binding.root
    }

}