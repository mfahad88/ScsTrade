package com.example.scstrade.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

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
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        
        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawZeroLine(true);

        Legend legend = this.getLegend();
        legend.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        this.getAxisRight().setEnabled(false);
    }

    public void setGroupedBarData(List<BarEntry> group1, List<BarEntry> group2, List<BarEntry> group3,List<BarEntry> group4,List<String> years, String label1, String label2, String label3,String label4) {
        float groupSpace = 0.1f;
        float barSpace = 0.05f;
        float barWidth = 0.2f;

        BarDataSet set1 = new BarDataSet(group1, label1);
        set1.setColor(Color.parseColor("#7cb5ec")); // Purple

        BarDataSet set2 = new BarDataSet(group2, label2);
        set2.setColor(Color.parseColor("#434348")); // Orange

        BarDataSet set3 = new BarDataSet(group3, label3);
        set3.setColor(Color.parseColor("#90ed7d")); // Orange

        BarDataSet set4 = new BarDataSet(group4, label4);
        set4.setColor(Color.parseColor("#f7a35c")); // Orange

        BarData data = new BarData(set1, set2,set3,set4);

        XAxis xAxis=this.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(years));
        xAxis.setGranularity(1f); // ensures 1:1 mapping
        xAxis.setGranularityEnabled(true);
        data.setBarWidth(barWidth);
        this.setData(data);
        int groupCount = group1.size();
        float groupWidth = data.getGroupWidth(groupSpace, barSpace);
        this.setDrawValueAboveBar(true);
        getXAxis().setAxisMinimum(0f);
        getXAxis().setAxisMaximum(0f + groupCount * groupWidth);
        this.setFitBars(true);
        this.setExtraOffsets(0f,0f,0f,0f);
        this.groupBars(0f, groupSpace, barSpace);

        if(!group1.stream().anyMatch(value->value.getY()<0) || !group2.stream().anyMatch(value->value.getY()<0) ||
                !group3.stream().anyMatch(value->value.getY()<0) ) {
            this.getAxisLeft().setAxisMinimum(0f);
        }


//        this.groupBars(0f, groupSpace, barSpace);
        this.invalidate();
    }
}