package com.example.scstrade.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class MultiLineChartView extends LineChart {
    public MultiLineChartView(Context context) {
        super(context);
        setupChart();
    }

    public MultiLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupChart();
    }

    public MultiLineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupChart();
    }

    private void setupChart() {
        this.getDescription().setEnabled(false);
        this.setDrawGridBackground(false);

        // X-Axis Configuration
        XAxis xAxis = this.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        // Y-Axis Configuration
        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setDrawLabels(false);
        leftAxis.setTextSize(12f);
        this.getAxisRight().setEnabled(false);

        // Legend
        Legend legend = this.getLegend();
        legend.setTextSize(12f);
    }

    public void setChartData(List<String> labels,List<String> legends, List<Entry> greenLineEntries, List<Entry> blueLineEntries, List<Entry> blackLineEntries,List<Integer> colors) {
        LineData lineData = new LineData();
        if(greenLineEntries!=null) {
            // Green Line DataSet
            LineDataSet greenLineDataSet = new LineDataSet(greenLineEntries, legends.get(0));
            greenLineDataSet.setColor(colors.get(0));
            greenLineDataSet.setCircleColor(colors.get(0));
            greenLineDataSet.setValueTextSize(12f);
            greenLineDataSet.setLineWidth(2f);
            lineData.addDataSet(greenLineDataSet);
        }

        if(blueLineEntries!=null) {
            // Blue Line DataSet
            LineDataSet blueLineDataSet = new LineDataSet(blueLineEntries, legends.get(1));
            blueLineDataSet.setColor(colors.get(1));
            blueLineDataSet.setCircleColor(colors.get(1));
            blueLineDataSet.setValueTextSize(12f);
            blueLineDataSet.setLineWidth(2f);
            lineData.addDataSet(blueLineDataSet);
        }

        if(blackLineEntries!=null) {
            // Black Line DataSet
            LineDataSet blackLineDataSet = new LineDataSet(blackLineEntries, legends.get(2));
            blackLineDataSet.setColor(colors.get(2));
            blackLineDataSet.setCircleColor(colors.get(2));
            blackLineDataSet.setValueTextSize(12f);
            blackLineDataSet.setLineWidth(2f);
            lineData.addDataSet(blackLineDataSet);
        }

        // Combine Data


//        this.getAxisLeft().setValueFormatter(new IndexAxisValueFormatter(strings));
        // Apply Data to Chart
        this.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        this.setData(lineData);
        this.invalidate(); // Refresh chart
    }
}
