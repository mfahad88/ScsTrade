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
        leftAxis.setAxisMinimum(100f);
        leftAxis.setTextSize(12f);
        this.getAxisRight().setEnabled(false);

        // Legend
        Legend legend = this.getLegend();
        legend.setTextSize(12f);
    }

    public void setChartData(List<String> labels, List<Entry> greenLineEntries, List<Entry> blueLineEntries, List<Entry> blackLineEntries) {
        List<String> strings=new ArrayList<>();
        strings.add("100");
        strings.add("300");
        strings.add("500");
        strings.add("700");
        strings.add("900");


        // Green Line DataSet
        LineDataSet greenLineDataSet = new LineDataSet(greenLineEntries, "Green Line");
        greenLineDataSet.setColor(Color.GREEN);
        greenLineDataSet.setCircleColor(Color.GREEN);
        greenLineDataSet.setValueTextSize(12f);
        greenLineDataSet.setLineWidth(2f);

        // Blue Line DataSet
        LineDataSet blueLineDataSet = new LineDataSet(blueLineEntries, "Blue Line");
        blueLineDataSet.setColor(Color.BLUE);
        blueLineDataSet.setCircleColor(Color.BLUE);
        blueLineDataSet.setValueTextSize(12f);
        blueLineDataSet.setLineWidth(2f);

        // Black Line DataSet
        LineDataSet blackLineDataSet = new LineDataSet(blackLineEntries, "Black Line");
        blackLineDataSet.setColor(Color.BLACK);
        blackLineDataSet.setCircleColor(Color.BLACK);
        blackLineDataSet.setValueTextSize(12f);
        blackLineDataSet.setLineWidth(2f);

        // Combine Data
        LineData lineData = new LineData(greenLineDataSet, blueLineDataSet, blackLineDataSet);
        this.getAxisLeft().setValueFormatter(new IndexAxisValueFormatter(strings));
        // Apply Data to Chart
        this.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        this.setData(lineData);
        this.invalidate(); // Refresh chart
    }
}
