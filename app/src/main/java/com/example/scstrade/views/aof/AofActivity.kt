package com.example.scstrade.views.aof

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityAofBinding
import com.example.scstrade.views.aof.fragments.accountopening.AccountOpeningFourFragment
import com.example.scstrade.views.aof.fragments.accountopening.AccountOpeningOneFragment
import com.example.scstrade.views.aof.fragments.accountopening.AccountOpeningThreeFragment
import com.example.scstrade.views.aof.fragments.accountopening.AccountOpeningTwoFragment
import com.example.scstrade.views.aof.fragments.kyc.KycEightFragment
import com.example.scstrade.views.aof.fragments.kyc.KycFourFragment
import com.example.scstrade.views.aof.fragments.kyc.KycOneFragment
import com.example.scstrade.views.aof.fragments.kyc.KycSevenFragment
import com.example.scstrade.views.aof.fragments.kyc.KycThreeFragment

class AofActivity : AppCompatActivity() {
    lateinit var binding:ActivityAofBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAofBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadFragment(KycEightFragment())
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