package com.example.scstrade.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scstrade.databinding.LabelSpinnerBinding;

public class LabelSpinner extends androidx.appcompat.widget.AppCompatSpinner {
    LabelSpinnerBinding binding;
    public LabelSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }



    public LabelSpinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LabelSpinner(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        binding = LabelSpinnerBinding.inflate(LayoutInflater.from(context));

    }
}
