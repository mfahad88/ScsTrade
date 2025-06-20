package com.example.scstrade.views.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.scstrade.R
import com.example.scstrade.helper.Utils
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry

class CustomCandlestickChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CandleStickChart(context, attrs) {

    init {
        setupChart()
    }

  /*  override fun onTouchEvent(event: MotionEvent?): Boolean {
        parent?.requestDisallowInterceptTouchEvent(true)
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        parent?.requestDisallowInterceptTouchEvent(true)
        return super.onInterceptTouchEvent(ev)
    }*/
    private fun setupChart() {
        this.description.isEnabled = false
        this.setBackgroundColor(Color.WHITE)
        this.setTouchEnabled(true)
        this.setPinchZoom(true)
        this.isDragEnabled=true
        this.background=(AppCompatResources.getDrawable(context,R.drawable.stock_card))

        // X Axis Customization
        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
        this.xAxis.textColor = ContextCompat.getColor(context,R.color.black)
        this.xAxis.setDrawGridLines(false)
        this.xAxis.setAvoidFirstLastClipping(true)
        this.xAxis.setDrawLabels(false)
//        this.xAxis.axisMinimum=10f
//        this.xAxis.axisMaximum=200f

        // Left Y Axis
        this.axisLeft.isEnabled = false
        // Right Y Axis
        this.axisRight.setDrawGridLines(true)
        this.axisRight.setDrawAxisLine(true)
        this.axisRight.isEnabled = true
        this.axisRight.textColor = ContextCompat.getColor(context,R.color.black)
        this.axisRight.textSize=8F
        // Disable Legend
        this.legend.isEnabled = false
    }

    fun setCandleData(dataList: List<CandleEntry>) {
        val isDark = Utils.isDarkMode(context)
        val candleDataSet = CandleDataSet(dataList, "Candlestick Data").apply {

            color = Color.rgb(80, 80, 80)
            valueTextColor = if(isDark) Color.WHITE else Color.BLACK
            shadowColor = Color.DKGRAY
            shadowWidth = 0.7f
            decreasingColor = ContextCompat.getColor(context,R.color.md_theme_error)
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = ContextCompat.getColor(context,R.color.md_theme_primary)
            increasingPaintStyle = Paint.Style.FILL
            neutralColor = Color.BLUE
            setDrawValues(false)
        }
        this.data = CandleData(candleDataSet)
        this.setTouchEnabled(true)
        this.isDragEnabled=true
        this.setScaleEnabled(false)
        this.setPinchZoom(false)
        this.setDoubleTapToZoomEnabled(false)

// Optional: Disable Y-axis scaling independently
        this.isScaleYEnabled = false
        this.isScaleXEnabled = false
        this.isHighlightPerTapEnabled = false
        this.isHighlightPerDragEnabled = false

// Remove marker (popup with value info)
        this.marker = null
//        this.setVisibleXRange(10f,30f)
        setupChart()
        this.invalidate()
    }
}
