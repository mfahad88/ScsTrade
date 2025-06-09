package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.example.scstrade.R;
import com.example.scstrade.databinding.GrayCardBinding;
import com.google.android.material.card.MaterialCardView;

public class GrayCardView extends MaterialCardView {
    public GrayCardBinding binding;

    public void setText(String text) {
        binding.currentMarValue.setText(text);
    }


    public GrayCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding=GrayCardBinding.inflate(LayoutInflater.from(getContext()),this,true);
        if(attrs!=null){
            TypedArray a=context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.GrayCardView,
                    0,0
            );
            try {
                String label = a.getString(R.styleable.GrayCardView_titleGrayView);
                String text = a.getString(R.styleable.GrayCardView_android_text);

                if(label!=null){
                    binding.currentMar.setHint(label);
                }
                if(text!=null){
                    binding.currentMarValue.setHint(text);
                }

            }finally {
                a.recycle();
            }
        }
    }
}
