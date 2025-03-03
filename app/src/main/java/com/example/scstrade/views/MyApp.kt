package com.example.scstrade.views

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.main.MainActivity


class MyApp : Application() {
    lateinit var viewModel: SharedViewModel
    override fun onCreate() {
        super<Application>.onCreate()
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

}