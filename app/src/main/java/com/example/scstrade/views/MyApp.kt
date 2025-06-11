package com.example.scstrade.views

import android.app.Activity
import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.CertificateHelper
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.services.RetrofitInstance
import com.example.scstrade.services.RetrofitInstanceAof
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.main.MainActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MyApp : Application() {
    lateinit var viewModel: SharedViewModel
    lateinit var login: LoginDataItem
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        getSha1Fingerprint()
        FirebaseApp.initializeApp(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        if(!Utils.getSharedPreference(this, AppConstants.LIGHT_MODE)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        FirebaseApp.initializeApp(this)
        RetrofitInstanceAof.init(this)
        RetrofitInstance.init(this)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(SharedViewModel::class.java)
        viewModel.apply {
            fetchIndices()
            fetchAllData()
        }

        CertificateHelper.printSHA1Fingerprint(this)
        registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {

            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityResumed(p0: Activity) {
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {
                if(p0 is MainActivity) {
                    Log.e("Stop", "Done")
                }
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
                if(p0 is MainActivity) {
                    viewModel.stopAll()
                }
            }

        })
    }
    fun getSha1Fingerprint() {
        try {
            val packageInfo: PackageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in packageInfo.signatures!!) {
                val md: MessageDigest = MessageDigest.getInstance("SHA-1")
                md.update(signature.toByteArray())
                val sha1Fingerprint = md.digest().joinToString(":") { "%02X".format(it) }
                Log.d("SHA-1 Fingerprint", sha1Fingerprint)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }


   /* override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val config = Configuration(newConfig)
        config.fontScale = 0.5f
        resources.updateConfiguration(config, Resources.getSystem().getDisplayMetrics())
    }*/

}