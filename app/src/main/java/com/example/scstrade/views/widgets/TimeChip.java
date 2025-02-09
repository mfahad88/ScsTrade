package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;


import androidx.appcompat.content.res.AppCompatResources;

import com.example.scstrade.R;
import com.example.scstrade.databinding.TimeChipBinding;

public class TimeChip extends RelativeLayout {
    private TimeChipBinding binding;
    public TimeChip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TimeChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding=TimeChipBinding.inflate(LayoutInflater.from(context),this,true);
        if(attrs!=null){
            TypedArray a=getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.TimeChip,
                    0,0
            );
            try{

                String text=a.getString(R.styleable.TimeChip_android_text);
                Boolean selected=a.getBoolean(R.styleable.TimeChip_setSelected,false);
                if(text!=null){
                    binding.tv.setText(text);
                }
                setSelected(selected);
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

    public void setChipSelected(Boolean isSelected){
       if(isSelected){
           binding.chip.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.time_chip_selected));
           binding.tv.setTextColor(Color.parseColor("#FFFFFF"));
       }else{
           binding.chip.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.time_chip_unselected));
           binding.tv.setTextColor(Color.parseColor("#49454F"));
       }
    }




}
