package com.example.scstrade.helper

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar

class ConnectivityObserver(private val context: Context) : LiveData<Boolean>() {

    private val connectivityManager = 
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)  // Internet is back
        }

        override fun onLost(network: Network) {
            postValue(false) // Internet lost
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActive() {
        super.onActive()
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        postValue(capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
