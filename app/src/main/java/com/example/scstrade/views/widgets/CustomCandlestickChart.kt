package com.example.scstrade.views.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        parent?.requestDisallowInterceptTouchEvent(true)
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        parent?.requestDisallowInterceptTouchEvent(true)
        return super.onInterceptTouchEvent(ev)
    }
    private fun setupChart() {
        this.description.isEnabled = false
        this.setBackgroundColor(Color.WHITE)
        this.setTouchEnabled(true)
        this.setPinchZoom(true)

        // X Axis Customization
        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
        this.xAxis.setDrawGridLines(false)
        this.xAxis.setAvoidFirstLastClipping(true)

        // Left Y Axis
        this.axisLeft.isEnabled = false
        // Right Y Axis
        this.axisRight.setDrawGridLines(true)
        this.axisRight.setDrawAxisLine(true)
        this.axisRight.isEnabled = true

        // Disable Legend
        this.legend.isEnabled = false
    }

    fun setCandleData(dataList: List<CandleEntry>) {
        val candleDataSet = CandleDataSet(dataList, "Candlestick Data").apply {
            color = Color.rgb(80, 80, 80)
            shadowColor = Color.DKGRAY
            shadowWidth = 0.7f
            decreasingColor = Color.RED
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = Color.GREEN
            increasingPaintStyle = Paint.Style.FILL
            neutralColor = Color.BLUE
        }

        this.data = CandleData(candleDataSet)
        this.invalidate()
    }
}
