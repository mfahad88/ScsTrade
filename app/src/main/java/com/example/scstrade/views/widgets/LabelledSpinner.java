package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scstrade.R;
import com.example.scstrade.databinding.LabelledSpinnerBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class LabelledSpinner extends TextInputLayout {
    LabelledSpinnerBinding binding;
    public AutoCompleteTextView dropdown;
    public LabelledSpinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = LabelledSpinnerBinding.inflate(LayoutInflater.from(context),this,true);
        if(attrs!=null){
            TypedArray a=context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.LabelledSpinner,
                    0,0
            );
            try {
                String hint = a.getString(R.styleable.LabelledSpinner_hintLabel);
                String dropdownLabel = a.getString(R.styleable.LabelledSpinner_dropdownLabel);

                if(hint!=null){
                    binding.textInput.setHint(hint);
                }
                if(dropdownLabel!=null){
                    binding.dropdown.setHint(dropdownLabel);
                }
                if(a.getTextArray(R.styleable.LabelledSpinner_entries)!=null) {

                    binding.dropdown.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, a.getTextArray(R.styleable.LabelledSpinner_entries)));
                }
                dropdown=binding.dropdown;
            }finally {
                a.recycle();
            }
        }
    }

    public void setEntries(List<String> entries){
        binding.dropdown.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,entries));
    }
}
