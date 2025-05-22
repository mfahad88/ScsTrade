package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.data.NotificationEntity
import com.example.scstrade.model.response.notification.NotificationDto
import com.example.scstrade.services.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel(application: Application): AndroidViewModel(application) {
    val db = AppDatabase.getDatabase(application)
    val mutableNotification=MutableLiveData<List<NotificationEntity>>()
    fun getNotification() {
        viewModelScope.launch (Dispatchers.IO){
            val result=db.notificationDao().getAllNotifications()
            withContext(Dispatchers.Main){
                mutableNotification.value=result
            }
        }

    }

    fun insertNotification(notifications:List<NotificationDto>){
        viewModelScope.launch {
            db.notificationDao().insertAll(
                notifications.map { NotificationEntity(it.mainAnnID,"${it.mainAnnHeading} - ${it.announcementTypeName}",it.mainAnnDetails,it.mainAnnDate,false) }.toList()
            )
        }
    }

    fun markAsRead(id: Int){
      viewModelScope.launch {
          db.notificationDao().markAsRead(id)
      }
    }
}