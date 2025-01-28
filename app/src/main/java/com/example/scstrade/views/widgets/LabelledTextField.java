package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.scstrade.R;
import com.example.scstrade.databinding.LabelledTextfieldBinding;

public class LabelledTextField extends LinearLayout {
    LabelledTextfieldBinding binding;
    public LabelledTextField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding=LabelledTextfieldBinding.inflate(LayoutInflater.from(context),this,true);
        if(attrs!=null){
            TypedArray a=context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.LabelledTextField,
                    0,0
            );
            try {
                String hint = a.getString(R.styleable.LabelledTextField_hintText);
                if(hint!=null){
                    binding.textInputLayout.setHint(hint);
                }
            }finally {
                a.recycle();
            }
        }
    }

    public void setHint(String hint){
        binding.textInputLayout.setHint(hint);
    }

    public String getText(){
        return  binding.textInputEditText.getText() !=null ? binding.textInputEditText.getText().toString() : "";
    }

    public void setText(String text){
        binding.textInputEditText.setText(text);
    }
}
