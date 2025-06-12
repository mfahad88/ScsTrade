package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.scstrade.R;
import com.example.scstrade.databinding.HighBinding;
import com.example.scstrade.databinding.LowViewBinding;
import com.example.scstrade.helper.Utils;

public class LowView extends RelativeLayout {
    private LowViewBinding binding;
    public LowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        binding= LowViewBinding.inflate(LayoutInflater.from(getContext()),this,true);
        if(attrs!=null){
            TypedArray a=getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.LowView,
                    0,0
            );
            try{

                String text=a.getString(R.styleable.LowView_android_text);
                if(text!=null){
                    binding.tv.setText(text);
                }
                if(Utils.Companion.getSmallestWidthDp(getContext())<400) {
                    binding.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                }else{
                    binding.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
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
