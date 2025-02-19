package com.example.scstrade.views.splash

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentSplashBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.login.LoginFragment
import com.example.scstrade.views.main.MainActivity
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding=FragmentSplashBinding.inflate(inflater,container,false)
        lifecycleScope.launch {
            delay(5000)

            if(Utils.getSharedPreference(requireContext(), listOf(false),AppConstants.IS_REMEMBER, object : TypeToken<List<Boolean>>() {}).first()){
                (requireActivity() as MainActivity).loadFragment(LandingFragment())
            }else{
                (requireActivity() as MainActivity).loadFragment(LoginFragment())
            }

        }

        return  binding.root
    }

}