package com.example.scstrade.views.main

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityMainBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.login.LoginFragment
import com.example.scstrade.views.splash.SplashFragment
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.reflect.TypeToken


class MainActivity : BaseActivity() {
    private lateinit var viewModel: SharedViewModel
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    /*    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
            setTheme(R.style.AppTheme)
        }else{
            setTheme(R.style.DarkTheme)
        }*/
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(this)
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Log.d("FCM", "Token: $token")
                // TODO: Send token to your backend server here
            }
        AppEventsLogger.activateApp(this)
        binding=ActivityMainBinding.inflate(LayoutInflater.from(this))
        viewModel = (application as MyApp).viewModel
        setContentView(binding.root)
//        Utils.setSystemBarIcons(this,darkIcons = fa)
        val snackbar =  Utils.showInternetError(binding.main,"You are offline. Please check your internet connection.",Snackbar.LENGTH_INDEFINITE)
        viewModel.isConnected.observe(this, Observer {

            if(!it){
                snackbar.show()
            }else{
                snackbar.dismiss()
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
        if(viewModel.isHome){
            val listType = object : TypeToken<List<LoginDataItem>>() {}
            val user= Utils.getSharedPreference(this, emptyList<LoginDataItem>(),AppConstants.USER,listType)
           val isRemember= Utils.getSharedPreference(this, listOf(false),AppConstants.IS_REMEMBER, object : TypeToken<List<Boolean>>() {}).first()
            if(user.isEmpty() && !isRemember){
                loadFragment(LoginFragment())
            }else {
                loadFragment(LandingFragment())
            }
        }else {
            loadFragment(SplashFragment())
        }

        subscribeToTopic("all")
    }


    private fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    Log.d("FCM", "Subscribed to topic: $topic")
                } else {
                    Log.e("FCM", "Subscription failed", task.exception)
                }
            }
    }
    public fun loadFragment(fragment: Fragment,isBackStack:Boolean = false) {
        if(isBackStack){
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
//                .commit()
        }else{
            supportFragmentManager
               .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss()
//                .commit()
        }
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            // Override any incoming configuration changes
            overrideConfiguration.densityDpi = resources.displayMetrics.densityDpi
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration(res.configuration)
        config.fontScale = 1.0f // Set font scale to default (no scaling)
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }


}