package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningTwoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AccountOpeningTwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountOpeningTwoFragment : Fragment() {
    lateinit var binding: FragmentAccountOpeningTwoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningTwoBinding.inflate(inflater,container,false)

        return binding.root
    }


}