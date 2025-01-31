package com.example.scstrade.views.widgets;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class CustomLineChart extends LineChart {

    private List<Entry> entries;
    private List<String> xLabels;
    private List<String> yLabels;
    private int lineColor = Color.BLUE;
    private int valueTextColor = Color.BLACK;

    public CustomLineChart(Context context) {
        super(context);
    }

    public CustomLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Set the data entries for the chart
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
        if(!entries.isEmpty()) {
            updateChart();
        }
    }

    // Set X-axis labels
    public void setXAxisLabels(List<String> labels) {
        this.xLabels = labels;
        updateXAxis();
    }

    // Set Y-axis labels
    public void setYAxisLabels(List<String> labels) {
        this.yLabels = labels;
        updateYAxis();
    }

    // Set styling for the line
    public void setLineColor(int color) {
        this.lineColor = color;
        updateChart();
    }

    // Set styling for the values text
    public void setValueTextColor(int color) {
        this.valueTextColor = color;
        updateChart();
    }

    private void updateChart() {
        try{
            // Create a dataset with the provided entries
            LineDataSet dataSet = new LineDataSet(entries, "Line Data");
            dataSet.setColor(lineColor);
            dataSet.setValueTextColor(valueTextColor);
            dataSet.setDrawValues(true);
            dataSet.setDrawCircles(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);



            // Create LineData object and set it to the chart
            LineData lineData = new LineData(dataSet);

            this.setData(lineData);
            this.invalidate(); // Refresh the chart
        }catch (Exception e){
            Log.e("CustomLineChart: ",e.getMessage());
        }
    }

    private void updateXAxis() {
        // Customize the X-axis with the provided labels
        XAxis xAxis = this.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
    }

    private void updateYAxis() {
        // Customize the Y-axis with the provided labels
        YAxis rightAxis = this.getAxisRight();
        rightAxis.setValueFormatter(new IndexAxisValueFormatter(yLabels));
        rightAxis.setAxisMinimum(0);

        // Disable the right Y-axis
        this.getAxisRight().setEnabled(false);
        this.getAxisLeft().setEnabled(true);
    }

    // Initialize default settings
    public void initChart() {
        this.getDescription().setEnabled(false); // Disable description
        this.getLegend().setEnabled(false); // Disable legend
    }

    public void removeGrid() {
        // Disable the grid lines for the Y-axis (both horizontal grid lines)
        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setDrawGridLines(false);  // Disable grid lines on the left Y-axis
        leftAxis.setDrawAxisLine(false);   // Optionally disable the axis line as well

        // Optionally, disable grid lines on the right Y-axis
        YAxis rightAxis = this.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);

        // Disable the grid lines for the X-axis (vertical grid lines)
        XAxis xAxis = this.getXAxis();
        xAxis.setDrawGridLines(false);  // Disable the vertical grid lines
        xAxis.setDrawAxisLine(false);   // Optionally disable the axis line as well
    }


    public void removeAxisValues() {
        // Disable the axis values on the X-axis
        XAxis xAxis = this.getXAxis();
        xAxis.setDrawLabels(false);  // Disable X-axis labels

        // Disable the axis values on the Y-axis (left and right)
        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setDrawLabels(false);  // Disable left Y-axis labels

        YAxis rightAxis = this.getAxisRight();
        rightAxis.setDrawLabels(false); // Disable right Y-axis labels
    }


    public void removeAxisTitles() {
        this.getDescription().setEnabled(false);
        this.getLegend().setEnabled(false);
        LineData lineData=this.getLineData();
        lineData.setDrawValues(false);
        this.setData(lineData);
    }

}
