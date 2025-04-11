package com.example.scstrade.views.main

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityMainBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.AppDatabase
import com.example.scstrade.services.RetrofitInstance
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.splash.SplashFragment
import com.example.scstrade.views.watchlist.WatchlistFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: SharedViewModel
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(LayoutInflater.from(this))
        viewModel = (application as MyApp).viewModel
        setContentView(binding.root)
        Utils.setSystemBarIcons(this,darkIcons = false)
        val snackbar =  Utils.showInternetError(binding.main,"You are offline. Please check your internet connection.",Snackbar.LENGTH_INDEFINITE)
        viewModel.isConnected.observe(this, Observer {

            if(!it){
                snackbar.show()
            }else{
                snackbar.dismiss()
            }
        })


            loadFragment(SplashFragment())

    }
    public fun loadFragment(fragment: Fragment,isBackStack:Boolean = false) {
        if(isBackStack){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
//                .commit()
        }else{
            supportFragmentManager
               .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss()
//                .commit()
        }
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
//        viewModel.stopIndices()
    }



}