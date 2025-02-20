package com.example.scstrade.views

import android.app.Application
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.scstrade.services.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Main).launch {
            while (true){
                val syncResults = SyncManager(applicationContext).syncData()
                Log.e("Result-->",syncResults.toString())
                delay(5000)
            }
        }

    }
}