package com.example.scstrade.views.splash

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentSplashBinding
import com.example.scstrade.views.login.LoginFragment
import com.example.scstrade.views.main.MainActivity
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
            (requireActivity() as MainActivity).loadFragment(LoginFragment())
//            findNavController().navigate(R.id.action_splashFragment_to_loginFragment,null,)
        }

        return  binding.root
    }

}