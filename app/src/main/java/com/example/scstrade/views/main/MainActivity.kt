package com.example.scstrade.views.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityMainBinding
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.AppDatabase
import com.example.scstrade.services.RetrofitInstance
import com.example.scstrade.services.SyncManager
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.splash.SplashFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: SharedViewModel
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(LayoutInflater.from(this))


        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            v.updateLayoutParams<MarginLayoutParams> {
                leftMargin = systemBars.left
                rightMargin = systemBars.right
                bottomMargin = systemBars.bottom
            }
            WindowInsetsCompat.CONSUMED
        }

            loadFragment(SplashFragment())
//        viewModel.fetchIndices()
//        viewModel.fetchAllData()
    }
    public fun loadFragment(fragment: Fragment,isBackStack:Boolean = false) {
        if(isBackStack){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
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