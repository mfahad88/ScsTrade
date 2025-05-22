package com.example.scstrade.views.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityNotificaionBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.NotificationViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp

class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificaionBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var notificationViewModel: NotificationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificaionBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.toolbar.binding.apply {
            toolbarWithBack.visibility = View.VISIBLE
            toolbarWithLogo.visibility = View.GONE
            backButton.visibility = View.GONE
            titleItem.text = "Notification"
            searchIcon.visibility = View.VISIBLE
            notificationIcon.visibility = View.INVISIBLE
        }
        sharedViewModel = (this.application as MyApp).viewModel
        notificationViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application as MyApp).create(NotificationViewModel::class.java)
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar.binding.customToolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            v.setPadding(systemBars.left,  systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sharedViewModel.notification()

        binding.apply {

            notificationList.apply {
                layoutManager=LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
                val divider = DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
                ContextCompat.getDrawable(context, R.drawable.custom_divider)?.let {
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
            binding.notificationList.adapter=NotificationAdapter(it){notificationEntity,index ->
                notificationViewModel.markAsRead(notificationEntity.id)
                notificationViewModel.getNotification()
            }


        })

    }

}