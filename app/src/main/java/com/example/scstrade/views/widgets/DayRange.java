package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.scstrade.R;
import com.example.scstrade.databinding.DayRangeBinding;

public class DayRange extends RelativeLayout {
    DayRangeBinding binding;
    AttributeSet attrs;
    float low;
    float high;
    float close;
    public DayRange(Context context) {
        super(context);
        init();
    }

    public DayRange(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        init();
    }

    public DayRange(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        init();
    }

    private void init(){
        binding=DayRangeBinding.inflate(LayoutInflater.from(getContext()),this,true);

        if(attrs!=null){
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.DayRange,0,0
            );
            try{
//                low=a.getFloat(R.styleable.DayRange_lowPrice,0f);
//                high=a.getFloat(R.styleable.DayRange_highPrice,0f);
//                close=a.getFloat(R.styleable.DayRange_closePrice,0f);

                if(low>0f){
                    binding.low.setText("Rs."+low);
                }

                if(high>0f){
                    binding.high.setText("Rs."+high);
                }

                if(close>0f){
                    float progress=((close-low)/(high-low)) * 100;
                    binding.indicator.post(() -> {
                        int progressWidth=binding.progress.getWidth();
                        float position = (progressWidth*progress);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.indicator.getLayoutParams();
                        params.setMarginStart((int) position);
                        binding.indicator.setLayoutParams(params);
                    });

                }
            }finally {
                a.recycle();
            }
        }

    }


    public void setLow(float low,float high,float close){
        this.low = low;
        this.high = high;
        this.close = close>high?high:close;

        if(low>0f){
            binding.low.setText("Rs."+this.low);
        }

        if(high>0f){
            binding.high.setText("Rs."+this.high);
        }

        if(close>0f){
            int progress= (int) ((this.close - this.low)/(this.high-this.low)*100);

            binding.indicator.post(() -> {
                int progressWidth=binding.progress.getWidth();
                float position = (progressWidth*(progress/100f));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.indicator.getLayoutParams();
                params.setMarginStart((int) position - binding.indicator.getWidth()/2);
                binding.indicator.setLayoutParams(params);
            });

        }
    }




}
