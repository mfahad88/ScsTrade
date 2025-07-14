package com.example.scstrade.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomCombinedChart extends CombinedChart {

    public CustomCombinedChart(Context context) {
        super(context);
        setupChart();
    }

    public CustomCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupChart();
    }

    public CustomCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupChart();
    }

    private void setupChart() {
        this.getDescription().setEnabled(false);
        this.setDrawGridBackground(false);
        this.setDrawBarShadow(false);
        this.setPinchZoom(false);
        this.setDoubleTapToZoomEnabled(false);
        this.setDrawValueAboveBar(true);
        this.setHighlightFullBarEnabled(false);
        // Configure X-Axis
        XAxis xAxis = this.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);

        // Configure Left Y-Axis
        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        // Disable Right Y-Axis
        YAxis rightAxis = this.getAxisRight();
        rightAxis.setDrawGridLines(true);
        this.getAxisRight().setEnabled(true);
    }

    public void setChartData(
            List<Float> barValues,
            List<Float> lineValues,
            List<String> labels,
            int barColor,
            String name1,
            String name2
    ) {
        // ✅ Null or empty input check
        if (barValues == null || barValues.isEmpty() ||
                lineValues == null || lineValues.isEmpty() ||
                labels == null || labels.isEmpty()) {
            this.clear();
            this.invalidate();
            return;
        }

        // ✅ Sanitize labels (null, empty, "null")
        List<String> safeLabels = new ArrayList<>();
        for (String label : labels) {
            if (label != null && !label.trim().isEmpty() && !"null".equalsIgnoreCase(label.trim())) {
                safeLabels.add(label.trim());
            } else {
                safeLabels.add(""); // fallback blank label
            }
        }

        CombinedData data = new CombinedData();

        // ✅ Line Data (e.g. Price-to-Book)
        LineData lineData = generateLineData(lineValues, name1);
        data.setData(lineData);

        // ✅ Bar Data (e.g. Book Value)
        BarData barData = generateBarData(barValues, barColor, name2);
        float barWidth = 0.5f;
        float barCount = barValues.size();
        barData.setBarWidth(barWidth);

        // ✅ Configure X axis limits for proper bar rendering
        getXAxis().setAxisMinimum(-barWidth);                  // Left padding for first bar
        getXAxis().setAxisMaximum(barCount - 1 + barWidth);    // Right padding for last bar

        data.setData(barData); // ✅ Set BarData after LineData

        // ✅ Final chart setup
        this.setData(data);
        this.getXAxis().setAvoidFirstLastClipping(true);
        this.getXAxis().setValueFormatter(new IndexAxisValueFormatter(safeLabels));

        this.invalidate();
    }

    /*public void setChartData(List<Float> barValues, List<Float> lineValues, List<String> labels,int barColor,String name1,String name2) {
        CombinedData data = new CombinedData();


        // Line Data (Price to Book Value)
        LineData lineData = generateLineData(lineValues,name1);
        data.setData(lineData);

        // Bar Data (Book Value)
        BarData barData = generateBarData(barValues,barColor,name2);
        *//*float groupCount = barValues.size();
        float barWidth = 0.5f;
        float xMin = 0f;
        float xMax = xMin + groupCount;

        this.getXAxis().setAxisMinimum(xMin);
        this.getXAxis().setAxisMaximum(xMax);*//*
        float barWidth = 0.5f;
        float barCount = barValues.size();
        barData.setBarWidth(barWidth);
        getXAxis().setAxisMinimum(-barWidth); // Shift to show first bar completely
        getXAxis().setAxisMaximum(barCount - 1 + barWidth ); // Extend to show last bar



//        barData.groupBars(0f,0.3f,0.1f);
        data.setData(barData);

        // Set Data to Chart
        this.setData(data);
        this.getXAxis().setAvoidFirstLastClipping(true);
        this.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        this.invalidate();
    }*/

    private BarData generateBarData(List<Float> values, int barColor,String name) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            entries.add(new BarEntry(i, values.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, name);

        dataSet.setColor(barColor);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(),R.color.black));
        dataSet.setValueTextSize(10f);
        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
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
        return new BarData(dataSets);
    }

    private LineData generateLineData(List<Float> values,String name) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            entries.add(new Entry(i, values.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, name);
        dataSet.setColor(ContextCompat.getColor(getContext(),R.color.black));
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(),R.color.black));
        dataSet.setValueTextSize(10f);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setCircleRadius(4f);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

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

        return new LineData(dataSets);
    }
}
