package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.example.scstrade.R;
import com.example.scstrade.databinding.DualOptionToggleViewBinding;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DualOptionToggleView extends RelativeLayout {
    DualOptionToggleViewBinding binding;
    public EditText editText;
    List<Pair<String,String>> list;
    public String selectedOption;

    public void setTextFieldValue(String textFieldValue) {
        this.textFieldValue = textFieldValue;
        binding.editText.setText(textFieldValue);
    }

    private String textFieldValue;
    private OnButtonClickListener listenerOne, listenerTwo;
    private OnFocus listener;
    public DualOptionToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = DualOptionToggleViewBinding.inflate(LayoutInflater.from(context),this,true);
        if(attrs!=null){
            TypedArray a=context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.DualOptionToggleView,
                    0,0
            );
            try{

                String title=a.getString(R.styleable.DualOptionToggleView_titleDualOption);
                String subTitle=a.getString(R.styleable.DualOptionToggleView_subTitleDualOption);
                String hint= a.getString(R.styleable.DualOptionToggleView_hintDualOption);
                String btn1=a.getString(R.styleable.DualOptionToggleView_titleButton1);
                String btn2=a.getString(R.styleable.DualOptionToggleView_titleButton2);
                Drawable drawable= a.getDrawable(R.styleable.DualOptionToggleView_drawable);

                if(title!=null){
                    binding.selectYour.setText(title);
                    binding.selectYour.setVisibility(View.VISIBLE);
                }else{
                    binding.selectYour.setVisibility(View.GONE);
                }

                if(drawable!=null){
                    binding.imageView.setImageDrawable(drawable);
                    binding.imageView.setVisibility(View.VISIBLE);
                }else{
                    binding.imageView.setVisibility(View.GONE);
                }
                if(hint!=null) {
                    binding.editText.setHint(hint);
                    editText=binding.editText;
                    binding.textField.setVisibility(View.VISIBLE);
                }else{
                    binding.textField.setVisibility(View.GONE);
                }

                binding.editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            textFieldValue=charSequence.toString();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                if(subTitle!=null){
                    binding.subHeading.setText(subTitle);
                    binding.subHeading.setVisibility(View.VISIBLE);
                }else{
                    binding.subHeading.setVisibility(View.GONE);
                }

                binding.text1.setText(btn1);
                binding.text2.setText(btn2);
//                toggleSelection(true);

                binding.btnSingle.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleSelection(true);
                        if(listenerOne!=null) {
                            listenerOne.onClick();
                        }
                    }
                });
                binding.textField.setOnFocusChangeListener((view, b) -> {
                    if(listener!=null){
                        listener.onChange(b);
                    }
                });
                binding.btnMarried.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleSelection(false);
                        if(listenerTwo!=null) {
                            listenerTwo.onClick();
                        }
                    }
                });

            }finally {
                a.recycle();
            }
        }
    }

    public void setOnButtonOneClickListener(OnButtonClickListener listener) {
        this.listenerOne = listener;
    }

    public void setOnButtonTwoClickListener(OnButtonClickListener listener) {
        this.listenerTwo = listener;
    }
    public interface OnButtonClickListener {
        void onClick();
    }

    public void setOnFocusListener(OnFocus listener){
        this.listener=listener;
    }
    public interface OnFocus{
        void onChange(boolean b);
    }
    public void setList(List<Pair<String,String>> list){
        binding.text1.setText(list.get(0).first);
        binding.text2.setText(list.get(1).first);
        this.list = list;
    }


    public String getTextFieldValue(){
        return textFieldValue;
    }

    public void setSelectedOption(String option){
        if(list.get(0).second.equalsIgnoreCase(option)){
            selectedOption=option;
            toggleSelection(true);
        }else{
            selectedOption=option;
            toggleSelection(false);
        }
    }

    public void toggleSelection(Boolean isButtonOneSelected){
        if(isButtonOneSelected){
            binding.btnSingle.setSelected(true);
            binding.btnMarried.setSelected(false);
            binding.text1.setTextColor(Color.parseColor("#ffffff"));
            binding.text2.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(),R.color.md_theme_primary));
            for (Pair<String, String> item : list) {
                if (item.first.equalsIgnoreCase(binding.text1.getText().toString())) {
                    String value = item.second;  // this will be "S"
                    selectedOption = value;
                    System.out.println("Value for " +item.first+": "+ value);
                    break;
                }
            }
        }else{
            binding.btnSingle.setSelected(false);
            binding.btnMarried.setSelected(true);
            binding.text1.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(),R.color.md_theme_primary));
            binding.text2.setTextColor(Color.parseColor("#ffffff"));
            for (Pair<String, String> item : list) {
                if (item.first.equalsIgnoreCase(binding.text2.getText().toString())) {
                    String value = item.second;  // this will be "S"
                    selectedOption = value;
                    System.out.println("Value for " +item.first+": "+ value);
                    break;
                }
            }
        }
    }
}
