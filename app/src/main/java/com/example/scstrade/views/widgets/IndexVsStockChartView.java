package com.example.scstrade.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class IndexVsStockChartView extends ConstraintLayout {

    private LineChart chart;

    public IndexVsStockChartView(Context context) {
        super(context);
        init(context);
    }

    public IndexVsStockChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IndexVsStockChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        chart = new LineChart(context);
        chart.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        addView(chart);
        setupChart();
    }

    private void setupChart() {
        chart.setTouchEnabled(false);
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.GRAY);
        xAxis.setGranularity(1f); // ensures 1:1 step between labels
        xAxis.setGranularityEnabled(true); // enables granularity
//        xAxis.setLabelCount(xLabels.size(), true); // show all labels, if space allows
        xAxis.setAvoidFirstLastClipping(true); // prevent edge labels from being clipped
        xAxis.setTextSize(10f); // reduce if overlapping
        xAxis.setLabelRotationAngle(0f);
//        chart.getAxisLeft().setAxisMinimum(175f);
//        chart.getAxisLeft().setAxisMaximum(275f);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setTextColor(Color.GRAY);

        chart.getAxisRight().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setTextColor(Color.BLACK);
    }

    public void setChartData(List<String> xLabels, List<Float> indexValues, List<Float> stockValues,
                              String indexLabel, String stockLabel) {

        List<Entry> indexEntries = new ArrayList<>();
        for (int i = 0; i < indexValues.size(); i++) {
            indexEntries.add(new Entry(i, indexValues.get(i)));
        }

        List<Entry> stockEntries = new ArrayList<>();
        for (int i = 0; i < stockValues.size(); i++) {
            stockEntries.add(new Entry(i, stockValues.get(i)));
        }

        LineDataSet indexDataSet = new LineDataSet(indexEntries, indexLabel);
        indexDataSet.setColor(Color.BLACK);
        indexDataSet.setDrawCircles(false);
        indexDataSet.setLineWidth(2f);
        indexDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineDataSet stockDataSet = new LineDataSet(stockEntries, stockLabel);
        stockDataSet.setColor(Color.parseColor("#84C5FF"));
        stockDataSet.setDrawCircles(false);
        stockDataSet.setLineWidth(2f);
        stockDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        LineData lineData = new LineData(indexDataSet, stockDataSet);
        chart.setData(lineData);
        chart.invalidate();
    }
}