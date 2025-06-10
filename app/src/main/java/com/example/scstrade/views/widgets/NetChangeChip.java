package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
import com.example.scstrade.databinding.NetChangeChipBinding;
import com.example.scstrade.databinding.VolumeChipBinding;
import com.example.scstrade.helper.Utils;

public class NetChangeChip extends RelativeLayout {
    public NetChangeChipBinding binding;
    public String previousText="0.0";
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

    public void setText(String nChange,String pClose){
        double netChange = Double.parseDouble(nChange);
        double preClose = Double.parseDouble(pClose);

        String changeSign = netChange > 0.0 ? "+" : "";
        String changeValue = Utils.Companion.formatDouble(netChange);
        double changePercent = (netChange / preClose) * 100;
        String percentValue = Utils.Companion.formatDouble(changePercent);

        String result = changeSign + changeValue + " " + changeSign + "(" + percentValue + "%)";
        if (nChange.contains("-")) {
            binding.relativeLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.rounded_gray_red));
            binding.tv.setTextColor(ContextCompat.getColor(getContext(),R.color.md_theme_error));
        } else {
            binding.relativeLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.rounded_gray_green));
            binding.tv.setTextColor(ContextCompat.getColor(getContext(),R.color.md_theme_primary));
        }
        /*if(nChange.equals("0") || nChange.equals("0.0")){
            binding.relativeLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.rounded_gray_blue));
            binding.tv.setTextColor(Color.parseColor("#1A73E8"));
        }else {
            if (nChange.contains("-")) {
                binding.relativeLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.rounded_gray_red));
                binding.tv.setTextColor(ContextCompat.getColor(getContext(),R.color.md_theme_error));
            } else {
                binding.relativeLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.rounded_gray_green));
                binding.tv.setTextColor(ContextCompat.getColor(getContext(),R.color.md_theme_primary));
            }
        }*/
        binding.tv.setText(result);
        previousText=nChange;
    }

    public String getText(){
        return binding.tv.getText().toString();
    }
}
