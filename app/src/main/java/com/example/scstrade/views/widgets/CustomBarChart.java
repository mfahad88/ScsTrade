package com.example.scstrade.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class CustomBarChart extends BarChart {
    public CustomBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupBarChart();
    }

    public CustomBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupBarChart();
    }

    private void setupBarChart() {
        this.getDescription().setEnabled(false);
        this.setDrawGridBackground(false);
        this.setDrawBarShadow(false);
        this.setPinchZoom(false);
        this.setDoubleTapToZoomEnabled(false);
        this.setDrawValueAboveBar(true);
        this.setHighlightFullBarEnabled(false);

        // X-Axis settings
        XAxis xAxis = this.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTypeface(Typeface.DEFAULT_BOLD);
        xAxis.setTextSize(12f);



        // Y-Axis settings
        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setTextSize(12f);

        this.getAxisRight().setEnabled(false);

        this.getLegend().setEnabled(false);



    }


    public void setChartData(List<String> labels, List<Float> values, float barWidth, int... barColors) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            entries.add(new BarEntry(i, values.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Sales");
        dataSet.setColors(barColors);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(barWidth);

        this.setData(data);
        this.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        this.invalidate(); // Refresh chart
    }

}
