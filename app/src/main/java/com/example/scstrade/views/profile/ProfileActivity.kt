package com.example.scstrade.views.profile

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isNotEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityProfileBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileActivity : BaseActivity() {
    lateinit var binding:ActivityProfileBinding
    lateinit var login:LoginDataItem
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityProfileBinding.inflate(LayoutInflater.from(this))
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)
        sharedViewModel = (this.application as MyApp).viewModel

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        fetchUser()

        binding.profileEmail.text=login.registrationEmail
        binding.profileName.text=login.registrationName
        binding.fullName.text = login.registrationName
        binding.mobileNumber.text = login.registrationPhone
        binding.email.text = login.registrationEmail

        binding.apply {
            buttonSave.setOnClickListener {
                if(fullName.text.isNotEmpty() && mobileNumber.isNotEmpty() && email.isNotEmpty()){
                    sharedViewModel.updateProfile(
                        name = fullName.text.toString(),
                        email = email.text.toString(),
                        password = login.registrationPassword.toString(),
                        phone = mobileNumber.text.toString(),
                        id = login.registrationID?:-1
                    )
                }else{
                    Utils.showError(binding.root,"Please provide valid inputs...")
                }
            }
        }

        sharedViewModel.mutableUpdateProfile.observe(this,Observer { result->
            when(result){
                is Resource.Error ->{
                    Utils.showError(binding.root,result.message?:"An error occurred")
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    lifecycleScope.launch {
                        Utils.showSuccess(binding.root,"Successfully updated...")
                        delay(2000)
                        sharedViewModel.mutableUpdateProfile.value = null
                        Utils.saveSharedPreference(
                            this@ProfileActivity,
                            AppConstants.USER,
                            result.data?: emptyList()
                        )

                        finish()

                    }

                }
            }
        })

//        sharedViewModel.mutableUpdateProfile.removeObserver(observer)

    }

    private fun fetchUser() {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(this@ProfileActivity, emptyList<LoginDataItem>(),
            AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }


    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration(res.configuration)
        config.fontScale = 1.0f // Set font scale to default (no scaling)
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }


    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            // Override any incoming configuration changes
            overrideConfiguration.densityDpi = resources.displayMetrics.densityDpi
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}