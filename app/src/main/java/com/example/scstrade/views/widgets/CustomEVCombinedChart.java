package com.example.scstrade.views.widgets;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CustomEVCombinedChart extends CombinedChart {

    public CustomEVCombinedChart(Context context) {
        super(context);
        setupChart();
    }

    public CustomEVCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupChart();
    }

    public CustomEVCombinedChart(Context context, AttributeSet attrs, int defStyle) {
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

        // X-Axis
        XAxis xAxis = this.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);

        // Left Y-Axis (EV / EBITDA)
        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//        leftAxis.setAxisMinimum(1f);
//        leftAxis.setAxisMaximum(4f);
        leftAxis.setGranularity(1f);
        leftAxis.setValueFormatter(new BillionFormatter());

        // Right Y-Axis (Market Cap & EP Value)
        YAxis rightAxis = this.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(true);
        rightAxis.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_bright));
        rightAxis.setAxisMinimum(0f);
//        rightAxis.setAxisMaximum(750f);
//        rightAxis.setGranularity(100f);
        rightAxis.setValueFormatter(new BillionFormatter());

        // Legend
        Legend legend = this.getLegend();
        legend.setTextSize(12f);
        legend.setWordWrapEnabled(true);
    }

    public void setChartData(
            List<Float> marketCapValues,
            List<Float> epValues,
            List<Float> evEbitdaValues,
            List<String> labels
    ) {
        CombinedData data = new CombinedData();

        // BarData (Market Cap & EP Value)
        BarData barData = generateBarData(marketCapValues, epValues);
        float barWidth = 0.3f;
        float groupSpace = 0.5f;
        float barSpace = 0.02f;
        barData.setBarWidth(barWidth);
        barData.groupBars(0f, groupSpace, barSpace);
        data.setData(barData);
        float groupWidth = barData.getGroupWidth(groupSpace, barSpace);
        float xAxisMin   = 0f;                // start at 0 (matches groupBars call)
        float xAxisMax   = xAxisMin + labels.size() * groupWidth;

        getXAxis().setAxisMinimum(xAxisMin);
        getXAxis().setAxisMaximum(xAxisMax);
        // LineData (EV / EBITDA)
        LineData lineData = generateLineData(evEbitdaValues);
        data.setData(lineData);

        // Apply to chart
        this.setData(data);
        this.getXAxis().setAvoidFirstLastClipping(true);
        this.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        this.getXAxis().setAxisMinimum(0f);
        this.getXAxis().setAxisMaximum(labels.size());

        this.invalidate();
    }

    private BarData generateBarData(List<Float> marketCaps, List<Float> epValues) {
        List<BarEntry> marketEntries = new ArrayList<>();
        List<BarEntry> epEntries = new ArrayList<>();

        for (int i = 0; i < marketCaps.size(); i++) {
            marketEntries.add(new BarEntry(i, marketCaps.get(i)));
            epEntries.add(new BarEntry(i, epValues.get(i)));
        }


        BarDataSet marketSet = new BarDataSet(marketEntries, "Market Cap");
        marketSet.setColor(Color.RED);
        marketSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black));
        marketSet.setValueTextSize(10f);
        marketSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
//        marketSet.setValueFormatter(new IndexAxisValueFormatter(marketCaps.stream().map(String::valueOf).collect(Collectors.toList())));
        marketSet.setValueFormatter(new BillionFormatter());


        BarDataSet epSet = new BarDataSet(epEntries, "EP Value");
        epSet.setColor(Color.parseColor("#ADD8E6"));
        epSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black));
        epSet.setValueTextSize(10f);
        epSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        epSet.setValueFormatter(new BillionFormatter());

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(marketSet);
        dataSets.add(epSet);

        return new BarData(dataSets);
    }

    private LineData generateLineData(List<Float> values) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            entries.add(new Entry(i, values.get(i)));
        }

        LineDataSet lineSet = new LineDataSet(entries, "EV / EBITDA");
        lineSet.setColor(ContextCompat.getColor(getContext(), R.color.black));
        lineSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black));
        lineSet.setValueTextSize(10f);
        lineSet.setCircleColor(Color.BLACK);
        lineSet.setCircleRadius(4f);
        lineSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineSet.setValueFormatter(new BillionFormatter());
        lineSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineSet.setCubicIntensity(0.2f);
        lineSet.setValueFormatter(new ValueFormatter() {
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
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineSet);


        return new LineData(dataSets);
    }

    private static class BillionFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            if(value >=1_000_000_000){
                return String.format("%.0f", value / 1_000_000_000);
            }else{
                return String.format("%.0f", value);
            }

        }
    }


    private float getMaxValue(List<Float> values) {
        float max = Float.MIN_VALUE;
        for (float val : values) {
            if (val > max) max = val;
        }
        return max;
    }

    private float roundUp(float value) {
        // Round up to nearest "nice" number (e.g., 100, 500, 1, 5)
        if (value <= 5) return 5f;
        if (value <= 10) return 10f;
        if (value <= 100) return ((int) (value / 10 + 1)) * 10f;
        if (value <= 500) return ((int) (value / 50 + 1)) * 50f;
        return ((int) (value / 100 + 1)) * 100f;
    }
}