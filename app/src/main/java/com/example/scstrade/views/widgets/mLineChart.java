package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class mLineChart extends LineChart {
    private Context context;
    private AttributeSet attrs;
    private int lineColor;
    private int filledColor;
    private int circleColor;
    LineDataSet dataSet;

    public int getFilledColor() {
        return filledColor;
    }

    public void setFilledColor(int filledColor) {
        this.filledColor = filledColor;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }




    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }


    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries,boolean isRightEnabled) {
        this.entries = entries;
        this.isRightEnabled = isRightEnabled;
        init(context,attrs);
    }

    private List<Entry> entries;
    private boolean isRightEnabled=true;
   /* public mLineChart(Context context) {
        super(context);
        this.context=context;
        init(context, null);
    }
*/
    public mLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.attrs=attrs;
        init(context,attrs);
    }



    public mLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
        this.attrs=attrs;
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a=context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LabelledTextField,
                0,0
        );

        lineColor=ContextCompat.getColor(context,R.color.md_theme_primary);
//        filledColor=ContextCompat.getColor(context,R.color.md_theme_secondaryFixedDim);
        dataSet=new LineDataSet(entries,"");

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setCircleColor(circleColor);
        dataSet.setFillColor(filledColor);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(AppCompatResources.getDrawable(context,R.drawable.shadow_green));
        dataSet.setColor(lineColor);
        dataSet.setLineWidth(2f);
        dataSet.setDrawIcons(false);


        LineData lineData = new LineData(dataSet);

        this.setData(lineData);
        this.getDescription().setEnabled(false);
        this.getLegend().setEnabled(false);
        XAxis xAxis = this.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ContextCompat.getColor(context,R.color.black));
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);
        leftAxis.setTextColor(ContextCompat.getColor(context,R.color.black));

        YAxis rightAxis = this.getAxisRight();
        rightAxis.setTextSize(5f);
        rightAxis.setTextColor(ContextCompat.getColor(context,R.color.black));
        rightAxis.setDrawLabels(true);
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(isRightEnabled);

        this.notifyDataSetChanged();
//        this.animateXY(5000,5000);
        this.invalidate();
        a.recycle();
    }
}
