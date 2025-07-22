package com.example.scstrade.views.aof
import androidx.compose.ui.res.dimensionResource

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
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.util.Log
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.aof.fragments.accountopening.AccountOpeningFiveFragment

class AofActivity : BaseActivity() {
    lateinit var binding:ActivityAofBinding
    lateinit var viewModel: AofViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAofBinding.inflate(LayoutInflater.from(this))
        viewModel =  ViewModelProvider.AndroidViewModelFactory.getInstance(this.application as MyApp).create(
            AofViewModel::class.java)
        binding.toolbar.binding.market.text= getString(R.string.new_account_opening)

        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.country()
        viewModel.city()
        binding.imageView22.setOnClickListener {
//            startActivity(Intent(this,StatusActivity::class.java))
        }
        viewModel.mutableCounty.observe(this, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if(result.data?.statusCode==200){
                        val country= result.data?.data?.map { it.id to it.name  }?.toList()
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
                        val city= result.data?.data?.map { it.id to it.name  to it.provinceCode }?.toList()

                        AppConstants.CITY = city?: emptyList()
                    }
                }
            }
        })
        loadFragment(WelcomeFragment())

        val data: Uri? = intent?.data
        val token = data?.getQueryParameter("token")

        if (token != null) {

            Log.d("VerifyEmail", "Token: $token")
            loadFragment(AccountOpeningFiveFragment())
        } else {
            Log.e("VerifyEmail", "No token found")
        }
    }


    public fun loadFragment(fragment: Fragment, isBackStack:Boolean = false) {
        if(isBackStack){
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(binding.fragmentContainer.id, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(binding.fragmentContainer.id,fragment)
                .commit()
        }
    }



    override fun getResources(): Resources {

        val res = super.getResources()
        val config = Configuration(res.configuration)

        val metrics = res.displayMetrics

        // Calculate screen width and height in inches
        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

        // Set fontScale based on diagonal screen size
        if(diagonalInches>3.9 && diagonalInches<4.9){
            config.fontScale = 0.85f  // Small phones
        }else if (diagonalInches>4.9 && diagonalInches<5.4){
            config.fontScale = 0.95f
        }else if (diagonalInches>5.5 && diagonalInches<6.9){
            config.fontScale = 1.0f
        }else{
            config.fontScale = 1.2f
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment = 0

        }
        res.updateConfiguration(config, metrics)
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