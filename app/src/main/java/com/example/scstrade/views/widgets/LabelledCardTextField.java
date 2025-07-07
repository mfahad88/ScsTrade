package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scstrade.R;
import com.example.scstrade.databinding.LabelledCardTextfieldBinding;
import com.google.android.material.textfield.TextInputLayout;

public class LabelledCardTextField extends TextInputLayout {
    LabelledCardTextfieldBinding binding;
    public EditText text1,text2;
    public LabelledCardTextField(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = LabelledCardTextfieldBinding.inflate(LayoutInflater.from(context),this,true);
        if(attrs!=null){
            TypedArray a=context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.LabelledCardTextField,
                    0,0
            );

           try {
               String title=a.getString(R.styleable.LabelledCardTextField_titleLabelledCardTextField);
               String hint1=a.getString(R.styleable.LabelledCardTextField_hint1);
               String hint2=a.getString(R.styleable.LabelledCardTextField_hint2);
               if(hint1!=null){
                   binding.text1.setInputType(InputType.TYPE_CLASS_TEXT);
                   binding.text1.setHint(hint1);
               }

               if(hint2!=null){
                   binding.text2.setHint(hint2);
                   binding.text2.setInputType(InputType.TYPE_CLASS_NUMBER);
               }

               if(title!=null){
                   binding.kindlyEnte.setText(title);
               }
               text1 = binding.text1;
               text2 = binding.text2;
           }catch (Exception e){
               e.printStackTrace();
           }finally {
               a.recycle();
           }
        }
    }
}
