package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.scstrade.R;
import com.example.scstrade.databinding.LabelledTextfieldBinding;
import com.google.android.material.textfield.TextInputEditText;

public class LabelledTextField extends LinearLayout {
    LabelledTextfieldBinding binding;
    public TextInputEditText textInputEditText;
    private OnFocus listener;
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
                String hintField = a.getString(R.styleable.LabelledTextField_hintTextField);
                String text = a.getString(R.styleable.LabelledTextField_android_text);
                String infoText= a.getString(R.styleable.LabelledTextField_infoText);
                binding.textInputEditText.setInputType(a.getInt(R.styleable.LabelledTextField_android_inputType,0));
                binding.textInputEditText.setFilters( new InputFilter[]{ new InputFilter.LengthFilter(a.getInt(R.styleable.LabelledTextField_android_maxLength,100)) });
                binding.textInputEditText.setMaxLines(a.getInt(R.styleable.LabelledTextField_android_maxLength,1));
                binding.textInputLayout.setPasswordVisibilityToggleEnabled(a.getBoolean(R.styleable.LabelledTextField_passwordToggleEnabled,false));
                binding.textInputEditText.setCompoundDrawablesWithIntrinsicBounds(null,null,a.getDrawable(R.styleable.LabelledTextField_android_drawableEnd),null);
                if(hint!=null){
                    binding.textInputLayout.setHint(hint);
                }
                if(hintField!=null){
                    binding.textInputEditText.setHint(hintField);
                }
                if(text!=null){
                    binding.textInputEditText.setText(text);
                }

                if(infoText!=null){
                    binding.supporting.setText(infoText);
                    binding.supporting.setVisibility(View.VISIBLE);
                }else{
                    binding.supporting.setVisibility(View.GONE);
                }
                textInputEditText=binding.textInputEditText;
                binding.textInputEditText.setOnFocusChangeListener((view, b) -> {
                    if(listener!=null) {
                        listener.onChange(b);
                    }
                });

                if(hint.toLowerCase().contains("mobile")){
                    textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                }

             /*   if(hintField!="" || hintField!=null) {
                    if (hintField.toLowerCase().contains("uin") || hintField.toLowerCase().contains("nic")
                            || hintField.toLowerCase().contains("cnic")) {
                        textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                    }
                }
*/
            }finally {
                a.recycle();
            }
        }

    }

    public void setOnFocusListener(OnFocus listener){
        this.listener=listener;
    }
    public interface OnFocus{
        void onChange(boolean b);
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
