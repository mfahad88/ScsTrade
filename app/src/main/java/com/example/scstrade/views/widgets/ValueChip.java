package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
import com.example.scstrade.databinding.VolumeChipBinding;
import com.example.scstrade.helper.Utils;

public class ValueChip extends RelativeLayout {
    public VolumeChipBinding binding;
    public String previousText="0.0";
    public ValueChip(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ValueChip(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }



    private void init(AttributeSet attrs) {
        binding=VolumeChipBinding.inflate(LayoutInflater.from(getContext()),this,true);
        if(attrs!=null){
            TypedArray a=getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.VolumeChip,
                    0,0
            );
            try{

                String text=a.getString(R.styleable.VolumeChip_android_text);
                int color=a.getColor(R.styleable.VolumeChip_android_background, Color.TRANSPARENT);
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
        if(!TextUtils.isEmpty(text)){
            Double vol=Double.parseDouble(text);
            if (text.contains("-")) {
                binding.relativeLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.rounded_gray_red));
                binding.tv.setTextColor(ContextCompat.getColor(getContext(),R.color.md_theme_error));
            } else {
                binding.relativeLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.rounded_gray_green));
                binding.tv.setTextColor(ContextCompat.getColor(getContext(),R.color.md_theme_primary));
            }
            /*if(previousText.equals(text)){
                binding.relativeLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.rounded_gray_blue));
                binding.tv.setTextColor(Color.parseColor("#1A73E8"));
            }else{
                if (text.contains("-")) {
                    binding.relativeLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.rounded_gray_red));
                    binding.tv.setTextColor(ContextCompat.getColor(getContext(),R.color.md_theme_error));
                } else {
                    binding.relativeLayout.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.rounded_gray_green));
                    binding.tv.setTextColor(ContextCompat.getColor(getContext(),R.color.md_theme_primary));
                }
            }*/
            binding.tv.setText("Value: "+ Utils.Companion.convertToMillions(vol));
            previousText=text;
        }

    }

    public String getText(){
        return binding.tv.getText().toString();
    }
}
