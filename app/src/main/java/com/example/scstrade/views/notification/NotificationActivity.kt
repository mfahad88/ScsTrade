package com.example.scstrade.views.notification
import androidx.compose.ui.res.dimensionResource

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityNotificaionBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.NotificationViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp

class NotificationActivity : BaseActivity() {
    lateinit var binding: ActivityNotificaionBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var notificationViewModel: NotificationViewModel

    override fun onResume() {

        super.onResume()
        sharedViewModel.notification()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificaionBinding.inflate(LayoutInflater.from(this))
        sharedViewModel = (this.application as MyApp).viewModel
        setContentView(binding.root)
        // binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = "Notification"

        notificationViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application as MyApp).create(NotificationViewModel::class.java)


        binding.apply {

            notificationList.apply {
                layoutManager=LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
                val divider = DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
                AppCompatResources.getDrawable(context, R.drawable.custom_divider)?.let {
                    divider.setDrawable(it)
                }
                addItemDecoration(divider)
            }
        }

        sharedViewModel.mutableNotificationList.observe(this, Observer { result->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root,result.message?:"An error occurred...")
                }
                is Resource.Loading -> {
                    binding.apply {
                        notificationList.visibility = View.GONE
                        loader.visibility = View.VISIBLE
                    }
                }
                is Resource.Success -> {
                    binding.apply {
                        notificationList.visibility = View.VISIBLE
                        loader.visibility = View.GONE
                        notificationViewModel.insertNotification(result.data?: emptyList())
                        notificationViewModel.getNotification()
                    }
                }
            }
        })

        notificationViewModel.mutableNotification.observe(this, Observer {
            binding.notificationList.adapter=NotificationAdapter(it,{
                val intent= Intent(this,NotificationDetailActivity::class.java)
                intent.putExtra(AppConstants.ID_REF,it.MainAnnIDRef)
                intent.putExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME,it.AnnouncementTypeName)
                startActivity(intent)

            },{ notificationEntity,index ->

                notificationViewModel.markAsRead(notificationEntity.id)
                notificationViewModel.getNotification()
            })


        })

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