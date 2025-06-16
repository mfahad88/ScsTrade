package com.example.scstrade.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;

public abstract class TickerItem {
    public abstract View toView(Context context);

    public static class Text extends TickerItem {
        private final String text;

        public Text(String text) {
            this.text = text;
        }

        @Override
        public View toView(Context context) {
            TextView tv = new TextView(context);
            tv.setText(text);
            tv.setTextColor(Color.parseColor("#484740"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setPadding(16, 8, 16, 8);
            return tv;
        }
    }

    public static class Image extends TickerItem {
        private final int resId;

        public Image(int resId) {
            this.resId = resId;
        }

        @Override
        public View toView(Context context) {
            ImageView iv = new ImageView(context);
            iv.setImageResource(resId);
            iv.setLayoutParams(new ViewGroup.LayoutParams(40, 40));
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return iv;
        }
    }
}
