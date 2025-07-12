package com.example.scstrade.views.aof.fragments.kyc
import androidx.compose.ui.res.dimensionResource

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentCongratulationsBinding
import com.example.scstrade.views.aof.AofActivity



class CongratulationsFragment : Fragment() {
    lateinit var binding:FragmentCongratulationsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCongratulationsBinding.inflate(inflater,container,false)
        (requireActivity() as AofActivity).binding.apply {
            progressBar.setProgress(6)
            steps.text="6/6"
        }
        binding.btnDashboard.setOnClickListener {
            (requireActivity() as AofActivity).finish()
        }
        return binding.root
    }


}