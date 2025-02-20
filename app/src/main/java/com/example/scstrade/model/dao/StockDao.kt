package com.example.scstrade.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.summary.KSEIndices

@Dao
interface StockDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertMarket(stockItem: StockItem)

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertMarket(stockItems:List<StockItem>)

    @Query("SELECT * FROM stock_item")
    fun getMarkets():List<StockItem>

    @Query("SELECT * FROM stock_item where LOWER(sYM) IN (:symbols)")
    fun getMarkets(symbols:List<String>):List<StockItem>

    @Query("Delete from stock_item")
    suspend fun deleteMarket()


    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertIndices(kseIndices: KSEIndices):Long

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertIndices(kseIndices:List<KSEIndices>)

    @Query("SELECT * FROM kse_indices")
    fun getIndices():List<KSEIndices>

    @Query("Delete from kse_indices")
    suspend fun deleteIndices()

}