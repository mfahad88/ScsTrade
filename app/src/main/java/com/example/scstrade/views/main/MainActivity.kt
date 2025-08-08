package com.example.scstrade.views.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityMainBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.login.LoginFragment
import com.example.scstrade.views.notification.NotificationDetailActivity
import com.example.scstrade.views.splash.SplashFragment

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: ActivityMainBinding
    private var screenSize: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        if(intent.getBooleanExtra(AppConstants.IS_NOTIFY,false)){
            val intentNotify = Intent(this,NotificationDetailActivity::class.java)
            intentNotify.putExtra(AppConstants.ID_REF,intent.getIntExtra(AppConstants.ID_REF,-1))
            intentNotify.putExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME,intent.getStringExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME))
            startActivity(intentNotify)
        }
    /*    // StrictMode for dev debugging
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )*/

        // Facebook + FCM setup
  /*      FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)*/

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "Token: $token")
            } else {
                Log.w("FCM", "Fetching FCM token failed", task.exception)
            }
        }

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        viewModel = (application as MyApp).viewModel
        setContentView(binding.root)

        screenSize = Utils.getScreenSizeInInches(this)
        Log.d("ScreenSize", "Screen size in inches: $screenSize")
        Log.e("Screen Pixel", Utils.getScreenWidthInPx(this).toString())
        Log.e("Screen Smallest Width", Utils.getSmallestWidthDp(this).toString())
        Utils.setEdgeToEdgeWithWhiteIcons(this)

        val snackbar = Utils.showInternetError(
            binding.main,
            "You are offline. Please check your internet connection.",
            Snackbar.LENGTH_INDEFINITE
        )
        viewModel.isConnected.observe(this) {
            if (!it) snackbar.show() else snackbar.dismiss()
        }

        // Request notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        // 🔄 FIXED: SharedPreferences access off main thread
        if (viewModel.isHome) {
            lifecycleScope.launch(Dispatchers.IO) {
                val userType = object : TypeToken<List<LoginDataItem>>() {}
                val user = Utils.getSharedPreference(
                    this@MainActivity,
                    emptyList(),
                    AppConstants.USER,
                    userType
                )
                val isRemembered = Utils.getSharedPreference(
                    this@MainActivity,
                    listOf(false),
                    AppConstants.IS_REMEMBER,
                    object : TypeToken<List<Boolean>>() {}
                ).first()

                withContext(Dispatchers.Main) {
                    if (user.isEmpty() && !isRemembered) {
                        loadFragment(LoginFragment())
                    } else {
                        loadFragment(LandingFragment())
                    }
                }
            }
        } else {
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

    fun loadFragment(fragment: Fragment, isBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            .replace(R.id.fragment_container, fragment)

        if (isBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commitAllowingStateLoss()
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.densityDpi = resources.displayMetrics.densityDpi
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration(res.configuration)
        val metrics = res.displayMetrics

        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

        config.fontScale = when {
            diagonalInches in 3.9..4.9 -> 0.85f
            diagonalInches in 4.9..5.4 -> 0.95f
            diagonalInches in 5.5..6.9 -> 1.0f
            else -> 1.2f
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment = 0
        }

        res.updateConfiguration(config, metrics)
        return res
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }
}
