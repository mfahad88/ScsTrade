package com.example.scstrade.views.splash

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentSplashBinding


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
        Handler().postDelayed(Runnable {
            findNavController().navigate(R.id.action_splashFragment_to_registerFragment)
        },5000)
        return  binding.root
    }

}