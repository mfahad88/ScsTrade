package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.scstrade.R;

public class MarketValueView extends LinearLayout {

    public TextView tvTitle, tvValue;
    private boolean highlightEnabled;

    public MarketValueView(Context context) {
        super(context);
        init(context, null);
    }

    public MarketValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MarketValueView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        setPadding(dpToPx(12), 0, dpToPx(12), 0);
        LayoutInflater.from(context).inflate(R.layout.view_market_value, this, true);

        tvTitle = findViewById(R.id.tvTitle);
        tvValue = findViewById(R.id.tvValue);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MarketValueView);
            String title = a.getString(R.styleable.MarketValueView_mv_title);
            String value = a.getString(R.styleable.MarketValueView_mv_value);
            boolean isColor = a.getBoolean(R.styleable.MarketValueView_isColor_Enabled, false);
            a.recycle();

            setTitle(title);
            setValue(value);
            if(isColor){
                if(value.contains("-")){
                    setBackgroundResource(R.drawable.bg_market_negative);
                    tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.md_theme_error));
                    tvValue.setTextColor(ContextCompat.getColor(getContext(), R.color.md_theme_error));
                }else {
                    setBackgroundResource(R.drawable.bg_market_positive);
                    tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.md_theme_primary));
                    tvValue.setTextColor(ContextCompat.getColor(getContext(), R.color.md_theme_primary));
                }
            }else{
                setBackgroundResource(R.drawable.bg_market_normal);
                tvValue.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            }

            tvValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   if(isColor){
                       if(tvValue.getText().toString().contains("-")){
                           setBackgroundResource(R.drawable.bg_market_negative);
                           tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.md_theme_error));
                           tvValue.setTextColor(ContextCompat.getColor(getContext(), R.color.md_theme_error));
                       }else {
                           setBackgroundResource(R.drawable.bg_market_positive);
                           tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.md_theme_primary));
                           tvValue.setTextColor(ContextCompat.getColor(getContext(), R.color.md_theme_primary));
                       }
                   }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

//            setHighlightEnabled(highlight);
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title) && !title.trim().isEmpty()) {
            tvTitle.setText(title);
        } else {
            tvTitle.setText(""); // Or use default/fallback
        }
    }

    public void setValue(String value) {
        if (!TextUtils.isEmpty(value) && !value.trim().isEmpty()) {
            tvValue.setText(value);
        } else {
            tvValue.setText("N/A"); // Fallback
        }
    }

  /*  public void setHighlightEnabled(boolean enabled) {
        this.highlightEnabled = enabled;
        if (enabled) {
            setBackgroundResource(R.drawable.bg_market_highlight);
            tvValue.setTextColor(ContextCompat.getColor(getContext(), R.color.md_theme_primary));
        } else {
            setBackgroundResource(R.drawable.bg_market_normal);
            tvValue.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }
    }*/

    public boolean isHighlightEnabled() {
        return highlightEnabled;
    }
}