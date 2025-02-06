package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.scstrade.R;
import com.example.scstrade.databinding.TradeValueViewBinding;

public class TradeValueView extends RelativeLayout {
    private TradeValueViewBinding binding;
    public TradeValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TradeValueView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        binding=TradeValueViewBinding.inflate(LayoutInflater.from(getContext()),this,true);
        if(attrs!=null){
            TypedArray a=getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.TradeValueView,
                    0,0
            );
            try{

                String text=a.getString(R.styleable.TradeValueView_android_text);
                int resource=a.getResourceId(R.styleable.TradeValueView_android_src,-1);
                if(text!=null){
                    binding.indexValue.setText(text);
                }
                if(resource!=-1){
                    binding.imageDropUp.setImageDrawable(AppCompatResources.getDrawable(getContext(),resource));
                }

            }finally {
                a.recycle();
            }
        }
    }
    public void setText(String text){
        binding.indexValue.setText(text);
    }

    public String getText(){
        return binding.indexValue.getText().toString();
    }

    public void setDrawable(Drawable drawable) {
        binding.imageDropUp.setImageDrawable(drawable);
    }

    public Drawable getDrawable() {
        return binding.imageDropUp.getDrawable();
    }
}
