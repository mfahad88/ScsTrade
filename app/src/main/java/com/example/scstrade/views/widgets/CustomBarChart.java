package com.example.scstrade.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomBarChart extends BarChart {
    public CustomBarChart(Context context) {
        super(context);
        setupBarChart();
    }

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
    public void setChartData(List<String> labels, List<Float> values, float barWidth) {
        // ✅ Safety check: null or empty input
        if (labels == null || values == null || labels.isEmpty() || values.isEmpty() || labels.size() != values.size()) {
            this.clear();
            this.invalidate();
            return;
        }

        // ✅ Sanitize labels (remove blank/"null")
        List<String> safeLabels = new ArrayList<>();
        for (String label : labels) {
            if (label != null && !label.trim().isEmpty() && !"null".equalsIgnoreCase(label.trim())) {
                safeLabels.add(label.trim());
            } else {
                safeLabels.add(""); // fallback blank
            }
        }

        // ✅ Create Bar Entries
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            Float value = values.get(i);
            if (value != null) {
                entries.add(new BarEntry(i, value));
            }
        }

        // ✅ Handle empty safe entries
        if (entries.isEmpty()) {
            this.clear();
            this.invalidate();
            return;
        }

        // ✅ Assign colors based on value sign
        List<Integer> colors = new ArrayList<>();
        for (BarEntry entry : entries) {
            if (entry.getY() >= 0) {
                colors.add(ContextCompat.getColor(getContext(), R.color.md_theme_primary)); // Positive
            } else {
                colors.add(ContextCompat.getColor(getContext(), R.color.md_theme_errorContainer)); // Negative
            }
        }

        // ✅ Create DataSet
        BarDataSet dataSet = new BarDataSet(entries, "Sales");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black));
        dataSet.setValueTextSize(12f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                float y = barEntry.getY();
                return Math.abs(y) < 0.0001f ? "" : String.format(Locale.US, "%,.2f", y);
            }
        });

        // ✅ Set chart data
        BarData data = new BarData(dataSet);
        data.setBarWidth(barWidth);
        this.setData(data);
        this.getXAxis().setValueFormatter(new IndexAxisValueFormatter(safeLabels));
        this.invalidate(); // Refresh chart
    }


 /*   public void setChartData(List<String> labels, List<Float> values, float barWidth) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            entries.add(new BarEntry(i, values.get(i)));
        }
        List<Integer> colors = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "Sales");
        for (BarEntry entry : entries) {
            if (entry.getY() >= 0) {
                colors.add(ContextCompat.getColor(getContext(), R.color.md_theme_primary)); // Positive values - Green
            } else {
                colors.add(ContextCompat.getColor(getContext(), R.color.md_theme_errorContainer)); // Negative values - Red
            }
        }
        dataSet.setColors(colors);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(),R.color.black));
        dataSet.setValueTextSize(12f);
        dataSet.setValueFormatter(new ValueFormatter() {
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

        BarData data = new BarData(dataSet);
        data.setBarWidth(barWidth);

        this.setData(data);
        this.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        this.invalidate(); // Refresh chart
    }*/

}
