package com.example.scstrade.views.main

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityMainBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.helper.Utils.Companion.getScreenWidthInPx
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
    var screenSize:Double?=null
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
        screenSize = Utils.getScreenSizeInInches(this)
        Log.d("ScreenSize", "Screen size in inches: $screenSize")
        Log.e("Screen Pixel", Utils.getScreenWidthInPx(this).toString())
        Log.e("Screen Smallest Width", Utils.getSmallestWidthDp(this).toString())
        Utils.setEdgeToEdgeWithWhiteIcons(this)
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

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView.rootView) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            insets
        }
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
                .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
//                .commit()
        }else{
            supportFragmentManager
               .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
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

        val metrics = res.displayMetrics

        // Calculate screen width and height in inches
        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())
//        Log.e("Screen Size in Inches: ",diagonalInches.toString())
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


}