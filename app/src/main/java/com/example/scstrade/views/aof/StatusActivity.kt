package com.example.scstrade.views.aof
import androidx.compose.ui.res.dimensionResource

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityStatusBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.aof.fragments.kyc.basicData.KycBasicDataOneFragment

class StatusActivity : AppCompatActivity() {
    lateinit var viewModel: AofViewModel
    lateinit var binding: ActivityStatusBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatusBinding.inflate(LayoutInflater.from(this))
        viewModel =  ViewModelProvider.AndroidViewModelFactory.getInstance(this.application as MyApp).create(
            AofViewModel::class.java)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.apply {
            loader.visibility = View.GONE
            main.visibility = View.VISIBLE
        }

        viewModel.mutableLifeCycle.observe(this,Observer{result->

            when(result){
                70 -> {
                    // BASIC_DATA
                    binding.statusImageBasicData.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.current_group))
                }
                80 -> {
                    // CONTACT_DETAILS
                    binding.statusImageBasicData.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageContactDetail.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                }
                90 -> {
                    // ATTORNEY_DETAILS
                    binding.statusImageBasicData.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageContactDetail.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageAttorneyDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.current_group))
                }
                100 -> {
                    // NOMINEE_DETAILS

                    binding.statusImageBasicData.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageContactDetail.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageAttorneyDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageNomineeDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.current_group))
                }
                110 -> {
                    // OTHER_DETAILS
                    binding.statusImageBasicData.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageContactDetail.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageAttorneyDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageNomineeDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageOtherDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.current_group))
                }
                120 -> {
                    // DOCUMENTS
                    binding.statusImageBasicData.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageContactDetail.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageAttorneyDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageNomineeDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageOtherDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.current_group))
                }
                140 -> {
                    // COMPLETED
                    binding.statusImageBasicData.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageContactDetail.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageAttorneyDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageNomineeDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageOtherDetails.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                    binding.statusImageFormSubmitted.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.done_group))
                }
                else -> {
                    // unknown
                    binding.apply {
                        statusImageBasicData.setImageDrawable(ContextCompat.getDrawable(this@StatusActivity,R.drawable.undone_group))
                        statusImageContactDetail.setImageDrawable(ContextCompat.getDrawable(this@StatusActivity,R.drawable.undone_group))
                        statusImageAttorneyDetails.setImageDrawable(ContextCompat.getDrawable(this@StatusActivity,R.drawable.undone_group))
                        statusImageNomineeDetails.setImageDrawable(ContextCompat.getDrawable(this@StatusActivity,R.drawable.undone_group))
                        statusImageOtherDetails.setImageDrawable(ContextCompat.getDrawable(this@StatusActivity,R.drawable.undone_group))
                        statusImageFormSubmitted.setImageDrawable(ContextCompat.getDrawable(this@StatusActivity,R.drawable.undone_group))
                    }
                }


            }
        })

    }
}