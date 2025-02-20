package com.example.scstrade.model.response.chart


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.scstrade.model.summary.KSEIndices
import com.google.gson.annotations.SerializedName
@Entity(
    tableName = "chart_items",
    foreignKeys = [ForeignKey(
        entity = KSEIndices::class,
        parentColumns = ["id"],
        childColumns = ["kseIndexId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ChartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val kseIndexId: Int,
    @SerializedName("trading_close")
    val tradingClose: Double,
    @SerializedName("trading_date")
    val tradingDate: String,
    @SerializedName("trading_high")
    val tradingHigh: Double,
    @SerializedName("trading_low")
    val tradingLow: Double,
    @SerializedName("trading_open")
    val tradingOpen: Double,
    @SerializedName("trading_vol")
    val tradingVol: Double
)