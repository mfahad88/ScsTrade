package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
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

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
        init(context,attrs);
    }

    private List<Entry> entries;
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
        boolean isTouchEnabled=a.getBoolean(R.styleable.mLineChart_isTouchEnabled,false);
        boolean isPinchZoom=a.getBoolean(R.styleable.mLineChart_isPinchZoom,false);
        boolean isDragEnabled=a.getBoolean(R.styleable.mLineChart_isDragEnabled,false);
        boolean isScaleEnabled=a.getBoolean(R.styleable.mLineChart_isScaleEnabled,false);
        boolean isRightAxis=a.getBoolean(R.styleable.mLineChart_isRightAxis,false);
        boolean isLeftAxis=a.getBoolean(R.styleable.mLineChart_isLeftAxis,false);
        String entry=a.getString(R.styleable.mLineChart_entries);
        if(entry!=null){
            try{
                JSONArray jsonArray=new JSONArray(entry);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    entries.add(new Entry(Float.parseFloat(obj.getString("x")),Float.parseFloat(obj.getString("y"))));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        this.setTouchEnabled(isTouchEnabled);
        this.setPinchZoom(isPinchZoom);
        this.setDragEnabled(isDragEnabled);
        this.setScaleEnabled(isScaleEnabled);

        LineDataSet dataSet=new LineDataSet(entries,"");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCircleColor(circleColor);
        dataSet.setFillColor(filledColor);
        dataSet.setDrawFilled(true);
        dataSet.setColor(lineColor);
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(true);

        LineData lineData = new LineData(dataSet);

        this.setData(lineData);

        XAxis xAxis = this.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(isLeftAxis);
        YAxis rightAxis = this.getAxisRight();
        rightAxis.setDrawLabels(true);
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(isRightAxis);
        this.animateXY(1000,1000);
        this.invalidate();
        a.recycle();
    }
}
