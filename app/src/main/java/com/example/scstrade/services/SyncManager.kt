package com.example.scstrade.services

import android.content.Context
import android.util.Log
import com.example.scstrade.model.data.SyncStatus
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SyncManager(val context: Context) {
    private val apiService = RetrofitInstance.api
    private val db=AppDatabase.getDatabase(context)

    suspend fun syncData():Map<String,SyncStatus> = coroutineScope {
        val results = mutableMapOf<String,SyncStatus>()
        val jobs = listOf(
            async { results["Market"]=syncMarket() },
            async { results["Indices"]=syncIndices() }
        )

        jobs.awaitAll()
        return@coroutineScope results
    }

    private suspend fun syncMarket(): SyncStatus {
        try {
            val market = apiService.fetchAllData()
            if (market.isNotEmpty()) {
                db.marketDao().deleteMarket()
                db.marketDao().insertMarket(market)
                return SyncStatus(true,"Market Sync")
            }else{
                return SyncStatus(false,"No Data found")
            }
        } catch (e: Exception) {
            Log.e("SyncManager", "Failed to sync Market: ${e.message}")
            return SyncStatus(false, "Failed to sync Market: ${e.message}")
        }
    }

    private suspend fun syncIndices(): SyncStatus {
        try {
            val indices = apiService.getIndices()
            if (indices.isNotEmpty()) {
                db.marketDao().deleteIndices()
                db.marketDao().insertIndices(indices)
               return SyncStatus(true,"Indices Sync")
            }else{
                return SyncStatus(false,"No Data found")
            }
        } catch (e: Exception) {
            Log.e("SyncManager", "Failed to sync Indices: ${e.message}")
            return SyncStatus(false, "Failed to sync Indices: ${e.message}")
        }
    }
}