package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scstrade.R;
import com.example.scstrade.databinding.MmarketBinding;
import com.example.scstrade.viewmodels.SharedViewModel;
import com.example.scstrade.views.MyApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class mMarket extends LinearLayout {
   public MmarketBinding binding;

    public mMarket(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public mMarket(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding=MmarketBinding.inflate(LayoutInflater.from(context),this,true);
        if(attrs!=null){
            TypedArray a=getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.mMarket,
                    0,0
            );
            try{
                SharedViewModel sharedViewModel=((MyApp) context.getApplicationContext()).viewModel;
                sharedViewModel.getMutableIndices().observe((AppCompatActivity)context, listResource -> {
                    if(!Objects.requireNonNull(listResource.getData()).isEmpty()){
                        if(listResource.getData().get(0).getMarketStatus().equalsIgnoreCase("close")){
                            binding.close.setVisibility(VISIBLE);
                            binding.open.setVisibility(GONE);
                        }else {
                            binding.close.setVisibility(GONE);
                            binding.open.setVisibility(VISIBLE);
                        }
                        SimpleDateFormat sdf= new SimpleDateFormat("dd MMM yyyy | hh:mma", Locale.ENGLISH);
                        binding.dateTime.setText(sdf.format(new Date()));
                    }
                });
            }finally {
                a.recycle();
            }
        }


    }
}
