package com.example.scstrade.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

import com.example.scstrade.R;
import com.example.scstrade.databinding.CustomSpinnerBinding;

public class CustomSpinner extends AppCompatSpinner {
    public CustomSpinner(Context context) {
        super(context);
        init(context);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
        init(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init(context);
    }

    private void init(Context context) {
     try{
         View view=  LayoutInflater.from(context).inflate(R.layout.custom_spinner,this,false);
         TextView textView=view.findViewById(R.id.spinner);
         // Setup ArrayAdapter with custom layouts
         ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                 context,
                 R.array.spinner_country,
                 android.R.layout.simple_spinner_dropdown_item // Custom layout for the spinner item
         );
         adapter.setDropDownViewResource(R.layout.custom_spinner); // Dropdown layout
         setAdapter(adapter);
     }catch (Exception e){
         Log.e("Error: ",e.getMessage());
     }
    }
}
