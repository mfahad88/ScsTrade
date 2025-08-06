package com.example.scstrade.views.aof.fragments
import androidx.compose.ui.res.dimensionResource

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentWelcomeBinding
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.accountopening.AccountOpeningOneFragment


class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    private lateinit var viewModel: AofViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentWelcomeBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        viewModel.mutableLifeCycle.value = 0
        binding.signup.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(fragment = AccountOpeningOneFragment())
        }
        binding.login.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(fragment = LoginAOFFragment())
        }
        return binding.root
    }


}