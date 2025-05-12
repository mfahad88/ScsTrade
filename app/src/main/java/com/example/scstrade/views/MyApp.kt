package com.example.scstrade.views

import android.app.Activity
import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.main.MainActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    lateinit var viewModel: SharedViewModel
    lateinit var login: LoginDataItem
    override fun onCreate() {
        super<Application>.onCreate()
        getSha1Fingerprint()
        FirebaseApp.initializeApp(this)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(SharedViewModel::class.java)
        viewModel.apply {
            fetchIndices()
            fetchAllData()
        }


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
}