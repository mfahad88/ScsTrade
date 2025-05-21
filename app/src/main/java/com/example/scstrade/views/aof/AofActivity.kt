package com.example.scstrade.views.aof

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityAofBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.aof.fragments.WelcomeFragment

class AofActivity : AppCompatActivity() {
    lateinit var binding:ActivityAofBinding
    lateinit var viewModel: AofViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAofBinding.inflate(LayoutInflater.from(this))
        viewModel =  ViewModelProvider.AndroidViewModelFactory.getInstance(this.application as MyApp).create(
            AofViewModel::class.java)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.country()
        viewModel.city()
        viewModel.mutableCounty.observe(this, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if(result.data?.statusCode==200){
                        val country= result.data?.data?.map { it.name to it.id }?.toList()
                        AppConstants.COUNTRY = country?: emptyList()
                    }
                }
            }
        })

        viewModel.mutableCity.observe(this, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if(result.data?.statusCode==200){
                        val city= result.data?.data?.map { it.name to it.id to it.provinceCode }?.toList()

                        AppConstants.CITY = city?: emptyList()
                    }
                }
            }
        })
        loadFragment(WelcomeFragment())
    }


    public fun loadFragment(fragment: Fragment, isBackStack:Boolean = false) {
        if(isBackStack){
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentContainer.id, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentContainer.id,fragment)
                .commit()
        }
    }
}