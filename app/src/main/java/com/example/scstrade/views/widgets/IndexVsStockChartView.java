package com.example.scstrade.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
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
        chart.setTouchEnabled(true);     // Enable gestures
        chart.setDragEnabled(true);      // Allow dragging
        chart.setScaleEnabled(false);    // Disable pinch zoom
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setDragDecelerationEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.9f);
        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.GRAY);
        xAxis.setTextSize(10f);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setLabelRotationAngle(0f);

        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setTextColor(Color.GRAY);
        chart.getAxisLeft().setTextSize(10f);
        chart.getAxisRight().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        legend.setTextSize(12f);
    }

    public void setChartData(List<String> xLabels, List<Float> indexValues, List<Float> stockValues,
                             String indexLabel, String stockLabel) {
        // ✅ Safety checks
        if (xLabels == null || indexValues == null || stockValues == null ||
                xLabels.isEmpty() || indexValues.isEmpty() || stockValues.isEmpty()) {
            chart.clear();
            chart.invalidate();
            return;
        }

        // ✅ Ensure all lists are aligned
        int minSize = Math.min(xLabels.size(), Math.min(indexValues.size(), stockValues.size()));
        if (minSize == 0) {
            chart.clear();
            chart.invalidate();
            return;
        }

        // ✅ Clean xLabels (handle null/blank/"null")
        List<String> safeLabels = new ArrayList<>();
        for (int i = 0; i < minSize; i++) {
            String label = xLabels.get(i);
            if (label != null && !label.trim().isEmpty() && !"null".equalsIgnoreCase(label.trim())) {
                safeLabels.add(label.trim());
            } else {
                safeLabels.add(""); // fallback blank label
            }
        }

        // ✅ Populate Entries
        List<Entry> indexEntries = new ArrayList<>();
        List<Entry> stockEntries = new ArrayList<>();
        for (int i = 0; i < minSize; i++) {
            indexEntries.add(new Entry(i, indexValues.get(i)));
            stockEntries.add(new Entry(i, stockValues.get(i)));
        }

        // ✅ Index Line
        LineDataSet indexDataSet = new LineDataSet(indexEntries, indexLabel);
        indexDataSet.setColor(ContextCompat.getColor(getContext(), R.color.black));
        indexDataSet.setDrawCircles(false);
        indexDataSet.setLineWidth(2f);
        indexDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        indexDataSet.setDrawValues(false);

        // ✅ Stock Line
        LineDataSet stockDataSet = new LineDataSet(stockEntries, stockLabel);
        stockDataSet.setColor(Color.parseColor("#84C5FF"));
        stockDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black));
        stockDataSet.setDrawCircles(false);
        stockDataSet.setLineWidth(2f);
        stockDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        stockDataSet.setDrawValues(false);

        // ✅ X-Axis Setup
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(safeLabels));
        xAxis.setGranularity(Math.max(1f, minSize / 6f));
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawLabels(false); // hide text if needed

        // ✅ Enable scrolling and set viewport
        chart.setVisibleXRangeMaximum(40f);
        if (!stockEntries.isEmpty()) {
            chart.moveViewToX(stockEntries.get(stockEntries.size() - 1).getX()); // scroll to latest
        }

        // ✅ Apply data and refresh
        chart.setData(new LineData(indexDataSet, stockDataSet));
        chart.invalidate();
    }

    /*public void setChartData(List<String> xLabels, List<Float> indexValues, List<Float> stockValues,
                             String indexLabel, String stockLabel) {

        if (xLabels == null || indexValues == null || stockValues == null) return;

        List<Entry> indexEntries = new ArrayList<>();
        List<Entry> stockEntries = new ArrayList<>();
        for (int i = 0; i < xLabels.size(); i++) {
            if (i < indexValues.size() && i < stockValues.size()) {
                indexEntries.add(new Entry(i, indexValues.get(i)));
                stockEntries.add(new Entry(i, stockValues.get(i)));
            }
        }

        LineDataSet indexDataSet = new LineDataSet(indexEntries, indexLabel);
        indexDataSet.setColor(ContextCompat.getColor(getContext(), R.color.black));
        indexDataSet.setDrawCircles(false);
        indexDataSet.setLineWidth(2f);
        indexDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        indexDataSet.setDrawValues(false);

        LineDataSet stockDataSet = new LineDataSet(stockEntries, stockLabel);
        stockDataSet.setValueTextColor(ContextCompat.getColor(getContext(),R.color.black));
        stockDataSet.setColor(Color.parseColor("#84C5FF"));
        stockDataSet.setDrawCircles(false);
        stockDataSet.setLineWidth(2f);
        stockDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        stockDataSet.setDrawValues(false);

        // Dynamic granularity for X-axis
        float granularity = (float) Math.max(1, xLabels.size() / 6);
        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setGranularity(granularity);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawLabels(false);

        // Enable scrolling
        chart.setVisibleXRangeMaximum(40f); // show 40 points at once
        if (!stockEntries.isEmpty()) {
            chart.moveViewToX(stockEntries.get(stockEntries.size() - 1).getX()); // scroll to end
        }

        chart.setData(new LineData(indexDataSet, stockDataSet));
        chart.invalidate();
    }*/
}
