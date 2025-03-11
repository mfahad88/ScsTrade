package com.example.scstrade.views.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class GroupedBarChart extends BarChart {

    public GroupedBarChart(Context context) {
        super(context);
        initChart();
    }

    public GroupedBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChart();
    }

    public GroupedBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initChart();
    }

    private void initChart() {
        this.getDescription().setEnabled(false);
        this.setDrawGridBackground(false);
        this.setDrawBarShadow(false);
        this.setFitBars(true);
        this.setDrawValueAboveBar(true);

        XAxis xAxis = this.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        
        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawZeroLine(true);
        Legend legend = this.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        this.getAxisRight().setEnabled(false);
    }

    public void setGroupedBarData(List<BarEntry> group1, List<BarEntry> group2, List<BarEntry> group3, String label1, String label2, String label3) {
        float groupSpace = 0.1f;
        float barSpace = 0.05f;
        float barWidth = 0.2f;

        BarDataSet set1 = new BarDataSet(group1, label1);
        set1.setColor(0xFF6200EE); // Purple

        BarDataSet set2 = new BarDataSet(group2, label2);
        set2.setColor(0xFFFF5722); // Orange

        BarDataSet set3 = new BarDataSet(group3, label3);
        set2.setColor(0xFFFF5722); // Orange
        BarData data = new BarData(set1, set2,set3);
        data.setBarWidth(barWidth);
        
        this.setData(data);
        this.groupBars(0f, groupSpace, barSpace);
        this.invalidate();
    }
}