package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;

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
        Log.e("LifecycleOwner", "Context class: " + context.getClass().getName()+" "+(context instanceof AppCompatActivity));
        if(attrs!=null){
            TypedArray a=getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.mMarket,
                    0,0
            );
            try{

                SharedViewModel sharedViewModel=((MyApp) context.getApplicationContext()).viewModel;
                LifecycleOwner lifecycleOwner =  (LifecycleOwner) context;

                sharedViewModel.getMutableIndices().observe(lifecycleOwner, listResource -> {
                    if(listResource.getData()!=null){
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
            }catch (Exception e){
                Log.e("LifecycleOwner",e.getMessage());
            }
            finally {
                a.recycle();
            }
        }


    }
}
