package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.scstrade.R;
import com.example.scstrade.databinding.HighBinding;
import com.example.scstrade.databinding.VolumeChipBinding;

public class HighView extends RelativeLayout {
    HighBinding binding;
    public HighView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HighView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        binding= HighBinding.inflate(LayoutInflater.from(getContext()),this,true);
        if(attrs!=null){
            TypedArray a=getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.HighView,
                    0,0
            );
            try{

                String text=a.getString(R.styleable.HighView_android_text);
                if(text!=null){
                    binding.tv.setText(text);
                }

            }finally {
                a.recycle();
            }
        }

    }

    public void setText(String text){
        binding.tv.setText(text);
    }

    public String getText(){
        return binding.tv.getText().toString();
    }
}
