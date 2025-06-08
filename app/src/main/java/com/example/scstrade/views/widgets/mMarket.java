package com.example.scstrade.views.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import com.example.scstrade.R;
import com.example.scstrade.databinding.CustomToolbarBinding;
import com.example.scstrade.databinding.MmarketBinding;
import com.example.scstrade.viewmodels.SharedViewModel;
import com.example.scstrade.views.MyApp;
import com.example.scstrade.views.notification.NotificationActivity;
import com.example.scstrade.views.search.SearchActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class mMarket extends LinearLayout {
   public CustomToolbarBinding binding;
   public ConstraintLayout content;

    public mMarket(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public mMarket(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding= CustomToolbarBinding.inflate(LayoutInflater.from(context),this,true);
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
                if(getActivity(context)!=null){
                    Log.e("Activity",((Activity)context).getClass().getSimpleName());
                    if(((Activity)context).getClass().getSimpleName().equals("MainActivity")){
                        binding.group.setVisibility(View.VISIBLE);

                        binding.titleItem.setVisibility(View.GONE);

                    }else{
                        binding.titleItem.setVisibility(View.VISIBLE);
                        binding.group.setVisibility(View.GONE);
                        binding.titleItem.setText(((Activity)context).getClass().getSimpleName().replace("Activity","").replace("Detail",""));

                    }

                }

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
               binding.notificationIcon.setOnClickListener(view ->context.startActivity(new Intent(context, NotificationActivity.class)));
                binding.searchIcon.setOnClickListener(view -> {
                   context.startActivity(new Intent(context, SearchActivity.class));
                });
                ViewCompat.setOnApplyWindowInsetsListener(binding.content, new androidx.core.view.OnApplyWindowInsetsListener() {
                    @NonNull
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(@NonNull View view, @NonNull WindowInsetsCompat windowInsets) {
                        Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout());
                        view.setPadding(0,insets.top,0,insets.bottom);

                        return windowInsets;
                    }
                }) ;

                content=binding.content;
            }catch (Exception e){
                Log.e("LifecycleOwner",e.getMessage());
            }
            finally {
                a.recycle();
            }
        }


    }



    public Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }
}
