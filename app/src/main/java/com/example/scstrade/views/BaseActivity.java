package com.example.scstrade.views;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    /*@Override
    protected void attachBaseContext(Context newBase) {
        // Lock the display density to the default value
        Configuration config = new Configuration();
        config.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
        config.fontScale = 1.0f;
        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfig) {
        if (overrideConfig != null) {
            overrideConfig.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
        }
        super.applyOverrideConfiguration(overrideConfig);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Ensure density stays locked after configuration changes
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        metrics.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
        getResources().updateConfiguration(getResources().getConfiguration(), metrics);
        super.onCreate(savedInstanceState);
    }*/
}