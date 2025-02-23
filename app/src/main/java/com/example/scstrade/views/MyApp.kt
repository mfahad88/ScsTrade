package com.example.scstrade.views

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.scstrade.viewmodels.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyApp : Application() {
    lateinit var viewModel: SharedViewModel
    override fun onCreate() {
        super.onCreate()
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(SharedViewModel::class.java)
        viewModel.apply {
            fetchIndices()
            fetchAllData()
        }
       /* CoroutineScope(Dispatchers.Main).launch {
            while (true){
                val syncResults = SyncManager(applicationContext).syncData()
                Log.e("Result-->",syncResults.toString())
                delay(5000)
            }
        }*/

    }
}