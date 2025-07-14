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
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        // ✅ Safety: Check for null or empty input
        if (group1 == null || group2 == null || group3 == null || group4 == null || years == null ||
                group1.isEmpty() || group2.isEmpty() || group3.isEmpty() || group4.isEmpty() || years.isEmpty()) {
            this.clear();
            this.invalidate();
            return;
        }

        float groupSpace = 0.1f;
        float barSpace = 0.05f;
        float barWidth = 0.2f;

        // ✅ Helper to create DataSet with safe formatter
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                float y = barEntry.getY();
                return Math.abs(y) < 0.0001f ? "" : String.format(Locale.US, "%,.2f", y);
            }
        };

        BarDataSet set1 = new BarDataSet(group1, label1);
        set1.setColor(Color.parseColor("#7cb5ec"));
        set1.setValueFormatter(formatter);

        BarDataSet set2 = new BarDataSet(group2, label2);
        set2.setColor(Color.parseColor("#434348"));
        set2.setValueFormatter(formatter);

        BarDataSet set3 = new BarDataSet(group3, label3);
        set3.setColor(Color.parseColor("#90ed7d"));
        set3.setValueFormatter(formatter);

        BarDataSet set4 = new BarDataSet(group4, label4);
        set4.setColor(Color.parseColor("#f7a35c"));
        set4.setValueFormatter(formatter);

        BarData data = new BarData(set1, set2, set3, set4);

        // ✅ Clean and validate year labels
        List<String> safeYears = new ArrayList<>();
        for (String year : years) {
            if (year != null && !year.trim().isEmpty() && !"null".equalsIgnoreCase(year.trim())) {
                safeYears.add(year.trim());
            } else {
                safeYears.add(""); // fallback blank label
            }
        }

        XAxis xAxis = this.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(safeYears));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        data.setBarWidth(barWidth);
        this.setData(data);

        int groupCount = group1.size();
        float groupWidth = data.getGroupWidth(groupSpace, barSpace);

        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(groupCount * groupWidth);
        this.setDrawValueAboveBar(true);
        this.setFitBars(true);
        this.setExtraOffsets(0f, 0f, 0f, 0f);
        this.groupBars(0f, groupSpace, barSpace);

        // ✅ Axis: force Y-axis minimum to 0 if all values are non-negative
        boolean allPositive = group1.stream().allMatch(e -> e.getY() >= 0)
                && group2.stream().allMatch(e -> e.getY() >= 0)
                && group3.stream().allMatch(e -> e.getY() >= 0)
                && group4.stream().allMatch(e -> e.getY() >= 0);

        if (allPositive) {
            this.getAxisLeft().setAxisMinimum(0f);
        }

        this.invalidate();
       /* float groupSpace = 0.1f;
        float barSpace = 0.05f;
        float barWidth = 0.2f;


        BarDataSet set1 = new BarDataSet(group1, label1);
        set1.setColor(Color.parseColor("#7cb5ec")); // Purple
        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                float y = barEntry.getY();
                // Treat anything extremely close to zero as zero
                if (Math.abs(y) < 0.0001f) {          // 0 → show nothing
                    return "";
                }
                return String.format(Locale.US, "%,.2f", y);
            }
        });
        BarDataSet set2 = new BarDataSet(group2, label2);
        set2.setColor(Color.parseColor("#434348")); // Orange
        set2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                float y = barEntry.getY();
                // Treat anything extremely close to zero as zero
                if (Math.abs(y) < 0.0001f) {          // 0 → show nothing
                    return "";
                }
                return String.format(Locale.US, "%,.2f", y);
            }
        });
        BarDataSet set3 = new BarDataSet(group3, label3);
        set3.setColor(Color.parseColor("#90ed7d")); // Orange
        set3.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                float y = barEntry.getY();
                // Treat anything extremely close to zero as zero
                if (Math.abs(y) < 0.0001f) {          // 0 → show nothing
                    return "";
                }
                return String.format(Locale.US, "%,.2f", y);
            }
        });
        BarDataSet set4 = new BarDataSet(group4, label4);
        set4.setColor(Color.parseColor("#f7a35c")); // Orange
        set4.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                float y = barEntry.getY();
                // Treat anything extremely close to zero as zero
                if (Math.abs(y) < 0.0001f) {          // 0 → show nothing
                    return "";
                }
                return String.format(Locale.US, "%,.2f", y);
            }
        });

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


        this.invalidate();*/
    }
}