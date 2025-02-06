package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.scstrade.R;
import com.example.scstrade.databinding.NetChangeChipBinding;
import com.example.scstrade.databinding.VolumeChipBinding;

public class NetChangeChip extends RelativeLayout {
    NetChangeChipBinding binding;
    public NetChangeChip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NetChangeChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        binding= NetChangeChipBinding.inflate(LayoutInflater.from(getContext()),this,true);
        if(attrs!=null){
            TypedArray a=getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.NetChangeChip,
                    0,0
            );
            try{

                String text=a.getString(R.styleable.NetChangeChip_android_text);
                int color=a.getColor(R.styleable.NetChangeChip_android_background, Color.TRANSPARENT);
                if(text!=null){
                    binding.tv.setText(text);
                }
                if(color!=Color.TRANSPARENT) {
                    binding.relativeLayout.setBackgroundColor(color);
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
