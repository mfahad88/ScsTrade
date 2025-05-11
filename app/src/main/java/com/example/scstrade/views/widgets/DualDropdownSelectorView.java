package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.scstrade.R;
import com.example.scstrade.databinding.DualDropdownSelectorViewBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DualDropdownSelectorView extends MaterialCardView {
    DualDropdownSelectorViewBinding binding;
    ArrayAdapter adapter1,adapter2;
    public void setSelectedListener1(AdapterView.OnItemSelectedListener selectedListener1) {
        binding.autocompleteTextview1.setOnItemSelectedListener(selectedListener1);
    }

    public void setSelectedListener2(AdapterView.OnItemSelectedListener selectedListener2) {
        binding.autocompleteTextview2.setOnItemSelectedListener(selectedListener2);
    }


    public DualDropdownSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = DualDropdownSelectorViewBinding.inflate(LayoutInflater.from(context),this,true);

        if(attrs!=null){
            TypedArray a=context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.DualDropdownSelectorView,
                    0,0
            );

            String titleDualDropdown=a.getString(R.styleable.DualDropdownSelectorView_titleDualDropdown);
            String hint1= a.getString(R.styleable.DualDropdownSelectorView_hintDualDropdown1);
            String hint2= a.getString(R.styleable.DualDropdownSelectorView_hintDualDropdown2);
            if(titleDualDropdown!=null){
                binding.title.setText(titleDualDropdown);
                binding.title.setVisibility(View.VISIBLE);
            }else{
                binding.title.setVisibility(View.GONE);
            }

            binding.autocompleteTextview1.setHint(hint1);
            binding.autocompleteTextview2.setHint(hint2);


        }
    }

    public void setList1(List<String> list){
       this.adapter1= new ArrayAdapter(this.binding.getRoot().getContext(), android.R.layout.simple_list_item_1,list);
       this.binding.autocompleteTextview1.setAdapter(this.adapter1);
    }

    public void setList2(List<String> list){
        this.adapter2= new ArrayAdapter(this.binding.getRoot().getContext(), android.R.layout.simple_list_item_1,list);
        this.binding.autocompleteTextview2.setAdapter(this.adapter2);

    }



}
