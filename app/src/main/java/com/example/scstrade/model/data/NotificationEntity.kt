package com.example.scstrade.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: Int,
    val title: String?,
    val message: String?,
    val timestamp: String?,
    val MainAnnIDRef:Int?,
    val AnnouncementTypeName:String?,
    val isRead: Boolean = false
)
