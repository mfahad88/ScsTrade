package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.example.scstrade.R;
import com.example.scstrade.databinding.TermsConditionBinding;
import com.google.android.material.card.MaterialCardView;

public class TermsConditionView extends MaterialCardView {
    TermsConditionBinding binding;

    public TermsConditionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = TermsConditionBinding.inflate(LayoutInflater.from(context),this,true);

        if(attrs!=null){
            TypedArray a=context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.TermsConditionView,
                    0,0
            );
            try {
                String btnTitle = a.getString(R.styleable.TermsConditionView_buttonTitle);
                String title = a.getString(R.styleable.TermsConditionView_title);
                if(btnTitle!=null){
                    binding.downloadPdf.setText(btnTitle);
                }

                if(title!=null){
                    binding.acceptGene.setText(title);
                }


            }finally {
                a.recycle();
            }
        }

    }

    public void setChecked(boolean isChecked){
        binding.checkBox.setChecked(isChecked);
    }

    public boolean getChecked(){
        return binding.checkBox.isChecked();
    }

}
