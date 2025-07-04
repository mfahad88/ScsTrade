package com.example.scstrade.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.scstrade.R;
import com.example.scstrade.databinding.LabelledSpinnerBinding;
import com.example.scstrade.model.Resource;
import com.example.scstrade.model.response.ApiResponse;
import com.example.scstrade.model.response.aof.country.CountryDto;
import com.example.scstrade.viewmodels.AofViewModel;
import com.example.scstrade.views.MyApp;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class CountyDropDown extends TextInputLayout {
    LabelledSpinnerBinding binding;
    public AutoCompleteTextView dropdown;
    private Pair<String,String> selectedDropDown;
    public MutableLiveData<Pair<String,String>> mutableSelectedDropdown;
    public List<Pair<String,String>> entries;
    public CountyDropDown(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public boolean isEmpty(){
        return selectedDropDown==null;
    }

    private void init(Context context, AttributeSet attrs) {
        binding = LabelledSpinnerBinding.inflate(LayoutInflater.from(context),this,true);
        mutableSelectedDropdown=new MutableLiveData<>();
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
                binding.dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                        String selectedLabel = (String) parent.getItemAtPosition(position);
                        for (Pair<String,String> item : entries) {
                            if (item.second.equalsIgnoreCase(selectedLabel)) {
                                selectedDropDown = item; // e.g. "S"
                                mutableSelectedDropdown.setValue(item);
//                                break;
                            }
                        }
                    }
                });

                dropdown=binding.dropdown;
            }finally {
                a.recycle();
            }
        }
    }

    public Pair<String, String> getSelectedDropDown() {
        return selectedDropDown;
    }

    public void setSelectedDropDown(String selectedDropDown) {
        if(!TextUtils.isEmpty(selectedDropDown)){
            for (Pair<String,String> item:entries){
                if(item.first.equalsIgnoreCase(selectedDropDown)){
                    binding.dropdown.setText(item.second,false);
                    this.selectedDropDown=item;
                    mutableSelectedDropdown.setValue(item);
//                    break;
                }
            }
        }

    }

    public void setListEntries(List<Pair<String,String>> entries){
        this.entries=entries;
        List<String> labels= new ArrayList<>();
        for (Pair<String,String> item:this.entries){
            labels.add(item.second.toString());
        }
        binding.dropdown.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,labels));

    }
}
